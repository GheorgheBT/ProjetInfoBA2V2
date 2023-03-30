package com.example.projetinfoba2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF

class Bullet(private val context: Context, private val x: Float, private val y: Float) {

    private val bulletImage: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ball) // recupere l'image de la balle
    private val scaledBulletImage: Bitmap = Bitmap.createScaledBitmap(bulletImage, 50, 25, true) // adapte les dimension de la balle
    private val bulletposition = RectF(x, y, x + scaledBulletImage.width, y + scaledBulletImage.height) // encode la position de la balle dans un rectangle
    private val ballSpeed = 20f // la vitesse eb x à laquelle la balle va se deplacer

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(scaledBulletImage, null, bulletposition, null) //dessine la balle dans le rectangle defini plus haut
    }

    fun updatePosition() { // actualise la position de la balle selon sa vitesse 
        bulletposition.left += ballSpeed
        bulletposition.right += ballSpeed
    }
}