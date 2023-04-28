package com.example.projetinfoba2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF

class Ennemi(private val context: Context,  x: Float, y: Float) : ViewComponent{

    //numero de l'image de l'oiseau qui s'affiche
    private var numeroImage = 0

    //liste des images
    private val listImage = intArrayOf(R.drawable.frame_0, R.drawable.frame_1,R.drawable.frame_2,R.drawable.frame_3,R.drawable.frame_4,R.drawable.frame_5,R.drawable.frame_6,R.drawable.frame_7) // liste des images

    //Aspect de l'ennemi
    private var image: Bitmap

    //taille de l'affichage de l'oiseau
    private var taille = 150

    //position de l'ennemi
    override var position = RectF(x, y, (x + taille), (y + taille)) // position de l'oiseau encode dans un rectangle

    //Vitesses de l'ennemi
    override var vitesseX = 5f
    override var vitesseY = 0f

    private var isOnScreen = true

    init {
        val options = BitmapFactory.Options().apply { inScaled = true }
        image = BitmapFactory.decodeResource(context.resources, listImage[numeroImage], options)
    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(image,null, position, null)
        numeroImage = (numeroImage + 1) % listImage.size
        val options = BitmapFactory.Options().apply { inScaled = true }
        image = BitmapFactory.decodeResource(context.resources, listImage[numeroImage], options)
    }

    override fun updatePosition(){
        if (ScreenData.screenWidth < position.left) {
            isOnScreen = false
        }
        if (isOnScreen) {
            position.left += vitesseX
            position.right += vitesseX
        }
    }

}

