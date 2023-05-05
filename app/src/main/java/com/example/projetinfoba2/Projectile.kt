package com.example.projetinfoba2


import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF

abstract class Projectile : Deplacement  {

    abstract val image: Bitmap

    abstract var isOnScreen : Boolean

    abstract override var position: RectF
    abstract fun draw(canvas: Canvas)

}