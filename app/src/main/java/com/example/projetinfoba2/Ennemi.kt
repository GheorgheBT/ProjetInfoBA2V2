package com.example.projetinfoba2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF

class Ennemi(context: Context, posX : Float, posY : Float) : ViewComponent, Observer{

    //liste des images
    private val listImage = intArrayOf(R.drawable.frame_0, R.drawable.frame_1,
        R.drawable.ufo_image,R.drawable.frame_3,R.drawable.frame_4,R.drawable.frame_5,R.drawable.frame_6,R.drawable.frame_7) // liste des images

    //Aspect de l'ennemi
    private var image: Bitmap

    //position de l'ennemi
    override var position = RectF()
    //Vitesses de l'ennemi
    override var vitesseX = 5f
    override var vitesseY = 0f

    //Dimensions
    private val height = context.resources.getString(R.string.LongueurEnnemi).toFloat()
    private val width = context.resources.getString(R.string.LargeurEnnemi).toFloat()
    //Delai de tir
    var delaiTir : Double = 200.0

    init {
        val options = BitmapFactory.Options().apply { inScaled = true }
        image = BitmapFactory.decodeResource(context.resources, R.drawable.ufo_image, options)
        position = RectF(posX - width/2, posY - height/2,posX + width/2, posY + height/2) // position de l'oiseau encode dans un rectangle

    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(image,null, position, null)
    }

    override fun updatePosition(){
        if (ScreenData.screenWidth < position.left) {
            resetPosition()
        }
        position.left += vitesseX
        position.right += vitesseX

    }
    private fun resetPosition(){
        position.right -= ScreenData.screenWidth + width
        position.left -= ScreenData.screenWidth + width
    }

    override fun updateDifficulty(diff: Int) {
        when (diff){
            1 -> {
                vitesseX = 5f
                delaiTir = 200.0
            }
            2 -> {
                vitesseX = 7f
                delaiTir = 170.0
            }
            3 -> {
                vitesseX = 9f
                delaiTir = 150.0
            }
        }
    }
}

