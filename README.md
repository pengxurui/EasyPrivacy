# EasyPrivacy

[![](https://www.jitpack.io/v/pengxurui/EasyPrivacy.svg)](https://www.jitpack.io/#pengxurui/EasyPrivacy)

# 前言

- 最近，各大应用市场都在推动应用支持 64 位架构，你的 App 已经支持了吗？
- 在这篇文章里，我将带你完成 64 位架构的的适配工作。同时会带你建立关于 ABI 的基本认识，并为你带来我的 Gradle 插件 [EasyPrivacy](https://github.com/pengxurui/EasyPrivacy)，帮助你检测工程中的 64 位适配问题。如果能帮上忙，请务必点赞加关注，这真的对我非常重要。

---
# 目录

![](https://github.com/pengxurui/EasyPrivacy/blob/master/images/EasyPrivacy%20-%20%E7%9B%AE%E5%BD%95.png)

---
# 1. 概述

#### 1.1 CPU 和 ABI 的关系

CPU 架构是 CPU 厂商定义的 CPU 规范，目前主流的 CPU 架构分为两大类：

- **复杂指令集（CISC）：** 例如 Intel 和 AMD 的 X86 架构；
- **精简指令集（RISC）：** 例如 IBM 的 PowerPC 架构、ARM 的 ARM 架构。

应用二进制接口（Application Binary Interface, ABI）定义了机器代码和操作系统的交互，**与我们熟知 API 会以一个接口源码实体存在不同，ABI 更应该理解为一种规范。** ABI 包含信息详见 [Android ABI](https://developer.android.google.cn/ndk/guides/abis) —— 官方文档

#### 1.2 Android 支持 的 ABI

不同的 Android 设备使用不同的 CPU，不同 CPU 支持的 ABI 也不同。目前，Android 设备支持的 ABI 类型如下：

|ABI|描述|
|:---|:---|
|armeabi|第 5 代、第 6 代的ARM 处理器，基本退出历史舞台|
|armeabiv-v7a|第 7 代及以上的 ARM 处理器，正在逐步退出历史舞台|
|arm64-v8a|第 8 代、64 位 ARM 处理器，目前是主流|
|x86 / x86_64|一般是模拟器|

不同 CPU 支持的 ABI 情况如下：

||armeabi|armeabi-v7a|arm64-v8a|x86|x86_64|mips|mips64|
|:---|:---|:---|:---|:---|:---|:---|:---|
|ARMv5 /<br>ARMv6|✔|||||||
|ARMV7|✔|✔||||||
|ARMV8|✔|✔|✔|||||
|X86|✔|✔||✔||||
|X86-64|✔|（不支持）||✔|✔|||
|MIPS||||||✔||
|MIPS64||||||✔|✔|

> **提示：** 通过 Build.SUPPORTED_ABIS 可以得到设备支持的 ABI 列表，并且是按照偏好排序的。

#### 1.3 主要 ABI 和辅助 ABI

**每个 CPU 架构都有一个主要 ABI 和（可选的）兼容的辅助 ABI，64 位 CPU 可以兼容 32 位 ABI（例如 x86_64 兼容 x86，反过来不行）。** 需要注意的是：只有使用主要 ABI 才能获得最佳性能（例如 x86 兼容 armeabi ），这就是应用市场着手推动 64 位架构适配的根本原因。

---
# 2. 为 Android 设备适配 64 位架构

#### 2.1 64 位架构适配的时间节点

海外应用市场早在 19 年就在推进 64 位架构的适配，从 2019 年 8 月 1 日起，在 `Google Play` 上发布的应用就必须支持 64 位架构。至于国内应用市场，大致的时间节点如下（以 小米、VIVO、OPPO 为例）：

- 至 2021 年 12 月底，在应用市场发布的应用必须支持 64 位架构；
- 至 2022 年 8 月底，对于支持 64 位的硬件系统，将只接收 64 位版本的 APK；
- 至 2023 年 12 月底，硬件将仅支持 64 位 APK，

#### 2.2 Android 系统 ABI 管理

在安装应用时，PMS 服务将扫描 APK 文件，从中查找出 APK 中主要 ABI 类型的 so 文件：

```
lib/<primary-abi>/lib<name>.so
```

如果没有找到，则会去查找 APK 文件中辅助 ABI 类型的 so 文件：

```
lib/<secondary-abi>/lib<name>.so
```

完成查找后，PMS 会将它们复制到 app 目录下的 so 库路径（例如：`/data/app/[packagename]/lib/arm64`），并在应用运行时执行到 System.loadLibrary(...) 时加载到内存中。如果没有查找到匹配的 so 文件，不会中断安装过程，但在运行时会崩溃。

关于加载 so 文件的过程，我们在 [《说说 so 库从加载到卸载的全过程》](https://juejin.cn/post/6892793299427491854) 这篇文章里已经讨论过了。你可以回去看看，主要源码在：[DexPathList.java](http://androidxref.com/9.0.0_r3/xref/libcore/dalvik/src/main/java/dalvik/system/DexPathList.java)

![](https://github.com/pengxurui/EasyPrivacy/blob/master/images/EasyPrivacy%20-%20Match.png)

—— 图片引用自爱奇艺技术团队

可以看到，**适配 64 位架构到底是做什么呢？说到底就是为系统提供性能最高的主要 ABI so 文件。** 上层应用的重点就是提供 64 位的 so 文件，我们可以将需要做的事情拆解为三部分：

- **1、检索不支持 64 位 的 so 文件（EasyPrivacy 插件）**
- **2、构建 64 位 APK**
- **3、分发 64 位 APK**

---
# 3. EasyPrivacy 插件一键检索 so 文件

关于如何检索 APK 中不支持 64 位 的 so 文件，官方提供了两种方法，具体可参考 [官方文档](https://developer.android.google.cn/distribute/best-practices/develop/64-bit#apk-analyzer)：

- **1、通过 APK 分析器分析（直接将 APK 拖到 Android Studio 上）；**
- **2、解压缩 APK 并通过 `grep` 命令来分析。**

这两种方法基本可以满足要求，但操作上太费时间，也无法直接提示 so 文件是通过哪个组件来集成的 **（例如，push.aar 内部集成了 libc++_shared.so，通过 APK 知晓该 so 文件是来自 push.aar）**。为了快速检索到项目中不支持 64 位 的 so 文件，贴心的我已经帮你实现为一个 EasyPrivacy 插件。源码地址：https://github.com/pengxurui/EasyPrivacy

#### 3.1 添加依赖

- **1、依赖 EasyPrivacy 插件**

在项目级 build.gradle 中声明远程仓库，并依赖 EasyPrivacy 插件：

`项目级 build.gradle`
```
buildscript {
    repositories {
        ...
        google()
        mavenCentral()
        // JitPack 仓库
        maven { url "https://jitpack.io" }
    }
    dependencies {
        ...
        classpath 'com.github.pengxurui:EasyPrivacy:v1.0.5
    }
}
```

- **2、应用 EasyPrivacy 插件**

在应用级或者模块级 build.gradle 中应用 EasyPrivacy 插件：

`build.gradle`

```
apply plugin: 'com.pengxr.easyprivacy'
...
```

执行 Sync Gradle 之后，可以在 Gradle 面板中看到新增的检测任务，具体位于 `privacy` 任务组：

![](https://github.com/pengxurui/EasyPrivacy/blob/master/images/EasyPrivacy%20-%20Gradle.png)

#### 3.2 执行 support 64-bit abi

执行 support 64-bit abi 任务，将检索该模块的 Gradle 依赖树中的 so 文件，从中筛选出其中没有完成 64 位适配的 so 文件。例如， 项目中存在 `armeabiv-v7a` 类型的 `libc++_shared.so` 文件，但没有提供对应的 64 位`arm64-v8a` 类型，就会在分组`so in armeabiv-v7a, but not in arm64-v8a:`中增加提示。

#### 3.3 分析日志

以下是在 sample 模块的日志输出：

```
...
> Configure project :sample
...
> Task :sampleLib:copyReleaseJniLibsProjectOnly UP-TO-DATE
> Task :sample:mergeReleaseNativeLibs UP-TO-DATE
> Task :sample:support 64-bit abi
EasyPrivacy => Support 64-bit abi start.
EasyPrivacy => so: /Users/pengxurui/.gradle/caches/transforms-2/files-2.1/297c6c751393f4fc48ae19ba461e118a/openDefault-4.2.7/jni/armeabi-v7a/libweibosdkcore.so
EasyPrivacy => so: /Users/pengxurui/.gradle/caches/transforms-2/files-2.1/297c6c751393f4fc48ae19ba461e118a/openDefault-4.2.7/jni/x86/libweibosdkcore.so
EasyPrivacy => so: /Users/pengxurui/.gradle/caches/transforms-2/files-2.1/297c6c751393f4fc48ae19ba461e118a/openDefault-4.2.7/jni/armeabi/libweibosdkcore.so
EasyPrivacy => so: /Users/pengxurui/workspace/public/EasyPrivacy/sample/build/intermediates/merged_jni_libs/release/out/armeabi-v7a/libbsdiff.so
EasyPrivacy => so: /Users/pengxurui/workspace/public/EasyPrivacy/sample/build/intermediates/merged_jni_libs/release/out/x86/libbsdiff.so
EasyPrivacy => so: /Users/pengxurui/workspace/public/EasyPrivacy/sample/build/intermediates/merged_jni_libs/release/out/armeabi/libbsdiff.so
EasyPrivacy => so: /Users/pengxurui/workspace/public/EasyPrivacy/sampleLib/build/intermediates/library_jni/release/jni/armeabi-v7a/libgetuiext3.so
EasyPrivacy => so: /Users/pengxurui/workspace/public/EasyPrivacy/sampleLib/build/intermediates/library_jni/release/jni/armeabi-v7a/libc++_shared.so
EasyPrivacy => so: /Users/pengxurui/workspace/public/EasyPrivacy/sampleLib/build/intermediates/library_jni/release/jni/arm64-v8a/libgetuiext3.so

EasyPrivacy => armeabi size: 2
EasyPrivacy => armeabiv-v7a size: 4
EasyPrivacy => arm64-v8a size: 1
EasyPrivacy => x86 size: 2
EasyPrivacy => x86_64 size: 0
EasyPrivacy => mips size: 0
EasyPrivacy => mips_64 size: 0

so in armeabi, but not in arm64-v8a:
[openDefault-4.2.7:libweibosdkcore.so]	[:libbsdiff.so]	

so in armeabiv-v7a, but not in arm64-v8a:
[openDefault-4.2.7:libweibosdkcore.so]	[:libbsdiff.so]  [:libc++_shared.so]	

so in x86, but not in x86-64:
[openDefault-4.2.7:libweibosdkcore.so]	[:libbsdiff.so]	

so in mips, but not in mips-64:
```

从以上日志可以看出，`[openDefault-4.2.7:libweibosdkcore.so]`、`[:libbsdiff.so]`、`[:libc++_shared.so]` 这三个 so 文件没有提供 `arm64-v8a` 类型，这部分就是你需要做适配的内容。

其中 `openDefault-4.2.7` 是 so 文件所处的 aar 的 pom 信息，你可以根据这个信息来判断需要适配的 SDK。另外，像 `:libbsdiff.so` 这种则属于直接集成在工程中的 so 文件。

---
# 4. 构建 64 位 APK

完成适配工作后，现在需要构建出 64 位的 APK。根据应用市场的要求，你需要构建出三种包：

- **1、32 位包**
- **2、64 位包**
- **3、32 / 64 位包（同时包含 32 位 和 64 位两种 so 文件）**

#### 4.1 ndk.abiFilters 配置

通过 ndk. abiFilters 配置可以过滤出需要打包到 APK 中的 so 文件，例如以下配置将会把 `armeabi-v7a` 和 `arm64-v8a` 两种 ABI 类型的 so 文件打包到 APK 中：

`应用级 build.gradle`
```
android {
    ...
    defaultConfig {
        ...
        ndk {
            abiFilters "armeabi-v7a","arm64-v8a"
        }
    }
}
```

#### 4.2 splits 配置

ndk.abiFilters 配置可以将所有支持的 ABI 的 so 文件都打包进 APK，缺点是包体积增大。其实，应用市场是支持单独分发 32 位和 64 位 APK 包的能力的，我们可以使用 splits 配置。例如以下配置会将每种 ABI 类型单独打包。universalApk 为 ture 时还会额外构建一个包含所有 ABI 类型的 APK。

```
android {
    ...
    defaultConfig {
        ...
        splits {
            abi {
                enable true
                reset()
                include 'armeabi-v7a', 'arm64-v8a'
                universalApk false
            }
        }
    }
}
```

---
# 5. 总结

EasyPrivacy 框架的源码我已经放在 Github 上了，源码地址：https://github.com/pengxurui/EasyPrivacy。我也写了一个简单的 Sample Demo，你可以直接运行体验下。欢迎批评，欢迎 Issue~

最近几个月，你是否经常会收到应用市场的隐私整改邮件呢？是的，在中国用户隐私意识得到强化的同时，针对 App 的隐私规范整改也在一步步收紧，海外（Google Play/App Store）走过的老路，最终我们也得走一遍呀！

我们会发现隐私整改是每个 App 都无法规避的问题，具备共性。我想做一个专门针对隐私整改的 Gradle 插件 EasyPrivacy，帮助开发者快速发现工程中隐私问题。市面上目前有类似的工具吗，可以分享给我。或者你可以说说那些最让你头疼的整改问题（给我提 Feature！）

---
#### 参考资料
- [支持 64 位架构](https://developer.android.google.cn/distribute/best-practices/develop/64-bit) —— 官方文档
- [构建多个 APK](https://developer.android.google.cn/studio/build/configure-apk-splits?hl=zh_cn) —— 官方文档
- [Android ABI](https://developer.android.google.cn/ndk/guides/abis) —— 官方文档
- [爱奇艺 App 架构升级之路——64 位适配探索与实践](https://www.infoq.cn/article/8waKuU1WUVbG0t3D3jIm) —— 爱奇艺技术产品团队 著
- [Android 适配 64 位架构](https://juejin.cn/post/6964737926617890853?share_token=a53cfb30-57f6-4ae7-b033-c95c289890a6) —— callmepeanut 著

> **创作不易，你的「三连」是丑丑最大的动力，我们下次见！**
