package com.cypress.bbcnewsapplication.common

fun String.toFlagEmoji(): String {
    if (this.length != 2) return this
    val countryCodeCaps = this.uppercase()

    val firstChar = Character.codePointAt(countryCodeCaps, 0) - 0x41 + 0x1F1E6
    val secondChar = Character.codePointAt(countryCodeCaps, 1) - 0x41 + 0x1F1E6

    return String(Character.toChars(firstChar)) +
            String(Character.toChars(secondChar))
}