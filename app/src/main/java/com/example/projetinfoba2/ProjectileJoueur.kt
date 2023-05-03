package com.example.projetinfoba2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProjectileJoueur(context: Context, x: Float, y: Float, Taille: Float) : Projectile(), Deplacement{

    override val image: Bitmap = BitmapFactory.decodeResource(context.resources,
        R.drawable.ballejoueur
    )
    override val position: RectF = RectF(x, y - Taille/2, x + Taille, y + Taille/2) // encode la position de la balle dans un rectangle
    override var vitesseX: Float = 20f // la vitesse à laquelle la balle va se deplacer
    override var vitesseY : Float = 0f
    override val degats: Int = 100
    override var isOnScreen = true

    override fun draw(canvas: Canvas) {
        //dessine la balle dans le rectangle defini plus haut
        canvas.drawBitmap(image, null, position, null)
    }


    override fun updatePosition() {
        if (ScreenData.screenWidth <position.left){
            isOnScreen = false
        }
        if (isOnScreen) {
            position.left += vitesseX
            position.right += vitesseX
        }
    }

    override fun getCollision(obstacleList: MutableList<Obstacle>?, joueur: Joueur) {
        val rightX= position.right
        val centerY = position.centerY()

        runBlocking {   //On bloque la continuation du thread pendant le lancement des coroutines detectant les collisions
            if (obstacleList != null) {
                for (obstacle in obstacleList) {
                    launch {// Pour chaque boucle, lancement d'une coroutine pour que la detection de collision se fasse plus vite
                        if (obstacle.isOnScreen && rightX >= obstacle.position.left && rightX <= obstacle.position.right && centerY >= obstacle.position.top && centerY <= obstacle.position.bottom) {
                            isOnScreen = false
                            if (obstacle.isDestructible) {
                                joueur.scores.updateScore()
                                obstacle.isOnScreen = false
                            }
                        }
                    }
                }
            }
        }
    }
}