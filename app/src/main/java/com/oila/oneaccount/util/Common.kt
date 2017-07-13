package com.oila.oneaccount.util

import android.app.Application
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * ---------------------------------------------------
 * Created by Sermilion on 13/07/2017.
 * Project: OneAccountKotlin
 * ---------------------------------------------------
 * <a href="http://www.ucomplex.org">www.ucomplex.org</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */
fun hideKeyboard(v: View, app: Application) {
    val imm = app.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(v.windowToken, 0)
}

fun showKeyboard(v: View, app: Application) {
    val imm = app.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
}