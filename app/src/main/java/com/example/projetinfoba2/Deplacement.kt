package com.example.projetinfoba2

import android.graphics.RectF

interface Deplacement {
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