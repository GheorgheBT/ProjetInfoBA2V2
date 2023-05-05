package com.example.projetinfoba2

import android.graphics.RectF

interface Deplacement {
    //Rectangle délimitant l'objet
    var position : RectF
    //Vitesse de déplacement de l'objet
    var vitesseX : Float
    var vitesseY : Float

    fun updatePosition(){ //Fonction de déplacement
        position.top += vitesseY
        position.bottom += vitesseY
        position.left += vitesseX
        position.right += vitesseX
    }

}