package com.example.projetinfoba2

import android.content.Context
import android.graphics.*


class Joueur(
    private val context: Context,
    private val x: Float,
    private val y: Float,
    private val joueurTaille: Float
) {


    var joueurImage = BitmapFactory.decodeResource(context.resources, R.drawable.pers_1, null)
    var joueurPosition = RectF(x, y, x + joueurTaille, y + joueurTaille) // position du joueur encodé dans un rectangle


    //Vie du joueur
    var joueurVie = 5

    // point du joueur
    val joueurPoint = 0

    //Vitesses du joueur
    private val vitesseMax = 20f
    private var vitesseX: Float = 0f
    private var vitesseY: Float = 0f


    fun draw(canvas: Canvas, paint: Paint, width: Float, height: Float) {
        canvas.drawBitmap(joueurImage, null, joueurPosition, null)
        paint.color = Color.BLACK
        paint.textSize = 50f
        canvas.drawText("Vies : $joueurVie", width, height, paint)
        canvas.drawText("Point: $joueurPoint", width, height - 100f, paint)
    }


    fun updatePosition() {
        joueurPosition.right += vitesseX
        joueurPosition.left += vitesseX
        joueurPosition.top += vitesseY
        joueurPosition.bottom += vitesseY
    }
    fun setSpeed(normeX : Float, normeY : Float){
        vitesseX = normeX * vitesseMax
        vitesseY = normeY * vitesseMax
    }
}