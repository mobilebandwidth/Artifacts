## Mobile Access Bandwidth in Practice: Measurement, Analysis, and Implications 

![license](https://img.shields.io/badge/Platform-Android-green "Android")
![license](https://img.shields.io/badge/Version-Beta-yellow "Version")
![license](https://img.shields.io/badge/Licence-Apache%202.0-blue.svg "Apache")

This is the Artifact README for the paper #203 "Mobile Access Bandwidth in Practice: Measurement, Analysis, and Implications". In this paper, we present Swiftest, an ultra-fast and ultra-light bandwidth testing service (BTS). 

### 0. Prerequisites

If you want to obtain an in-depth and comprehensive understanding of Swiftest's performance, we highly recommend that you meet the following requirements.

+ **Devices.** You should possess an Android mobile device with different accesses, including 4G(LTE), sub-6GHz 5G, and/or WiFi networks. The OS version should be Android 10.0 or newer.

+ **Geographic Location.** You should be located in the mainland of China and under sub-6GHz 5G coverage. This is because both our test servers and users who opt in for evaluation were all located in the mainland of China. Currently, we have not tested (and thus cannot guarantee) the performance of Swiftest (our proposed bandwidth testing services) outside the mainland of China or under mmWave 5G networks.

For the users who do not meet the above requirements, we will endeavor to facilitate their understanding of Swiftest as well.
In particular, we will provide them with a "standard device" placed in our lab located in Beijing, China, which they can remotely access (through Teamviewer) and further conduct online artifact evaluation upon. For more details, please refer to our [wiki](https://github.com/mobilebandwidth/Artifacts/wiki/Artifact-Evaluation-with-Standard-Device).

+ **Skills.** If the user would like to build Swiftest on their own from the source code, it would be helpful for them to have some basic knowledge of Android development. For example, it would be very helpful if the evaluators have a familiarity with Android Studio. This is not a necessity, since we will present the evaluators with a detailed tutorial on building and running Swiftest.

### 1. Getting Started with Swiftest

(1) Download the APK file from `Swiftest/release/swiftest.apk` to your mobile device, and click to install Swiftest.

(2) Grant the **precise location permission** to Swiftest. Note that this is only used for server selection process, and we will **NOT** record any of your personally identifiable information.

(3) When you see the GUI in the left figure, you are now ready for a bandwidth test!

<div align="center">
    <img src="https://raw.githubusercontent.com/mobilebandwidth/Artifacts/main/.github/images/ready.jpeg" width="200px">
    &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;
    <img src="https://raw.githubusercontent.com/mobilebandwidth/Artifacts/main/.github/images/finish.jpeg" width="200px">
</div>

(4) Click the `START` button, after a short period of time (usually only one or two seconds), Swiftest will output your test result, test duration, data usage, and network type as shown in the right figure above.

### 2. Artifact Claims

* **BTS-APP.** The beta version of BTS-APP used in our paper is no longer supported. Therefore, we re-implement the BTS-APP's basic testing logic and deploy BTS-APP together with Swiftest on the same test server pool for an apple-to-apple comparison. 

    Our current implementation of BTS-APP has been confirmed by BTS-APP's development team. However, due to commercial restriction, we only provide the installation package in `BTS-APP/release/BTS-APP.apk`. We encourage evaluators to use mainstream bandwidth testing apps like Speedtest to perform general validation as well before the evaluation.

* **Server Deployment.** As for the server pool, currently we deploy `10` servers in China. Each server has a 100-Mbps uplink bandwidth. Therefore, Swiftest can support up to 1,000~Mbps bandwidth. In addition, test servers are not deployed outside the mainland of China. 

    Therefore, ***if you are the evaluator outside the mainland of China, we recommend you access our standard device for evaluation (detailed in our [wiki](https://github.com/mobilebandwidth/Artifacts/wiki/Artifact-Evaluation-with-Standard-Device)).***

* **Reproducibility.** It should be noted that the exact ground truth of a user's network bandwidth in the wild is in fact hard to obtain, which also has been pointed out by some prior work, e.g., [FastBTS](https://www.usenix.org/system/files/nsdi21-yang-xinlei.pdf). Also, under some circumstances (e.g., high-speed rails) the ground-truth bandwidth could vary dramatically even within a short period of time (several seconds). Therefore, it is common that the bandwidth test results can vary among consecutive tests within just several minutes, even for Speedtest.

  Given the above, we suggest that:

  - the evaluators should perform bandwidth tests under relatively stable network conditions (without VPNs, with good signal strength and little cross-user network contention).
  - pay more attention to the **test duration** and  **data usage** during the evaluation phase, which are the key advantages of Swiftest compared with other BTSes.

* **Stability.** Swiftest is currently under its beta version for further improvement and bug fixing, and thus could be unstable during the evaluation process. 

  - If you meet any problems when using Swiftest (such as app not responding), please quit the app, wait a couple of seconds, and then reopen the app for bandwidth testing. 

  - Please do not start a new test immediately after the previous test; instead, wait one or two seconds as a cooldown in between to avoid mutual interference.

    If the above does not work, please feel free to contact us.


### 3. Artifact Evaluation

#### 3.1 Scope
We provide 1) our data and script to reproduce the figures in our paper, and 2) our implementations of Swiftest and BTS-APP (confirmed by BTS-APP's development team) for running bandwidth tests.

#### 3.2 Evaluating Swiftest

1. Install BTS-APP (using the installation package provided at  `BTS-APP/release/BTS-APP.apk`)  and Swiftest (either the the installation package provided at `Swiftest/release/swiftest.apk`) on your device.
2. Open BTS-APP, click `START` button, and then wait until the test results is printed on the screen.
3. Quit BTS-APP, wait 1-2 seconds (avoid mutual interference), open Swiftest, click `START` button, and wait until the test result is output.
4. Change the sequence of BTS-APP and Swiftest for another group of tests. Note that  conduct sequential (back-to-back) bandwidth tests, with 1-2 second cooldown in between to avoid mutual interference.
5. Compare the test duration, data usage, and test results of BTS-APP and Swiftest to see the performance.
6. You can also test your bandwidth using mainstream bandwidth testing apps like Speedtest to perform a general comparison. There could be a certain range of error in terms of test results provided by Speedtest, BTS-APP, and Swiftest due to the differences in server pools and bandwidth testing logic. **Note that the results of Speedtest should not be considered when formally evaluating the performance of Swiftest (only BTS-APP and Swiftest are compared in detail in Section 5.3).**

For evaluators who would like to use our standard device for evaluation, please refer to our [wiki](https://github.com/mobilebandwidth/Artifacts/wiki/Artifact-Evaluation-with-Standard-Device) for more details.

#### 3.3 Reproducing Data Plots

All the data plots are generated from [Origin](https://www.originlab.com/), a **Windows-based** **proprietary** computer program for interactive scientific graphing and data analysis. We recommend you to download installation package of Origin from our cloud disk (click [here](https://drive.google.com/file/d/1-kM04RlJmXzZhDmWq_ZFieEkKR-Zqq3n/view?usp=sharing)). You can also download it from the [offical websites](https://www.originlab.com/demodownload.aspx), but additional registration efforts are needed. Then you can install Origin on your own computer. Once the Origin is successfully installed, you will have a **3-day free trial for evaluation**. You can find the detailed installation instructions in our [wiki](https://github.com/mobilebandwidth/Artifacts/wiki/Installing-Origin-on-Windows).

**Tips.** If you do not have a Windows device or just would not like to bother to install Origin on your own device, we also recommend you to **remotely access our standard device (where Origin has been installed) and review the data plot online**. Please refer to our [wiki](https://github.com/mobilebandwidth/Artifacts/wiki/Artifact-Evaluation-with-Standard-Device) for more access details. All the data plots are placed in the `plots` folder on the desktop.

Once you have successfully installed Origin (or have logged in our standard device), you can move on to reproduce our data plots! All the scripts and data of the plots in our paper are placed [here](https://github.com/mobilebandwidth/Artifacts/tree/main/plots) (the same as those in the `plots` folder on our standard device). Basically, for each figure in the paper, we provide the plot file (in PDF form), its corresponding origin source file (in OPJU form) and raw data (in EXCEL form for evaluators' fast check). **Double click the OPJU files** with Origin, and you will see the interface as shown in the below figure. For each OPJU file, all you need to do is the following three steps:

1. Click the `Project Explorer`;
2. Click the `Book` to see the source data of the figure;
3. Click the `Graph` to see the corresponding figure.

<div align="center">
    <img src="https://raw.githubusercontent.com/mobilebandwidth/Artifacts/main/.github/images/origin.jpeg" width="800px">
</div>
If you have any difficulty using Origin, please do not hesitate to contact us and ***yangxinlei19971105@gmail.com*** or ***linhao-16@outlook.com***. We will try our best to help you reproduce our figures through Origin.






The following parts of README are not important to the running of Swiftest and result reproducing, feel free to skip them if you do not wish to build Swiftest from scratch.

### 4. Code Organization

The codebase of Swiftest is organized as follows.

```
Swiftest
    |----release
    |----src
          |---- client-side
          |---- server-side
              |---- test-server
              |---- master-server
```

+ `Swiftest/src/client-side` is an Android Studio project that can be built as the client of Swiftest， which is the critical component of Swiftest. Swiftest's main bandwidth testing logic is implemented in `Swiftest/src/client-side/app/src/main/java/com/example/swiftest/BandwidthTest.java`.
+ `Swiftest/server-side/test-server` currently includes a simplified version of test servers' transmission logic. We are still negotiating with BTS-APP's operational team to acquire the release permission of the entire transmission logic of test servers.
+ `Swiftest/server-side/master-server` contains the source code (in Go) and the executable binary of master-server, which is also a simplified implementation.

### 5. Building Swiftest 

#### 5.1 Environment & Dependencies

To build Swiftest, you need to have the following software environments and dependencies.

+ Android Studio (version 2021.2.1 or newer, we recommend you to download the latest version from the [official website](https://developer.android.com/studio))
+ Android SDK platform version 10.0 or latter (to support 5G technologies)
+ Android SDK Build-Tools' version newer than 21.0, our recommended version is 31.0

#### 5.2 Building Swiftest

+ Open the project of Swifest client located at `Artifacts/Swiftest/src/client-side` with Android Studio, and wait for all the dependencies being installed. If you have changed the SDK enviroments according to Section 2.1, don't forget to sync the project (you can manually sync the project by clicking `file` $\rightarrow$ `sync the project with gradle files`).
+ Click `Build`$\rightarrow$`Build Bundle(s)/APK(s)`$\rightarrow$`Build APK(s)` to generate the installation package of Swiftest.
+ If you build the APK successfully, you would see a line of output `''BUILD SUCCESSFUL in xxx s''` in the terminal. Click the `locate` button to quickly find out your generated APK file (see the figure below). 

<div align="center">
    <img src="https://raw.githubusercontent.com/mobilebandwidth/Artifacts/main/.github/images/build.jpeg" width="800px">
</div>



### 6. References

```
@inproceedings {xinlei2022bandwidth,
    author = {Xinlei Yang, Hao Lin, Zhenhua Li, Feng Qian, Xingyao Li, Zhiming He, Xudong Wu, Xianlong Wang, Yunhao Liu, Zhi Liao, Daqiang Hu and Tianyin Xu},
    title = {Mobile Access Bandwidth in Practice: Measurement, Analysis, and Implications},
    booktitle = {Proceedings of the ACM Special Interest Group on Data Communication (SIGCOMM)},
    year = {2022},
    publisher = {ACM}
  }
```



