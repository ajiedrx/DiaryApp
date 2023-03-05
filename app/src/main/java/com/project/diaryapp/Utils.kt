package com.project.diaryapp

import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private fun Snackbar.allowInfiniteLines(): Snackbar {
    return apply { (view.findViewById<View?>(com.google.android.material.R.id.snackbar_text) as? TextView?)?.isSingleLine = false }
}

fun View.showSnackbar(text: String){
    Snackbar.make(this, text, Snackbar.LENGTH_LONG).allowInfiniteLines().show()
}

fun String.changeDateFormat(inputFormat: String = "yyyy-MM-dd'T'HH:mm:ss", outputFormat: String = "dd MMMM yyy"): String{
    val inFormat = DateTimeFormatter.ofPattern(inputFormat)
    val parsedTime = LocalDate.parse(this.removeRange(this.length - 8, this.length), inFormat)
    return DateTimeFormatter.ofPattern(outputFormat).format(parsedTime)
}