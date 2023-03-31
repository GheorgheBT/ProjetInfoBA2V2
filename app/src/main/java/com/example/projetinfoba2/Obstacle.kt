package com.example.projetinfoba2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF
import android.os.Handler
import java.util.*

class Obstacle (private val context: Context, private val x: Float, private val y: Float) {

    private val listeObstacleImage = intArrayOf(R.drawable.par_1,R.drawable.par_2,R.drawable.par_3,R.drawable.par_4 ,R.drawable.par_5,R.drawable.par_6
        ,R.drawable.par_7,R.drawable.par_8,R.drawable.par_9,R.drawable.par_10) // liste des images des balles
    private val obstacleTaille = 100f
    val obstacleImage : Bitmap

    private val obstacleVitesse = -5f // la vitesse eb x à laquelle la balle va se deplacer
    private val obstacleDegats = 100
    private var obstacleOnScreen = true
    init {
        // initialise l'image de l'obstacle avec une valeur aléatoire
        val random = Random().nextInt(listeObstacleImage.size)
        obstacleImage = BitmapFactory.decodeResource(context.resources, listeObstacleImage[random])
    }

    val obstaclePosition = RectF(x, y, x + obstacleImage.width, y - obstacleImage.height) // encode la position de la balle dans un rectangle

    fun draw(canvas: Canvas) {
        if (obstacleOnScreen) {//dessine la balle dans le rectangle defini plus haut
            canvas.drawBitmap(
                obstacleImage,
                null,
                obstaclePosition,
                null
            )
        }

    }

    fun updatePosition() { // actualise la position d
        if (obstacleOnScreen) {
            obstaclePosition.left += obstacleVitesse
            obstaclePosition.right += obstacleVitesse
        }
    }

    fun obstacleDestroyed() {
        obstacleOnScreen = false
    }

}
