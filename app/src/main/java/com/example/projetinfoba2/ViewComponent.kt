package com.example.projetinfoba2

import android.graphics.RectF

interface ViewComponent {
    val position : RectF
    var vitesseX : Float
    var vitesseY : Float
    fun updatePosition(){
        position.top += vitesseY
        position.bottom += vitesseY
        position.left += vitesseX
        position.right += vitesseX
    }

}