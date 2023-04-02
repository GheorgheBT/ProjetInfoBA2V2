package com.example.projetinfoba2

import android.content.Context
import android.graphics.*
import android.os.SystemClock
import android.util.Log


class Joueur(private val context: Context, private val x: Float, private val y: Float) {

    private var numeroImage = 0 // numero de l'image du joueur
    private val listJoueurImage = intArrayOf(R.drawable.pers_1, R.drawable.pers_2,R.drawable.pers_3,R.drawable.pers_4,R.drawable.pers_4,R.drawable.pers_5) // liste des images
    private var joueurImage: Bitmap
    private val joueurTaille = 200f //taille de l'affichage du joueur
    var joueurPosition = RectF(x, y, x + joueurTaille, y + joueurTaille) // position du joueur encodé dans un rectangle
    private val joueurGravite = 5f

    //Vie du joueur
    private var joueurVie = 5

    //Vitesses du joueur
    private val vitesseMax = 20f
    private var vitesseX : Float = 0f
    private var vitesseY : Float = 0f

    init { // initialisation de la premiere image du joueur
        val options = BitmapFactory.Options().apply { inScaled = true }
        joueurImage = BitmapFactory.decodeResource(context.resources, listJoueurImage[numeroImage], options)
    }

    fun draw(canvas: Canvas) {
        val paint = Paint()
        canvas.drawBitmap(joueurImage,null, joueurPosition, null)
        numeroImage = (numeroImage + 1) % listJoueurImage.size // change to the next frame
        val options = BitmapFactory.Options().apply { inScaled = true }
        joueurImage = BitmapFactory.decodeResource(context.resources, listJoueurImage[numeroImage], options) // load the next image
    }

    fun updatePosition(joystick: Joystick) {
        vitesseX = joystick.getDeltaPosJoystick()[0] / joystick.cercleExtRayon * vitesseMax// On prend le delta (x et y) du joystick, qu'on normalise (entre 0 et 1)
        vitesseY = joystick.getDeltaPosJoystick()[1] / joystick.cercleExtRayon * vitesseMax

        joueurPosition.right += vitesseX
        joueurPosition.left += vitesseX
        joueurPosition.top += vitesseY
        joueurPosition.bottom += vitesseY
    }


    fun isTouched(x: Float, y: Float): Boolean { // detect si on a cliqué sur
        return x >= joueurPosition.left && x <= joueurPosition.right && y >= joueurPosition.top && y <= joueurPosition.bottom
    }


    fun gravite(){
        if (joueurPosition.bottom <= 1150f){
            joueurPosition.top+=joueurGravite
            joueurPosition.bottom+=joueurGravite
        }
    }
}