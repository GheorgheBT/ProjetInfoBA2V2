package com.example.projetinfoba2

import android.content.Context
import android.graphics.*

class Bird(private val context: Context, val x: Float, val y: Float) {


    private var numeroImage = 0 // numero de l'image de l'oiseau que s'affiche
    private val listOiseauImage = intArrayOf(R.drawable.frame_0, R.drawable.frame_1,R.drawable.frame_2,R.drawable.frame_3,R.drawable.frame_4,R.drawable.frame_5,R.drawable.frame_6,R.drawable.frame_7) // liste des images
    private var oiseauImage: Bitmap
    private var oiseauTaille = 200f //taille de l'affichage de l'oiseau
    var oiseauposition = RectF(x, y, x + oiseauTaille, y + oiseauTaille) // position de l'oiseau encode dans un rectangle
    var oiseauVitesse = 5f

    init { // initialisation de la premiere image de l'oiseau
        val options = BitmapFactory.Options().apply { inScaled = true }
        oiseauImage = BitmapFactory.decodeResource(context.resources, listOiseauImage[numeroImage], options)
    }

    fun draw(canvas: Canvas) {
        val paint = Paint()
        canvas.drawBitmap(oiseauImage,null, oiseauposition, null)
        numeroImage = (numeroImage + 1) % listOiseauImage.size // change to the next frame
        val options = BitmapFactory.Options().apply { inScaled = true }
        oiseauImage = BitmapFactory.decodeResource(context.resources, listOiseauImage[numeroImage], options) // load the next image
    }

    fun updatePosition() {
        oiseauposition.left += oiseauVitesse
        oiseauposition.right += oiseauVitesse
    }

    fun isClicked(x: Float, y: Float): Boolean { // detect si on a cliqué sur l'oiseau et return true ou false
        return x >= oiseauposition.left && x <= oiseauposition.right && y >= oiseauposition.top && y <= oiseauposition.bottom
    }

    //fun updatePosition(x: Float, y: Float) {
    //    oiseauposition.offsetTo(x - oiseauTaille / 2f, y - oiseauTaille / 2f)
    //}


}

