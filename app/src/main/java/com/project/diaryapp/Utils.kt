package com.project.diaryapp

import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

private fun Snackbar.allowInfiniteLines(): Snackbar {
    return apply { (view.findViewById<View?>(com.google.android.material.R.id.snackbar_text) as? TextView?)?.isSingleLine = false }
}

fun View.showSnackbar(text: String){
    Snackbar.make(this, text, Snackbar.LENGTH_LONG).allowInfiniteLines().show()
}