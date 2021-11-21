package com.pengxr.easyprivacy.bit64.bean

import javax.annotation.Nonnull

/**
 * Collect all so file in current project.
 *
 * Created by pengxr on 2021/9/10.
 */
class SoFileList {

    List<SoFile> armeabi = []
    List<SoFile> armeabiv_v7a = []
    List<SoFile> arm64_v8a = []
    List<SoFile> x86 = []
    List<SoFile> x86_64 = []
    List<SoFile> mips = []
    List<SoFile> mips_64 = []

    /**
     * Print 64-bit abi supported status of the current project.
     */
    void printlnResult() {
        println("EasyPrivacy => armeabi size: ${armeabi.size()}")
        // printSoFileList(armeabi)

        println("EasyPrivacy => armeabiv-v7a size: ${armeabiv_v7a.size()}")
        // printSoFileList(armeabiv_v7a)

        println("EasyPrivacy => arm64-v8a size: ${arm64_v8a.size()}")
        // printSoFileList(arm64_v8a)

        println("EasyPrivacy => x86 size: ${x86.size()}")
        // printSoFileList(x86)

        println("EasyPrivacy => x86_64 size: ${x86_64.size()}")
        // printSoFileList(x86_64)

        println("EasyPrivacy => mips size: ${mips.size()}")
        // printSoFileList(mips)

        println("EasyPrivacy => mips_64 size: ${mips_64.size()}")
        // printSoFileList(mips_64)

        println("so in armeabi, but not in arm64-v8a:")
        diffSoFileList(armeabi, arm64_v8a)

        println("so in armeabiv-v7a, but not in arm64-v8a:")
        diffSoFileList(armeabiv_v7a, arm64_v8a)

        println("so in x86, but not in x86-64:")
        diffSoFileList(x86, x86_64)

        println("so in mips, but not in mips-64:")
        diffSoFileList(mips, mips_64)
    }

    /**
     * Print every so file's information.
     */
    private void printSoFileList(List<SoFile> soList) {
        if (!soList.isEmpty()) {
            int count = 0
            for (SoFile so : soList) {
                if (++count >= 3) {
                    println()
                    count = 0
                }
                printf("%s\t", so.toString())
            }
            println()
        }
    }

    /**
     * Print every so file's information in so32List but not in so64List.
     *
     * @param so32List 32-bit so file list.
     * @param so64List 64-bit so file list.
     */
    private void diffSoFileList(@Nonnull List<SoFile> so32List, @Nonnull List<SoFile> so64List) {
        if (so32List.isEmpty()) {
            printf("\n")
            return
        }
        Set<SoFile> so64Set = new HashSet<>()
        for (SoFile soFile : so64List) {
            so64Set.add(soFile)
        }
        int count = 0
        so32List.each { so32 ->
            if (!so64Set.contains(so32)) {
                if (++count >= 3) {
                    println()
                    count = 0
                }
                printf("%s\t", so32.toString())
            }
        }
        printf("\n\n")
    }
}