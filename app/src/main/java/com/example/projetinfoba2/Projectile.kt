package com.example.projetinfoba2


import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF

abstract class Projectile : ViewComponent  {

    abstract val image: Bitmap

    abstract val degats : Int

    abstract var isOnScreen : Boolean

    abstract override val position: RectF
    abstract fun draw(canvas: Canvas)

    abstract fun getCollision(
        obstacleList: MutableList<Obstacle>? = null,
        joueur: Joueur)
}