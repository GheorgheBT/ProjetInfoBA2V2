package com.example.projetinfoba2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF

class Ennemi(context: Context, posX : Float, posY : Float) : Deplacement, Observer, ObjetDeCollision{

    //Aspect de l'ennemi
    private var image: Bitmap

    //Rectangle délimitant l'ennemi
    override var position = RectF()

    //Vitesses de l'ennemi
    override var vitesseX = 5f
    override var vitesseY = 0f

    //Dimensions de l'ennemi
    private val height = context.resources.getString(R.string.LongueurEnnemi).toFloat()
    private val width = context.resources.getString(R.string.LargeurEnnemi).toFloat()

    //Reprise du context pour l'utiliser dans la classe
    private val ctx = context

    //Condition d'affichage de l'ennemi
    override var isOnScreen = true

    init {
        //Initialisation de l'image de l'ennemi
        val options = BitmapFactory.Options().apply { inScaled = true }
        image = BitmapFactory.decodeResource(context.resources, R.drawable.ufo_image, options)
        //Initialisation de la position de l'ennemi
        position = RectF(posX - width/2, posY - height/2,posX + width/2, posY + height/2)

    }

    fun draw(canvas: Canvas) {
        //Dessin de l'ennemi a chaque appel
        canvas.drawBitmap(image,null, position, null)
    }

    override fun updatePosition(){
        //Réinitialisation de la position si l'ennemi sort de l'écran
        if (ScreenData.screenWidth < position.left) {
            resetPosition()
        }

        //Mise a jour de la position de l'ennemi
        position.left += vitesseX
        position.right += vitesseX

    }
    private fun resetPosition(){
        //Fonction de réinitialisation de la position par un déplacement à gauche le l'écran
        position.right -= ScreenData.screenWidth + width
        position.left -= ScreenData.screenWidth + width
    }

    override fun updateParameters(diff: Int) {
        //Moification de la vitesse de l'ennemi en fonction de la difficulté
        when (diff){
            1 -> {vitesseX = ctx.resources.getString(R.string.VitesseEnnemiEasy).toFloat()}
            2 -> {vitesseX = ctx.resources.getString(R.string.VitesseEnnemiMedium).toFloat()}
            3 -> {vitesseX = ctx.resources.getString(R.string.VitesseEnnemiHard).toFloat()}
        }
    }
}

