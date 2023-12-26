package com.mikko.todo.darkmode

import android.content.Context
import androidx.core.content.ContextCompat
import com.mikko.todo.R

class DarkTheme: MyAppTheme {
    override fun firstActivityBackgroundColor(context: Context) =
        ContextCompat.getColor(context, R.color.default_gray)

    override fun firstActivityTextColor(context: Context) =
        ContextCompat.getColor(context, R.color.white)

    override fun firstActivityIconColor(context: Context) =
        ContextCompat.getColor(context, R.color.white)

    override fun id() = 1
}