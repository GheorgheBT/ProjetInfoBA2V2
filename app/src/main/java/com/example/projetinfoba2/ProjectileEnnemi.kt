package com.example.projetinfoba2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProjectileEnnemi(context: Context, x: Float, y: Float,projectileTaille: Float) : Projectile(),ViewComponent {

    //Aspect de la bvelle
    override val image: Bitmap = BitmapFactory.decodeResource(context.resources,R.drawable.oeuf)

    //Position de la balle
    override val position: RectF = RectF(x, y, x + projectileTaille, y + projectileTaille) // encode la position de la balle dans un rectangle

    //Vitesses de la balle
    override val vitesse: Float = 20f // la vitesse eb x à laquelle la balle va se deplacer

    // Degats causé par la balle
    override val degats: Int = 100


    override var isOnScreen = true


    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(image, null, position, null)
    }

    override fun updatePosition() {
        if (gameData.screenWidth <position.left){
            isOnScreen = false
        }
        if (isOnScreen) {
            position.top += vitesse
            position.bottom += vitesse
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