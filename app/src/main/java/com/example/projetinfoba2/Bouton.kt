package com.example.projetinfoba2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF

class Bouton(
    private val context: Context,
    private val x: Float,
    private val y: Float,
    val boutonTaille: Float
) {

    private val boutonImage: Bitmap =
        BitmapFactory.decodeResource(context.resources, R.drawable.bouton2)
    private val BoutonPosition = RectF(
        x,
        y,
        x + boutonTaille,
        y + boutonTaille
    ) // encode la position de la balle dans un rectangle


    fun draw(canvas: Canvas) {
        canvas.drawBitmap(
            boutonImage, null, BoutonPosition, null
        )
    }

    fun isClicked(x: Float, y: Float): Boolean { // detect si on a cliqué
        return x >= BoutonPosition.left && x <= BoutonPosition.right && y >= BoutonPosition.top && y <= BoutonPosition.bottom
    }
}