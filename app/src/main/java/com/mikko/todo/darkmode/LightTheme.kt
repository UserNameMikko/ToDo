package com.mikko.todo.darkmode

import android.content.Context
import androidx.core.content.ContextCompat
import com.mikko.todo.R

class LightTheme: MyAppTheme {
    override fun firstActivityBackgroundColor(context: Context) =
        ContextCompat.getColor(context, R.color.white)

    override fun firstActivityTextColor(context: Context) =
        ContextCompat.getColor(context, R.color.black)
    override fun firstActivityIconColor(context: Context) =
        ContextCompat.getColor(context, R.color.default_gray)

    override fun id() = 0

}