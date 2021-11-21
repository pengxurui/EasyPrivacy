package com.pengxr.easyprivacy.bit64

import com.pengxr.easyprivacy.bit64.bean.SoFile
import com.pengxr.easyprivacy.bit64.bean.SoFileList
import org.gradle.api.Project
import org.gradle.api.Task

/**
 * Support 64-bit abi.
 *
 * Created by pengxr on 2021/9/10.
 */
class Bit64Feature {

    void apply(Project project) {
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
            project.getTasks().create("support 64-bit abi") {
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
    }

    /**
     * Find so file recursively.
     */
    void findSoFile(File file, SoFileList soList) {
        if (null == file) {
            return
        }
        if (file.isDirectory()) {
            // recursively
            file.listFiles().each {
                findSoFile(it, soList)
            }
        } else if (file.absolutePath.endsWith(".so")) {
            println "EasyPrivacy => so: ${file.absolutePath}"

            SoFile so = generateSoInfo(file)

            if (so.soPath.contains("armeabi-v7a")) {
                soList.armeabiv_v7a.add(so)
            } else if (so.soPath.contains("armeabi")) {
                soList.armeabi.add(so)
            } else if (so.soPath.contains("arm64-v8a")) {
                soList.arm64_v8a.add(so)
            } else if (so.soPath.contains("x86_64")) {
                soList.x86_64.add(so)
            } else if (so.soPath.contains("x86")) {
                soList.x86.add(so)
            } else if (so.soPath.contains("mips64")) {
                soList.mips_64.add(so)
            } else if (so.soPath.contains("mips")) {
                soList.mips.add(so)
            }
        }
    }

    /**
     * Generate information for each so file.
     */
    private SoFile generateSoInfo(File file) {
        String filePath = file.absolutePath

        SoFile so = new SoFile()
        so.soPath = filePath
        so.soName = file.name

        if (filePath.contains("merged_jni_libs") || filePath.contains("library_jni")) {
            so.pomName = ""
        } else {
            String separator = File.separator
            if (System.properties['os.name'].toLowerCase().contains('windows')) {
                separator = "\\\\"
            }
            String[] dirPath = filePath.split(separator)
            so.pomName = dirPath[dirPath.length - 4]
        }
        return so
    }
}