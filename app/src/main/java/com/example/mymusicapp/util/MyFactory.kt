package com.example.mymusicapp.util

object MyFactory {
    fun convert(input: String): String {
        return input.replace(Regex("[àáạảãâầấậẩẫăằắặẳẵ]"), "a")
            .replace(Regex("[èéẹẻẽêềếệểễ]"), "e")
            .replace(Regex("[ìíịỉĩ]"), "i")
            .replace(Regex("[òóọỏõôồốộổỗơờớợởỡ]"), "o")
            .replace(Regex("[ùúụủũưừứựửữ]"), "u")
            .replace(Regex("[ỳýỵỷỹ]"), "y")
            .replace(Regex("đ"), "d")
    }
}