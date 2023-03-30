package com.example.projetinfoba2

import android.content.Context
import android.graphics.*

class Bird(private val context: Context, val x: Float, val y: Float) {

    private var frameIndex = 0 // numero de l'image de l'oiseau que s'affiche
    private val frames = intArrayOf(R.drawable.frame_0, R.drawable.frame_1,R.drawable.frame_2,R.drawable.frame_3,R.drawable.frame_4,R.drawable.frame_5,R.drawable.frame_6,R.drawable.frame_7) // liste des images
    private var birdImage: Bitmap

    private var birdsize = 250f //taille de l'affichage de l'oiseau
    var birdposition = RectF(x, y, x + birdsize, y + birdsize) // position de l'oiseau encode dans un rectangle

    init { // initialisation de la premiere image de l'oiseau
        val options = BitmapFactory.Options().apply { inScaled = true }
        birdImage = BitmapFactory.decodeResource(context.resources, frames[frameIndex], options)
    }

    fun isClicked(x: Float, y: Float): Boolean { // detect si on a cliqué sur l'oiseau et return true ou false
        return x >= birdposition.left && x <= birdposition.right && y >= birdposition.top && y <= birdposition.bottom
    }

    fun updatePosition(x: Float, y: Float) {
        birdposition.offsetTo(x - birdsize / 2f, y - birdsize / 2f)
    }

    fun draw(canvas: Canvas) {
        val paint = Paint()
        canvas.drawBitmap(birdImage,null, birdposition, null)
        frameIndex = (frameIndex + 1) % frames.size // change to the next frame
        val options = BitmapFactory.Options().apply { inScaled = true }
        birdImage = BitmapFactory.decodeResource(context.resources, frames[frameIndex], options) // load the next image
    }
}

