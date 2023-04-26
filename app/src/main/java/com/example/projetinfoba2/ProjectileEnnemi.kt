package com.example.projetinfoba2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProjectileEnnemi(context: Context, x: Float, y: Float,projectileTaille: Float) : Projectile() {

    //Aspect de la balle
    override val image: Bitmap = BitmapFactory.decodeResource(context.resources,R.drawable.oeuf)

    //Position de la balle
    override val position = RectF(x, y, x + projectileTaille, y + projectileTaille) // encode la position de la balle dans un rectangle

    //Vitesses de la balle
    override var vitesseX = 0f // la vitesse à laquelle la balle va se deplacer
    override var vitesseY = 20f

    // Degats causé par la balle
    override val degats: Int = 100

    override var isOnScreen = true


    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(image, null, position, null)
    }

    override fun updatePosition() {
        if (ScreenData.screenWidth <position.left){
            isOnScreen = false
        }
        if (isOnScreen) {
            super.updatePosition()
        }
    }
    override fun getCollision(
        obstacleList: MutableList<Obstacle>?,
        obstacleToRemove: MutableList<Obstacle>?,
        joueur: Joueur
    ) {
        val rightX= position.right
        val centerY = position.centerY()

        runBlocking {   //On bloque la continuation du thread pendant le lancement des coroutines detectant les collisions
            if (obstacleList != null) {
                for (obstacle in obstacleList) {
                    launch {// Pour chaque boucle, lancement d'une coroutine pour que la detection de collision se fasse plus vite
                        if (rightX >= obstacle.position.left && rightX <= obstacle.position.right && centerY >= obstacle.position.top && centerY <= obstacle.position.bottom) {
                            isOnScreen = false
                            if (obstacle.isDestructible) {
                                joueur.scores.updateScore()
                                obstacleToRemove?.add(obstacle)
                            }
                        }
                    }
                }
            }
        }
    }
}