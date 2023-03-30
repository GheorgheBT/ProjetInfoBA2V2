package com.example.projetinfoba2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF

class Balle(private val context: Context, private val x: Float, private val y: Float) {

    private val balleImage: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ball) // recupere l'image de la balle
    private val scaledBalleImage: Bitmap = Bitmap.createScaledBitmap(balleImage, 90, 45, true) // adapte les dimension de la balle
    private val ballePosition = RectF(x, y, x + scaledBalleImage.width, y + scaledBalleImage.height) // encode la position de la balle dans un rectangle
    private val balleVitesse = 20f // la vitesse eb x à laquelle la balle va se deplacer

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(scaledBalleImage, null, ballePosition, null) //dessine la balle dans le rectangle defini plus haut
    }

    fun updatePosition() { // actualise la position de la balle selon sa vitesse 
        ballePosition.left += balleVitesse
        ballePosition.right += balleVitesse
    }
}