package com.example.projetinfoba2


import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF

abstract class Projectile {

    abstract val image: Bitmap

    abstract val position : RectF

    abstract val vitesse : Float

    abstract val degats : Int

    abstract var isOnScreen : Boolean

    abstract fun draw(canvas: Canvas)

    abstract fun updatePosition()
    
    abstract fun getCollision(
        obstacleList: MutableList<Obstacle>? = null,
        obstacleToRemove: MutableList<Obstacle>? = null,
        joueur: Joueur)
}