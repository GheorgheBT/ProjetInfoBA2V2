package com.example.projetinfoba2

import android.content.Context
import android.graphics.*


class Joueur(private val context: Context, private val x: Float, private val y: Float) {

    private var numeroImage = 0 // numero de l'image du joueur
    private val listJoueurImage = intArrayOf(R.drawable.pers_1, R.drawable.pers_2,R.drawable.pers_3,R.drawable.pers_4,R.drawable.pers_4,R.drawable.pers_5) // liste des images
    private var joueurImage: Bitmap
    private val joueurTaille = 200f //taille de l'affichage du joueur
    var joueurPosition = RectF(x, y, x + joueurTaille, y + joueurTaille) // position du joueur encodé dans un rectangle
    private val joueurVitesse = 10f
    private var joueurVie = 5

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

    fun updatePosition(x: Float, y: Float) {
        joueurPosition.offsetTo(x - joueurTaille / 2f, y - joueurTaille / 2f)
    }


    fun isTouched(x: Float, y: Float): Boolean { // detect si on a cliqué sur
        return x >= joueurPosition.left && x <= joueurPosition.right && y >= joueurPosition.top && y <= joueurPosition.bottom
    }
}