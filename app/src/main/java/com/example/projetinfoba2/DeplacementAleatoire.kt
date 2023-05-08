package com.example.projetinfoba2

import android.graphics.RectF

interface DeplacementAleatoire {
    //Rectangle délimitant l'objet
    var position : RectF
    //Vitesse de déplacement de l'objet
    var vitesseX : Float
    var vitesseY : Float
    fun updatePosition(obstacleList: MutableList<Obstacle>, joueur: Joueur)
}