

package com.example.projetinfoba2

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager


class ScreenData {
    @Suppress("DEPRECATION") // Pour enlever l'avertissement "deprecation"
    companion object{
        // Dimensions de l'ecran
        var screenWidth : Float = 0f
        var screenHeight : Float = 0f
        var leftScreenSide = 0f
        var upScreenSide = 0f


        //Mise à jour des dimensions de l'ecran
        fun setScreenWidth(context: Context) : Float{
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = windowManager.defaultDisplay
            val displayMetrics = DisplayMetrics()
            display.getMetrics(displayMetrics)
            screenWidth = displayMetrics.widthPixels.toFloat()
            return screenWidth
        }
        fun setScreenHeight(context: Context) : Float{
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = windowManager.defaultDisplay
            val displayMetrics = DisplayMetrics()
            display.getMetrics(displayMetrics)
            screenHeight = displayMetrics.heightPixels.toFloat()
            return screenHeight
        }
    }

}