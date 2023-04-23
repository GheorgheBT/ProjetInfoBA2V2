package com.example.projetinfoba2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF
import java.util.*

class Obstacle ( context: Context,  x: Float, y: Float) : ViewComponent{

    //Images des obstacles
    private val listeObstacleImage = intArrayOf(
        R.drawable.par_1,
        R.drawable.par_2,
        R.drawable.par_3,
        R.drawable.par_4,
        R.drawable.par_5,
        R.drawable.par_6,
        R.drawable.par_7,
        R.drawable.par_8,
        R.drawable.par_9,
        R.drawable.par_10
    )

    //Obstacles destructibles
    private val listeObstacleDestuctible = intArrayOf(
        R.drawable.par_2,
        R.drawable.par_4,
        R.drawable.par_6,
        R.drawable.par_8,
        R.drawable.par_10
    )

    //Aspect de l'obstacle
    private val image: Bitmap

    // vitesse de l'obstacle
    var vitesse = -5f

    // Degats causés par le contacte avec un obstacle
    private val Degats = 100

    //Proprieté de destructibilité
    var isDestructible = false

    //Proprieté d'affichage
    private var isOnScreen = true
    init {// initialise l'image de l'obstacle de manière  aléatoire
        val random = Random().nextInt(listeObstacleImage.size)
        image = BitmapFactory.decodeResource(context.resources, listeObstacleImage[random])
        if ( listeObstacleDestuctible.contains(listeObstacleImage[random])){// caracterisation de la destructibilité
            isDestructible = true
        }
    }
    // position de l'obstacle (dans un RectF)
    val position = RectF(x, y, x  + image.width, y + image.height) // encode la position

    fun draw(canvas: Canvas) {
        //dessine l'obstacle dans le rectangle defini plus haut
        if (isOnScreen) {
           canvas.drawBitmap(image, null, position, null)
        }
    }

    override fun updatePosition(){
        // actualise la position
        if (position.right< gameData.leftScreenSide) {
            isOnScreen = false
        }
        if (isOnScreen) {
            position.left += vitesse
            position.right += vitesse
        }
    }

}
