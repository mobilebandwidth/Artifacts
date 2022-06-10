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

(3) When you see the following interface, you are now ready for a bandwidth test!

![](https://raw.githubusercontent.com/mobilebandwidth/Artifacts/main/.github/images/ready.jpeg)

(4) After a short period of time (usually only one or two seconds), Swiftest will output your test result, test duration, data usage, and network type as well as shown in the Figure below.

![](https://raw.githubusercontent.com/mobilebandwidth/Artifacts/main/.github/images/finish.jpeg)

### 2. Detailed Instructions 

#### 2.1 Environment & Dependencies

#### 2.2* Building Swiftest from Scratch


#### 2.3 Evaluation Methodology

#### 2.4 Result Reproducing


### 3. Artifact Claims

* **Scope.** Detail what we provide for evaluation.

* **Setup.** Detail how we implement and deploy Swiftest and BTS-APP for artifact evaluation.

* **Reproducibility.** Detail the potential factors that may affact the bandwidth testing results.

* **Stability.** Detail the drawbacks of current Swiftest.


### 4. Code Organization

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


