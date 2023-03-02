package com.project.diaryapp

import android.util.Patterns

fun String.validateEmail(): Boolean{
    return this.matches(Patterns.EMAIL_ADDRESS.toRegex())
}

fun String.validateAlphanumeric(): Boolean{
    return this.all { it.isLetterOrDigit() }
}