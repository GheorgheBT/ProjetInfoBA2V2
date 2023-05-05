package com.example.projetinfoba2

interface DeplacementAleatoire {
    fun updatePosition(obstacleList: MutableList<Obstacle>, joueur: Joueur)
}