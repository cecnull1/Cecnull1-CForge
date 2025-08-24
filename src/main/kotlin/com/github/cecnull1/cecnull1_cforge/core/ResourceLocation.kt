package com.github.cecnull1.cecnull1_cforge.core

import kotlin.text.isEmpty
import kotlin.text.split

data class ResourceLocation(val modId: String, val path: String) {
    /**
     * 判断 [ResourceLocation] 是否为空。
     */
    fun isEmpty(): Boolean {
        return path.isEmpty()
    }

    /**
     * 将 [ResourceLocation] 转换成字符串。
     * 格式为 "modId:path"
     */
    override fun toString(): String {
        return "$modId:$path"
    }

    override fun hashCode(): Int {
        return 31 * modId.hashCode() + path.hashCode()
    }

    companion object {
        /**
         * 將字符串转换成 [ResourceLocation]，
         * 当转化成 [ResourceLocation] 失败时抛出 [IllegalArgumentException]。
         *
         * 不推荐向字符串参数传递非数字、非小写字母、非下划线分隔的字符串，也不传递空命名空间和空路径的字符串。
         *
         * 尽管函数会尝试转化成 [ResourceLocation]。
         *
         * @param string 字符串
         * @return [ResourceLocation]
         * @throws IllegalArgumentException 如果字符串格式错误
         * */
        fun fromString(string: String): ResourceLocation {
            return fromStringOrNull(string) ?: throw kotlin.IllegalArgumentException("Invalid ResourceLocation string: $string")
        }

        /**
         * 將字符串转换成 [ResourceLocation]，
         * 当转化成 [ResourceLocation] 失败时返回 null。
         *
         * 不推荐向字符串参数传递非数字、非小写字母、非下划线分隔的字符串，也不传递空命名空间和空路径的字符串。
         *
         * 尽管函数会尝试转化成 [ResourceLocation]
         *
         * @param string 字符串
         * @return [ResourceLocation]?
         * */
        fun fromStringOrNull(string: String): ResourceLocation? {
            val split = string.split(":", limit = 2)
            if (split.size != 2) return null
            return ResourceLocation(split[0], split[1])
        }

        /**
         * 将字符串转换成 [ResourceLocation]，
         * 如果转换失败，则返回一个空的 [ResourceLocation]（可通过 [isEmpty] 判断）。
         *
         * 不推荐向字符串参数传递非数字、非小写字母、非下划线分隔的字符串，也不传递空命名空间和空路径的字符串。
         *
         * 尽管函数会尝试转化成 [ResourceLocation]。
         *
         * @param string 字符串
         * @return [ResourceLocation]
         */
        fun fromStringSafe(string: String): ResourceLocation {
            return fromStringOrNull(string) ?: ResourceLocation("", "")
        }
    }
}
