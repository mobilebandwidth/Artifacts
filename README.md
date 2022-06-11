# Artifacts 
![license](https://img.shields.io/badge/Platform-Android-green "Android")
![license](https://img.shields.io/badge/Version-Beta-yellow "Version")
![license](https://img.shields.io/badge/Licence-Apache%202.0-blue.svg "Apache")

This is the Artifact README for Swiftest, an ultra-fast and ultra-light bandwidth testing service (BTS).

### 0. Prerequisites

If you want to obtain an in-depth and comprehensive understanding about the performance of Swiftest, we highly recommend that you meet the following requirements.

+ **Devices.** You should possess an Android mobile device with different accesses including 4G(LTE), sub-6GHz 5G, and/or WiFi networks. The Android OS version should be 10.0 or latter.

+ **Geographic Location.** You should be located in the mainland of China and under sub-6GHz 5G coverage. This is because both our test servers and users who opt in for evaluation were all located in the mainland of China. Currently we have not tested (and thus cannot guarantee) the performance of Swiftest (our proposed bandwidth testing services) outside the mainland of China or under mmWave 5G networks.

For the users who do not meet the above requirements, we will endeavor to facilitate their understanding of Swiftest as well.
In particular, we will provide them with a "standard device" placed in our lab located in Beijing, China, which they can remotely access (through Teamviewer) and further conduct online artifact evaluation upon. Currently, this standard device is not equipped with a SIM card so that only WiFi access is available. Nevertheless, we will soon make the 4G/5G access available for evaluation.

+ **Skills.** If the user would like to build Swiftest on their own from the source code, it would be helpful for them to have some basic knowledge of Android development. For example, it would be very helpful if the evaluators have a familiarity with Android Studio. This is not a necessity as we will present the evaluators with a detailed tutorial on building and running Swiftest.

### 1. Getting Started with Swiftest

(1) Download the APK file from `Swiftest/installation_package/swiftest.apk` to your mobile device, and click to install Swiftest.

(2) Grant the **precise location permission** to Swiftest. Note that this is only used for server selection process, and we will **NOT** record any of your personally identifiable information.

(3) When you see the GUI depicted in the left figure, you are now ready for a bandwidth test!

<div align="center">
    <img src="https://raw.githubusercontent.com/mobilebandwidth/Artifacts/main/.github/images/ready.jpeg" width="200px">
    &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;
    <img src="https://raw.githubusercontent.com/mobilebandwidth/Artifacts/main/.github/images/finish.jpeg" width="200px">
</div>

(4) After a short period of time (usually only one or two seconds), Swiftest will output your test result, test duration, data usage, and network type as well as shown in the right figure above.

### 2.* Building Swiftest 

This part of README are not important for the running of Swiftest and result reproducing, feel free to skip them if you do not wish to build Swiftest from scratch.

#### 2.1 Environment & Dependencies

To build Swiftest, you need to have the following software environments and dependencies.

+ Android Studio (version 2021.2.1 or newer, we recommend you to download the latest version from the [official website](https://developer.android.com/studio))
+ Android SDK platform version 10.0 or latter (to support 5G technologies)
+ Android SDK Build-Tools' version newer than 21.0, our recommended version is 31.0

#### 2.2 Building Swiftest

+ Open the project of Swifest client located at `xxx/xxx/xxx/xxx` with Android Studio, and wait for all the dependencies being installed. If you have changed the SDK enviroments according to Section 2.1, don't forget to sync the project (you can manually sync the project by clicking `file` $\rightarrow$ `sync the project with gradle files`).
+ Click `Build`$\rightarrow$`Build Bundle(s)/APK(s)`$\rightarrow$`Build APK(s)` to generate the installation package of Swiftest.
+ If you build the APK successfully, you would see a line of output `''BUILD SUCCESSFUL in xxx s''` in the terminal. Click the `locate` button to quickly find out your generated APK file (see the figure below). 

<div align="center">
    <img src="https://raw.githubusercontent.com/mobilebandwidth/Artifacts/main/.github/images/build.jpeg" width="800px">
</div>

​    

### 3. Artifact Claims

* **Setup.** The beta version of BTS-APP used in our paper (released in Dec. 2021) is no longer supported. Therefore, we re-implement the BTS-APP's basic testing logic (according to Section 2 of our paper) by ourselves and deploy BTS-APP together with Swiftest on the same test server pool for an apple-to-apple comparison. Note that our current implementation of BTS-APP has been confirmed by BTS-APP's development team, while they do not want us to make our detailed implementations publicly available due to business issues. Therefore, we only provide the installation package in `BTS-APP/xxx/xxx`. We encourage evaluators to use mainstream bandwidth testing apps like Speedtest to perform general validation before the evaluation.

  As for the server pool, currently we deploy `xxx` servers in China, each server has an 100-Mbps uplink bandwidth. Note that this server pool is a bit smaller than that used in our paper due to the monetary cost and small user scale (probably only used by the evaluators and us). Therefore, Swiftest can support up to xxx~Mbps bandwidth, which can cover almost all (99\%) the test cases based on our previous experience. In addition, test servers are not deployed outside the mainland of China because currently we do not have a business plan outside the mainland of China. Therefore, if you are the evaluator outside the mainland of China, we recommend you to access our standard device for evaluation.

* **Reproducibility.** It should be highlighted that the exact ground truth of a user's network bandwidth in the wild is in fact hard to obtain, which also has been pointed out by some prior work, e.g., [FastBTS](https://www.usenix.org/system/files/nsdi21-yang-xinlei.pdf). Even though Swiftest is equipped with advanced testing logics such as data-driven bandwidth probing (introduced in Section 5.1 of our paper), the results can still be inevitably affected by factors like traffic shaping mechisms of routers and ISPs, load balancing and spectrum allocation strategies in base stations, and network resource contention across different end hosts. More importantly, under some circumstances (e.g., high mobility scenarios like high-speed rails and metro networks, and densely populated area like large shopping mall) the ground-truth bandwidth could vary dramatically even within a short period of time (several seconds). Therefore, it is common that the bandwidth test results can vary significantly among consecutive tests within just several minutes, even for Speedtest which deploys 16,000+ global test servers.

  Given the above, we recommend users to perform bandwidth tests under relatively stable network conditions (without VPNs, with a good signal strength and little cross-user network  contention). This is because only under these cases the test results are meaningful to users. Also, given the above, we recommend you to pay more attention to the test duration and  data usage during the evaluation phase, which are the key advantages of Swiftest compared with other BTSes.

* **Stability.** Swiftest is currently under its beta version for further improvement and bug fixing, and thus cloud be unstable during the evaluation process. If you meet any problems when using Swiftest (such as app not responding), please quit the app, wait a couple of seconds, and then reopen the app for bandwidth testing. If this does not work, please feel free to contact us.


### 4. Artifact Evaluation

#### 4.1 Scope
We provide 1) our data and script to reproduce the figures in our paper, and 2) our implementations of Swiftest and BTS-APP (confirmed by BTS-APP's development team) for running bandwidth tests.

#### 4.2 Evaluating Swiftest

1. Install BTS-APP (using the installation package provided in  `xxx` )  and Swiftest (either the the installation package provided in  `xxx` or the one you build on your own) on your device.
2. Open BTS-APP, click `START` button, wait until the test results is printed on the screen.
3. Quit BTS-APP, wait 1-2 seconds (avoid mutual interference), open Swiftest, click `START` button, wait until the test result is output.
4. Change the sequence of BTS-APP and Swiftest for another group of test. Note that  conduct sequential (back-to-back) bandwidth tests, with 1-2 second cooldown in between to avoid mutual interference.
5. Compare the test duration, data usage, and test results of BTS-APP and Swiftest to see the performance.
6. You can also test your bandwidth using mainstream bandwidth testing apps like Speedtest to perform general comparison. There could be a certain range of error in terms of test results provided by Speedtest, BTS-APP, and Swiftest due to the differences in server pools and bandwidth testing logics. 

#### 4.3 Reproducing Data Plots

Our data plots are all generated from [Origin](https://www.originlab.com/), a proprietary computer program for interactive scientific graphing and data analysis. The detailed tutorial can be found [here](https://www.youtube.com/watch?v=oIqqwovfFfU). Basically, in each origin source file ( files ended with ` .opju`), both the figure and the corresponding data are contained as shown in the below figure.


### 5. Code Organization

The codebase of Swiftest is organized as follows.

```
Swiftest
|---- client-side
|---- server-side
          |---- test-server
          |---- master-server
```

+ `Swiftest/client-side` is an Android Studio project that can be built as the client of Swiftest.
+ `Swiftest/server-side/test-server` currently includes a simplified version of test servers' transmission logic. We are still negotiating with BTS-APP's operational team to acquire the release permission of the entire transmission logic of test servers.
+ `Swiftest/server-side/master-server` contains the source code (in Go) and the executable binary of master-server.

