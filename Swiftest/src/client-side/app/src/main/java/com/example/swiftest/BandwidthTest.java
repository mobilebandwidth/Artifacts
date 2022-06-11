package com.example.swiftest;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class BandwidthTest {

    Context context;

    final static private int InitTimeout = 500;
    final static private int PingTimeout = 5000;
    final static private int MinFinishedNum = 5;

    final static private int ThreadNum = 4;
    final static private int ServerCapability = 100;            // 100Mbps per server

    final static private int TestTimeout = 2000;                // Maximum test duration
    final static private int MaxTrafficUse = 200;               // Maximum traffic limit

    final static private int SamplingInterval = 10;             // Time interval for Sampling
    final static private int SamplingWindow = 50;               // Sampling overlap

    final static private int CheckerSleep = 50;                 // Time interval between checks
    final static private int CheckerWindowSize = 10;            // SimpleChecker window size
    final static private int CheckerSelectedSize = 8;           // SimplerChecker selected size
    final static private double CheckerThreshold = 0.03;        // 3% threshold
    final static private int CheckerTimeoutWindow = 50;         // Window size when overtime


    final static private String MasterIP = "118.31.164.30";

    public String bandwidth_Mbps = "0";
    public String duration_s = "0";
    public String traffic_MB = "0";
    public String networkType;

    boolean stop = false;

    BandwidthTest(Context context) {
        this.context = context;
    }

    public void stop() {
        stop = true;
    }

    static class PingThread extends Thread implements Comparable<PingThread> {
        long rtt;
        String ip;
        boolean finished;

        PingThread(String ip) {
            this.ip = ip;
            this.finished = false;
            this.rtt = PingTimeout * 2;
        }

        public void run() {
            try {
                URL url = new URL("http://" + ip + "/testping.html");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(PingTimeout);
                connection.setReadTimeout(PingTimeout);
                long nowTime = System.currentTimeMillis();
                connection.connect();
                connection.getResponseCode();
                rtt = System.currentTimeMillis() - nowTime;
                connection.disconnect();
                finished = true;
            } catch (IOException e) {
                finished = true;
            }
        }

        @Override
        public int compareTo(PingThread pingThread) {
            return Long.compare(rtt, pingThread.rtt);
        }
    }

    static class InitThread extends Thread {
        public ArrayList<String> ipList;

        InitThread() {
            this.ipList = new ArrayList<>();
        }

        public void run() {
            try {
                URL url = new URL("http://" + MasterIP + ":8080/speedtest/iplist/available");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(InitTimeout);
                connection.setReadTimeout(InitTimeout);
                connection.connect();

                if (connection.getResponseCode() == 200) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null)
                        stringBuilder.append(line);

                    JSONObject jsonObject = new JSONObject(stringBuilder.toString());

                    int server_num = jsonObject.getInt("server_num");
                    JSONArray jsonArray = jsonObject.getJSONArray("ip_list");
                    ArrayList<String> ipList = new ArrayList<>();
                    for (int i = 0; i < server_num; ++i)
                        ipList.add(jsonArray.getString(i));
                    connection.disconnect();

                    ArrayList<PingThread> pingThreads = new ArrayList<>();
                    for (String ip : ipList)
                        pingThreads.add(new PingThread(ip));
                    for (PingThread pingThread : pingThreads)
                        pingThread.start();

                    long start_time = System.currentTimeMillis();
                    while (true) {
                        int finish_num = 0;
                        for (PingThread pingThread : pingThreads)
                            if (pingThread.finished)
                                finish_num++;
                        if (finish_num == server_num)
                            break;
                        if (System.currentTimeMillis() - start_time > InitTimeout && finish_num > MinFinishedNum)
                            break;
                    }

                    Collections.sort(pingThreads);

                    for (PingThread pingThread : pingThreads)
                        this.ipList.add(pingThread.ip);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }

    static class SimpleChecker extends Thread {
        ArrayList<Double> speedSample;
        boolean finish;
        Double simpleSpeed;

        SimpleChecker(ArrayList<Double> speedSample) {
            this.speedSample = speedSample;
            this.finish = false;
            this.simpleSpeed = 0.0;
        }

        public void run() {
            while (!finish) {
                try {
                    sleep(CheckerSleep);

                    int n = speedSample.size();
                    if (n < CheckerWindowSize) continue;

                    ArrayList<Double> recentSamples = new ArrayList<>();
                    for (int i = n - CheckerWindowSize; i < n; ++i)
                        recentSamples.add(speedSample.get(i));
                    Collections.sort(recentSamples);
                    int windowNum = CheckerWindowSize - CheckerSelectedSize + 1;
                    for (int i = 0; i < windowNum; ++i) {
                        int j = i + CheckerSelectedSize - 1;
                        double lower = recentSamples.get(i), upper = recentSamples.get(j);
                        // Here no division by 0 is considered,
                        // but (NaN < CheckerThreshold) so it's work!
                        // All 0 should not go through this condition.
                        if ((upper - lower) / upper < CheckerThreshold) {
                            double res = 0;
                            for (int k = i; k <= j; ++k)
                                res += recentSamples.get(k);
                            simpleSpeed = res / CheckerSelectedSize;
                            finish = true;
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    double res = 0.0;
                    int n = speedSample.size();
                    for (int k = n - CheckerTimeoutWindow; k < n; ++k)
                        res += speedSample.get(k);
                    simpleSpeed = res / CheckerTimeoutWindow;
                    break;
                }
            }
        }

        public double getSpeed() {
            return simpleSpeed;
        }
    }

    static class DownloadThread extends Thread {
        DatagramSocket socket;
        InetAddress address;
        boolean stopped;
        int port;
        int size;

        DownloadThread(String ip, int port) {
            try {
                this.address = InetAddress.getByName(ip);
                this.port = port;
                this.stopped = false;
                this.socket = new DatagramSocket();
                socket.setSoTimeout(TestTimeout);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @SuppressWarnings("InfiniteLoopStatement")
        public void run() {
            byte[] send_data = "1".getBytes();
            DatagramPacket send_packet = new DatagramPacket(send_data, send_data.length, address, port);
            byte[] stop_data = "stop".getBytes();
            DatagramPacket stop_packet = new DatagramPacket(stop_data, stop_data.length, address, port);
            try {
                socket.send(send_packet);

                int BUFFER_SIZE = 1024;
                byte[] receive_buf = new byte[BUFFER_SIZE * 2];
                DatagramPacket receive_packet = new DatagramPacket(receive_buf, receive_buf.length);

                while (!stopped) {
                    socket.receive(receive_packet);
                    String receive_data = new String(receive_packet.getData(), 0, receive_packet.getLength());
                    size += receive_data.length();
                }

                socket.send(stop_packet);
                socket.close();

            } catch (IOException e) {
//                e.printStackTrace();
            }
        }
    }

    static class DownloadThreadMonitor {
        ArrayList<DownloadThread> downloadThread;
        ArrayList<String> serverIP;
        int warmupNum;
        int stepNum;
        int serverNum;
        int runningServerNum;


        DownloadThreadMonitor(ArrayList<String> serverIP, String networkType) {
            this.serverIP = serverIP;
            this.downloadThread = new ArrayList<>();
            for (String ip : serverIP)
                for (int i = 0; i < ThreadNum; ++i)
                    downloadThread.add(new DownloadThread(ip, 9876));

            this.serverNum = serverIP.size();
            this.runningServerNum = 0;

            switch (networkType) {
                case "5G":
                    this.warmupNum = 5;
                    this.stepNum = 3;
                    break;
                case "WiFi":
                    this.warmupNum = 2;
                    this.stepNum = 2;
                    break;
                case "4G":
                    this.warmupNum = 2;
                    this.stepNum = 1;
                    break;
                default:
                    this.warmupNum = 1;
                    this.stepNum = 1;
                    break;
            }
        }

        void start() {
            for (int i = 0; i < warmupNum; ++i) {
                if (runningServerNum >= serverNum)
                    return;
                for (int j = 0; j < ThreadNum; ++j)
                    downloadThread.get(i * ThreadNum + j).start();
                Log.d("Server added:", serverIP.get(runningServerNum));
                runningServerNum++;
            }
        }

        public void add() {
            for (int i = 0; i < stepNum; ++i) {
                if (runningServerNum >= serverNum)
                    return;
                for (int j = 0; j < ThreadNum; ++j)
                    downloadThread.get(runningServerNum * ThreadNum + j).start();
                Log.d("Server added:", serverIP.get(runningServerNum));
                runningServerNum++;
            }
        }

        public void stop() throws InterruptedException {
            for (DownloadThread thread : downloadThread)
                thread.stopped = true;
            for (DownloadThread thread : downloadThread)
                thread.join();
        }

        public int capability() {
            return runningServerNum * ServerCapability;
        }
    }

    public void SpeedTest() throws InterruptedException {
        stop = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("No permission:", "ACCESS_FINE_LOCATION");
                return;
            }
        }

        networkType = getNetworkType();

        InitThread initThread = new InitThread();
        initThread.start();
        initThread.join();

        DownloadThreadMonitor downloadThreadMonitor = new DownloadThreadMonitor(initThread.ipList, networkType);

        ArrayList<Double> speedSample = new ArrayList<>();
        SimpleChecker checker = new SimpleChecker(speedSample);

        long startTime = System.currentTimeMillis();
        downloadThreadMonitor.start();
        checker.start();

        ArrayList<Double> sizeRecord = new ArrayList<>();
        ArrayList<Long> timeRecord = new ArrayList<>();
        int posRecord = -1;
        while (true) {
            try {
                Thread.sleep(SamplingInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            long downloadSize = 0;
            for (DownloadThread t : downloadThreadMonitor.downloadThread)
                downloadSize += t.size;
            double downloadSizeMBits = (double) (downloadSize) / 1024 / 1024 * 8;
            long nowTime = System.currentTimeMillis();

            while (posRecord + 1 < timeRecord.size() && nowTime - timeRecord.get(posRecord + 1) >= SamplingWindow)
                posRecord++;

            double speed = 0;
            if (posRecord >= 0)
                speed = (downloadSizeMBits - sizeRecord.get(posRecord)) * 1000.0 / (nowTime - timeRecord.get(posRecord));
                speedSample.add(speed);

            sizeRecord.add(downloadSizeMBits);
            timeRecord.add(nowTime);

            if (speed > downloadThreadMonitor.capability())
                downloadThreadMonitor.add();

            if (checker.finish) {
                Log.d("Bandwidth Test", "Test succeed.");
                break;
            }
            if (nowTime - startTime >= TestTimeout) {
                Log.d("Bandwidth Test", "Exceeding the time limit.");
                break;
            }
            if (downloadSizeMBits / 8 >= MaxTrafficUse) {
                Log.d("Bandwidth Test", "Exceeding the traffic limit.");
                break;
            }
            if (stop) {
                Log.d("Bandwidth Test", "Testing Stopped.");
                break;
            }
        }
        downloadThreadMonitor.stop();
        checker.interrupt();
        checker.join();

        bandwidth_Mbps = String.format(Locale.CHINA, "%.2f", checker.getSpeed());
        duration_s = String.format(Locale.CHINA, "%.2f", (double) (System.currentTimeMillis() - startTime) / 1000);
        traffic_MB = String.format(Locale.CHINA, "%.2f", sizeRecord.get(sizeRecord.size() - 1) / 8);

        Log.d("bandwidth_Mbps", bandwidth_Mbps);
        Log.d("duration_s", duration_s);
        Log.d("traffic_MB", traffic_MB);
    }

    String getNetworkType() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isAvailable())
            return "NONE";
        int connectionType = networkInfo.getType();
        if (connectionType == ConnectivityManager.TYPE_WIFI)
            return "WiFi";
        if (connectionType == ConnectivityManager.TYPE_MOBILE) {
            int cellType = networkInfo.getSubtype();
            switch (cellType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:     // api< 8: replace by 11
                case TelephonyManager.NETWORK_TYPE_GSM:      // api<25: replace by 16
                    return "2G";
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:   // api< 9: replace by 12
                case TelephonyManager.NETWORK_TYPE_EHRPD:    // api<11: replace by 14
                case TelephonyManager.NETWORK_TYPE_HSPAP:    // api<13: replace by 15
                case TelephonyManager.NETWORK_TYPE_TD_SCDMA: // api<25: replace by 17
                    return "3G";
                case TelephonyManager.NETWORK_TYPE_LTE:      // api<11: replace by 13
                case TelephonyManager.NETWORK_TYPE_IWLAN:    // api<25: replace by 18
                case 19: // LTE_CA
                    return "4G";
                case TelephonyManager.NETWORK_TYPE_NR:       // api<29: replace by 20
                    return "5G";
                default:
                    return "unknown";
            }
        }
        return "unknown";
    }
}