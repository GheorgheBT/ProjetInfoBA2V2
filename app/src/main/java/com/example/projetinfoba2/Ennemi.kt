package com.example.projetinfoba2

import android.content.Context
import android.graphics.*

class Ennemi(private val context: Context, val x: Float, val y: Float) {


    private var numeroImage = 0 // numero de l'image de l'oiseau que s'affiche
    private val listEnnemiImage = intArrayOf(R.drawable.frame_0, R.drawable.frame_1,R.drawable.frame_2,R.drawable.frame_3,R.drawable.frame_4,R.drawable.frame_5,R.drawable.frame_6,R.drawable.frame_7) // liste des images
    private var ennemiImage: Bitmap
    private var ennemiTaille = 200f //taille de l'affichage de l'oiseau
    var ennemiPosition = RectF(x, y, x + ennemiTaille, y + ennemiTaille) // position de l'oiseau encode dans un rectangle
    var ennemiVitesse = 5f
    var ennemiOnScreen = true
    init { // initialisation de la premiere image de l'oiseau
        val options = BitmapFactory.Options().apply { inScaled = true }
        ennemiImage = BitmapFactory.decodeResource(context.resources, listEnnemiImage[numeroImage], options)
    }

    fun draw(canvas: Canvas) {
        val paint = Paint()
        canvas.drawBitmap(ennemiImage,null, ennemiPosition, null)
        numeroImage = (numeroImage + 1) % listEnnemiImage.size // change to the next frame
        val options = BitmapFactory.Options().apply { inScaled = true }
        ennemiImage = BitmapFactory.decodeResource(context.resources, listEnnemiImage[numeroImage], options) // load the next image
    }

    fun updatePosition() :  Boolean {
        if (ennemiOnScreen){
        ennemiPosition.left += ennemiVitesse
        ennemiPosition.right += ennemiVitesse
        return true
        }
        else {return false}
    }
    fun isOnScreen(sreenRect : RectF){
        if(sreenRect.right<ennemiPosition.left){
            ennemiOnScreen = false
        }
    }
    fun isClicked(x: Float, y: Float): Boolean { // detect si on a cliqué sur l'oiseau et return true ou false
        return x >= ennemiPosition.left && x <= ennemiPosition.right && y >= ennemiPosition.top && y <= ennemiPosition.bottom
    }
}

