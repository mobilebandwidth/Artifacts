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

<figure class="half" align="center">
    <img src="https://raw.githubusercontent.com/mobilebandwidth/Artifacts/main/.github/images/ready.jpeg" width="200px">
    &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;
  <img src="https://raw.githubusercontent.com/mobilebandwidth/Artifacts/main/.github/images/finish.jpeg" width="200px">

</figure>

(4) After a short period of time (usually only one or two seconds), Swiftest will output your test result, test duration, data usage, and network type as well as shown in the right figure above.

### 2.* Building Swiftest 

This part of README are not important for the running of Swiftest and result reproducing, feel free to skip them if you do not wish to build Swiftest from scratch.

#### 2.1 Environment & Dependencies

To build Swiftest, you need to have the following software environments and dependencies.

+ Android Studio (version 2021.2.1 or newer, we recommend you to download the latest version from the [official website](https://developer.android.com/studio))
+ Android OS version 10.0 or latter (to support 5G technologies)
+ Android SDK Build-Tools' version newer than 21.0, our recommended version is 31.0

#### 2.2 Building Swiftest

+ Open the project of Swifest client located at `xxx/xxx/xxx/xxx` with Android Studio, and wait for all the dependencies being installed. If you have changed the SDK enviroments according to Section 2.1, don't forget to sync the project (you can manually sync the project by clicking `file` $\rightarrow$ `sync the project with gradle files`).
+ Click `Build`$\rightarrow$`Build Bundle(s)/APK(s)`$\rightarrow$`Build APK(s)` to generate the installation package of Swiftest.
+ If you build the APK successfully, you would see a line of output `BUILD SUCCESSFUL in xxx s` in the terminal. Click the `locate` word to quickly find out the place of the generated APK file (see the figure below). 

<figure class="half" align="center">
    <img src="https://raw.githubusercontent.com/mobilebandwidth/Artifacts/main/.github/images/build.jpeg" width="400px">

​    

### 3. Artifact Claims

* **Setup.** Detail how we implement and deploy Swiftest and BTS-APP for artifact evaluation.

* **Reproducibility.** Detail the potential factors that may affact the bandwidth testing results.

* **Stability.** Detail the drawbacks of current Swiftest.


### 4. Artifact Evaluation

#### 4.1 Scope
Detail what we provide for evaluation.

#### 4.2 Evaluating Swiftest

#### 4.3 Reproducing Data Plots


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

