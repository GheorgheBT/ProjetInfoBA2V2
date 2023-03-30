package com.example.projetinfoba2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF

class Bouton(private val context: Context, private val x: Float, private val y: Float,val numeroBouton: Int) {

    private val listeBoutonImage = intArrayOf(R.drawable.bouton1,R.drawable.bouton2) // liste des images des balles
    private val boutonTaille = 200f
    private val boutonImage : Bitmap = BitmapFactory.decodeResource(context.resources, listeBoutonImage[numeroBouton])
    private val BoutonPosition = RectF(x, y, x + boutonTaille, y + boutonTaille) // encode la position de la balle dans un rectangle
    private var boutonOnScreen = true

    fun draw(canvas: Canvas) {
        if (boutonOnScreen) {//dessine la balle dans le rectangle defini plus haut
            canvas.drawBitmap(
                boutonImage,
                null,
                BoutonPosition,
                null
            )
        }

    }

    fun isClicked(x: Float, y: Float): Boolean { // detect si on a cliqué sur l'oiseau et return true ou false
        return x >= BoutonPosition.left && x <= BoutonPosition.right && y >= BoutonPosition.top && y <= BoutonPosition.bottom
    }

    fun Destroy(){
        boutonOnScreen = false
    }
}