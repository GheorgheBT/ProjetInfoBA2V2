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
    private val Image: Bitmap

    // vitesse de l'obstacle
    var Vitesse = -5f

    // Degats causés par le contacte avec un obstacle
    private val Degats = 100

    //Proprieté de destructibilité
    var isDestructible = false

    //Proprieté d'affichage
    var isOnScreen = true
    init {// initialise l'image de l'obstacle de manière  aléatoire
        val random = Random().nextInt(listeObstacleImage.size)
        Image = BitmapFactory.decodeResource(context.resources, listeObstacleImage[random])
        if ( listeObstacleDestuctible.contains(listeObstacleImage[random])){// caracterisation de la destructibilité
            isDestructible = true
        }
    }
    // position de l'obstacle (dans un RectF)
    val Position = RectF(x, y, x  + Image.width, y + Image.height) // encode la position

    fun draw(canvas: Canvas) {
        //dessine l'obstacle dans le rectangle defini plus haut
        if (isOnScreen) {
           canvas.drawBitmap(Image, null, Position, null)
        }
    }

    override fun updatePosition(){
        // actualise la position
        if (Position.right< gameData.leftScreenSide) {
            isOnScreen = false
        }
        if (isOnScreen) {
            Position.left += Vitesse
            Position.right += Vitesse
        }
    }

}
