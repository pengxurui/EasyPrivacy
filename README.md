![](https://github.com/pengxurui/AndroidFamily/blob/master/images/Android_Banner.png)

<p align='center'>
  <a>
    <img src="https://www.jitpack.io/v/pengxurui/EasyPrivacy.svg">
  </a>
  <a href="https://www.github.com/pengxurui" target="_blank">
    <img src="https://komarev.com/ghpvc/?username=pengxurui&style=flat&label=👁%20Views">
  </a>
</p>

<p align='center'>
  <a href="https://www.github.com/pengxurui" target="_blank">
    <img src="https://img.shields.io/badge/作者-@小彭-brightgreen.svg?style=flat&logo=GitHub">
  </a>
  <a href="https://github.com/pengxurui/Android-NoteBook/raw/master/images/搜一搜公众号.png" target="_blank">
    <img src="https://img.shields.io/badge/公众号-彭旭锐-brightgreen.svg?style=flat&logo=WeChat">
  </a>
  <a href="https://juejin.cn/user/1063982987230392" target="_blank">
    <img src="https://img.shields.io/badge/🔥%20juejin-掘金-blue.svg?style=flat">
  </a>
  <a href="https://www.zhihu.com/people/pengxurui" target="_blank">
    <img src="https://img.shields.io/badge/zhihu-知乎-informational.svg?style=flat&logo=Zhihu">
  </a>
  <a href="https://www.toutiao.com/c/user/token/MS4wLjABAAAAbY-k9r66YVymjlqMaaGZJO67hCNYaNGa7PCoisQYmR4" target="_blank">
    <img src="https://img.shields.io/badge/toutiao-头条-red.svg?style=flat">
  </a>
  <a href="https://www.cnblogs.com/pengxurui/" target="_blank">
    <img src="https://img.shields.io/badge/cnblogs-博客园-green.svg?style=flat">
  </a>
  <a href="https://blog.csdn.net/pengxurui?spm=1000.2115.3001.5343" target="_blank">
    <img src="https://img.shields.io/badge/csdn-CSDN-9cf.svg?style=flat">
  </a>
  <a href="" target="_blank">
    <img src="https://img.shields.io/badge/jianshu-简书-orange.svg?style=flat">
  </a>
</p>

## EasyPrivacy 能帮到你什么？

最近几个月，你是否经常会收到应用市场的隐私整改邮件呢？是的，在中国用户隐私意识得到强化的同时，针对 App 的隐私规范整改也在一步步收紧，海外（Google Play / App Store）走过的老路，最终我们也得走一遍呀！

我们会发现隐私整改是每个 App 都无法规避的问题，具备共性。我想做一个专门针对隐私整改的 Gradle 插件 EasyPrivacy，帮助开发者快速发现工程中隐私问题。市面上目前有类似的工具吗，可以分享给我。或者你可以说说那些最让你头疼的整改问题（给我提 Feature！）

## 使用方法

**1、先给一个 Star：** 你的支持对我非常重要，我的内容质量绝对对得起你的 Star，给我一点创作的动力，感谢。

**2、进小彭的 Android 交流群：** 加我微信进群，我们对群质量有要求，你可以在这里找到志同道合的朋友。群里可以讨论技术、分享文章、聊天、吐槽，允许适当发招聘广告，不受欢迎的行为是严格禁止的：

**3、关注我的公众号 [彭旭锐]：** 坚持高质量原创内容，不人云亦云，公众号后续是我主要的内容更新平台：

**4、关注我的 [掘金](https://juejin.cn/user/1063982987230392)、[知乎](https://www.zhihu.com/people/pengxurui) 和 [《AndroidFamily》](https://github.com/pengxurui/AndroidFamily) 专栏：** 掘金上有我历史发布过的所有文章，AndroidFamily 专栏是我参考杜威十进制模型搭建的 Android 成长学习路线，你可以参考我的模型定制专属的知识体系。

**5、添加依赖：**

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

## 功能概览

|Gradle Task|描述|
|:---|:---|
|support64BitAbi|一键检测工程中的 64 位适配问题|

## 详细说明

### 1. support64BitAbi

**1.1 为什么要使用 support64BitAbi？**

海外应用市场早在 19 年就在推进 64 位架构的适配，从 2019 年 8 月 1 日起，在 `Google Play` 上发布的应用就必须支持 64 位架构。至于国内应用市场，从 2021 年开始，各大应用市场都在推动应用支持 64 位架构大致的时间节点如下（以 小米、VIVO、OPPO 为例）：

- 至 2021 年 12 月底，在应用市场发布的应用必须支持 64 位架构；
- 至 2022 年 8 月底，对于支持 64 位的硬件系统，将只接收 64 位版本的 APK；
- 至 2023 年 12 月底，硬件将仅支持 64 位 APK，

而使用 EasyPrivacy 插件提供的 `support64BitAbi` Task，可以一键检测工程中存在的 64 位适配问题。更多内容见 [《AndroidFamily 专栏》](https://github.com/pengxurui/AndroidFamily) 文章 [《一键检索未适配 64 位架构的 so 文件》](https://juejin.cn/post/7034201332500135966) 。

**1.2 如何使用 support64BitAbi？**

进入 Gradle 面板，执行 `support64BitAbi` Task。该任务将检索该模块的 Gradle 依赖树中的 so 文件，从中筛选出其中没有完成 64 位适配的 so 文件。例如，项目中存在 `armeabiv-v7a` 类型的 `libc++_shared.so` 文件，但没有提供对应的 64 位 `arm64-v8a` 类型，就会在分组`so in armeabiv-v7a, but not in arm64-v8a:`中增加提示。

`sample 模块日志`
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

**1.3 support64BitAbi 的优势是什么?**

关于检索 APK 中未适配 64 位架构的 so 文件问题，官方提供了 2 种方案，具体可参考 [官方文档](https://developer.android.google.cn/distribute/best-practices/develop/64-bit#apk-analyzer)：

- **1、通过 APK 分析器分析（直接将 APK 拖到 Android Studio 上）；**
- **2、解压缩 APK 并通过 `grep` 命令来分析。**

这 2 种方法基本可以满足要求，但操作上太费时间，也无法直接提示 so 文件是通过哪个组件来集成的 **（例如，push.aar 内部集成了 libc++\_shared.so，虽然可以知道 libc++\_shared.so 是不支持 64 位架构的，但无法知晓该 so 文件是来自 push.aar）**。

**1.4 support64BitAbi 的原理**

新建一个名为 support64BitAbi 的 Gradle Task，并 Hook 到 Android Gradle Plugin 中用于合并 Native 代码的 Task `merge[BuildVariants]NativeLibs` 之后。从该 Task 的输入文件（即 so 文件）的路径中识别出文件的 ABI 类型和模块名，最终汇总的出分析报告。

`核心源码 Bit64Feature.java`
```
project.afterEvaluate {
    // 1. Find the task merge[BuildVariants]NativeLibs
    Task mergeNativeTask = null
    for (Task task : project.getTasks()) {
        if (task.name.startsWith("merge") && task.name.endsWith("NativeLibs")) {
            mergeNativeTask = task
        }
    }
    if (null == mergeNativeTask) {
        return
    }

    // 2. Create detect task.
    project.getTasks().create("support64BitAbi") {
        group "privacy"
        dependsOn mergeNativeTask

        doFirst {
            println "EasyPrivacy => Support 64-bit abi start."

            SoFileList soList = new SoFileList()
            // 2.1 Find so file recursively.
            mergeNativeTask.inputs.files.each { file ->
                findSoFile(file, soList)
            }
            // 2.2 Print 64-bit abi supported status.
            soList.printlnResult()
        }
    }
}
```

**1.5 注意事项**

完成适配工作后，现在需要构建出 64 位的 APK。根据应用市场的要求，你需要构建出 3 种包：

**1、32 位包**

**2、64 位包**

**3、32 / 64 位包（同时包含 32 位 和 64 位两种 so 文件）**

以下提供 2 种构建 64 位 APK 的 AGP 配置方法：

- **方法 1 - ndk.abiFilters 配置：** 通过 ndk. abiFilters 配置可以过滤出需要打包到 APK 中的 so 文件，例如以下配置将会把 `armeabi-v7a` 和 `arm64-v8a` 两种 ABI 类型的 so 文件打包到 APK 中：

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

- **方法 1 - splits 配置：** ndk.abiFilters 配置可以将所有支持的 ABI 的 so 文件都打包进 APK，缺点是包体积增大。其实，应用市场是支持单独分发 32 位和 64 位 APK 包的能力的，我们可以使用 splits 配置。例如以下配置会将每种 ABI 类型单独打包。universalApk 为 ture 时还会额外构建一个包含所有 ABI 类型的 APK。

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

## 共同成长

- 欢迎提 Issue 帮助修复缺陷；
- 欢迎提 Pull Request 增加新的 Feature，让 EasyPrivacy 变得更加强大，你的 ID 会出现在 Contributors 中；
- 欢迎加 [作者微信](https://github.com/pengxurui/AndroidFamily/blob/master/images/%E4%B8%AA%E4%BA%BA%E5%BE%AE%E4%BF%A1.jpeg) 与作者交流，欢迎加入交流群找到志同道合的伙伴。

## 小彭的其它开源项目

- [AndroidFamily](https://github.com/pengxurui/AndroidFamily) 【Android 面经 + Android 学习指南】一份面向 Android 开发者的成长和进阶的学习路线；🔥
- [AndroidPlatforms](https://github.com/pengxurui/AndroidPlatforms) 每个 Android 开发都要收藏的系统适配手册，带你全面体系化地解读 Android 系统更新；🔥
- [EasyTrack](https://github.com/pengxurui/EasyTrack) 基于西瓜视频前端视图树埋点方案实现的埋点方案；
- [LeetCode-Kotlin](https://github.com/pengxurui/LeetCode-Kotlin) LeetCode 高频题解 - Kotlin 版本。

更多内容，请 [点击](https://juejin.cn/user/1063982987230392)

## Donate

如果本仓库对你有帮助，可以请小彭喝杯速溶咖啡。

![](https://github.com/pengxurui/AndroidFamily/blob/master/images/%E8%AF%B7%E5%B0%8F%E5%BD%AD%E5%96%9D%E6%9D%AF%E9%80%9F%E6%BA%B6%E5%92%96%E5%95%A1.png)

## License

Copyright [2022] [Peng Xurui]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
