package com.example.mymusicapp.helper

import com.example.mymusicapp.common.AppCommon

object StringHelper {
    fun convert(input: String): String {
        return input.replace(Regex("[àáạảãâầấậẩẫăằắặẳẵ]"), "a")
            .replace(Regex("[èéẹẻẽêềếệểễ]"), "e")
            .replace(Regex("[ìíịỉĩ]"), "i")
            .replace(Regex("[òóọỏõôồốộổỗơờớợởỡ]"), "o")
            .replace(Regex("[ùúụủũưừứựửữ]"), "u")
            .replace(Regex("[ỳýỵỷỹ]"), "y")
            .replace(Regex("đ"), "d")
    }

    fun getHash(key: String): Long {
        val newKey = convert(key)
        var value: Long = 0
        newKey.forEach {
            val asciiCode = it.code
            if (asciiCode <= 255)
                value = (value * AppCommon.BASE + asciiCode) % AppCommon.MOD
        }
        return value
    }
}