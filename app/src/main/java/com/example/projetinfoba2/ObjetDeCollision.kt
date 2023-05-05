package com.example.projetinfoba2

import android.graphics.RectF

interface ObjetDeCollision {
    //Condition d'affichage de l'objet à l'écran
    var isOnScreen : Boolean
    //Rectangle délimitant l'objet
    var position : RectF
}