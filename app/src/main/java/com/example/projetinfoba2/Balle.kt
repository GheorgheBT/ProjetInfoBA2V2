package com.example.projetinfoba2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF

class Balle(private val context: Context, private val x: Float, private val y: Float) {
    private val balleModele = intArrayOf(R.drawable.ball) // liste des images des balles
    private val balleImage: Bitmap =  BitmapFactory.decodeResource(context.resources,balleModele[0])// recupere l'image de la balle
    private val scaledBalleImage: Bitmap = Bitmap.createScaledBitmap(balleImage, 90, 45, true) // adapte les dimensions de la balle
    private val ballePosition = RectF(x, y, x + scaledBalleImage.width, y + scaledBalleImage.height) // encode la position de la balle dans un rectangle
    private val balleVitesse = 20f // la vitesse eb x à laquelle la balle va se deplacer
    private val balleDegats = 100
    private var balleOnScreen = true

    fun draw(canvas: Canvas) {
        if (balleOnScreen) {//dessine la balle dans le rectangle defini plus haut
            canvas.drawBitmap(
                scaledBalleImage,
                null,
                ballePosition,
                null
            )
        }

    }

    fun updatePosition() { // actualise la position de la balle selon sa vitesse
        if (balleOnScreen) {
            ballePosition.left += balleVitesse
            ballePosition.right += balleVitesse
        }
    }
     fun ballDestroyed(){
         balleOnScreen = false
     }
    
}