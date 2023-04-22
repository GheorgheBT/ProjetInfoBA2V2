package com.example.projetinfoba2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF

class Ennemi(private val context: Context, val x: Float, val y: Float) : ViewComponent{

    private var numeroImage = 0 // numero de l'image de l'oiseau que s'affiche
    private val listImage = intArrayOf(R.drawable.frame_0, R.drawable.frame_1,R.drawable.frame_2,R.drawable.frame_3,R.drawable.frame_4,R.drawable.frame_5,R.drawable.frame_6,R.drawable.frame_7) // liste des images
    private var image: Bitmap
    private var taille = 200f //taille de l'affichage de l'oiseau
    var position = RectF(x, y, ((x + taille)/1.5).toFloat(), ((y + taille)/1.5).toFloat()) // position de l'oiseau encode dans un rectangle
    var vitesse = 5f
    var isOnScreen = true
    init { // initialisation de la premiere image de l'oiseau
        val options = BitmapFactory.Options().apply { inScaled = true }
        image = BitmapFactory.decodeResource(context.resources, listImage[numeroImage], options)
    }

    fun draw(canvas: Canvas) {

        canvas.drawBitmap(image,null, position, null)
        numeroImage = (numeroImage + 1) % listImage.size // change to the next frame
        val options = BitmapFactory.Options().apply { inScaled = true }
        image = BitmapFactory.decodeResource(context.resources, listImage[numeroImage], options) // load the next image
    }

    override fun updatePosition(){
        if (gameData.screenWidth < position.left) {
            isOnScreen = false
        }
        if (isOnScreen) {
            position.left += vitesse
            position.right += vitesse
        }
    }


}

