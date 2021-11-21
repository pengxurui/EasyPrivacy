package com.pengxr.easyprivacy.bit64.bean

/**
 * Information of each so file.
 *
 * Created by pengxr on 2021/9/10.
 */
class SoFile {

    /**
     * So File path.
     */
    String soPath = ""

    /**
     * So File name.
     */
    String soName = ""

    /**
     * Pom name.
     */
    String pomName = ""

    String toString() {
        StringBuilder builder = new StringBuilder()
        builder.append("[")
        builder.append(pomName)
        builder.append(":")
        builder.append(soName)
        builder.append("]")
        return builder.toString()
    }

    @Override
    int hashCode() {
        return 31 * pomName.hashCode() + soName.hashCode()
    }

    @Override
    boolean equals(Object other) {
        if (!(other instanceof SoFile)) {
            return false
        }
        return pomName == other.pomName && soName == other.soName
    }
}