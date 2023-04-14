package com.example.projetinfoba2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class Projectile( context: Context, x: Float,y: Float,  numeroProjectile: Int, projectileTaille: Float) {

    private val listeProjectileImage = intArrayOf(R.drawable.ball, R.drawable.oeuf) // liste des images des balles

    private val Image: Bitmap = BitmapFactory.decodeResource(context.resources, listeProjectileImage[numeroProjectile])

    private val Position = RectF(x, y, x + projectileTaille, y + projectileTaille) // encode la position de la balle dans un rectangle

    private val Vitesse = 20f // la vitesse eb x à laquelle la balle va se deplacer

    private val Degats = 100

    var isOnScreen = true

    fun draw(canvas: Canvas) {
        //dessine la balle dans le rectangle defini plus haut
        canvas.drawBitmap(Image, null, Position, null)
    }

    fun updatePositionBalle(screenRect : RectF){ // actualise la position de la balle selon sa vitesse
        if (screenRect.right<Position.left  ){
            isOnScreen = false
        }
        if (isOnScreen) {
            Position.left += Vitesse
            Position.right += Vitesse
        }
    }

    fun updatePositionOeuf() { // actualise la position de la balle selon sa vitesse
        Position.top += Vitesse
        Position.bottom += Vitesse
    }

    fun getCollision(obstacleList : MutableList<Obstacle> ,obstacleToRemove: MutableList<Obstacle> ){
        val rightX= Position.right
        val centerY = Position.centerY()

        runBlocking {   //On bloque la continuation du thread pendant le lancement des coroutines detectant les collisions
            for (obstacle in obstacleList) {
                launch {// Pour chaque boucle, lancement d'une coroutine pour que la detection de collision se fasse plus vite
                    if (rightX >= obstacle.Position.left && rightX <= obstacle.Position.right && centerY >= obstacle.Position.top && centerY <= obstacle.Position.bottom) {
                        isOnScreen = false
                        if (obstacle.isDestructible) {
                            obstacleToRemove.add(obstacle)
                        }
                    }
                }
            }
        }
        //destroy(obstacleList , obstacleToRemove)
    }

    //private fun destroy (obstacleList : MutableList<Obstacle>, obstacleToRemove: MutableList<Obstacle> ){
        //supprime les obstacles qui ne sont plus atifs de la liste des obstacles actifs
    //    obstacleList.removeAll(obstacleToRemove)
    //    obstacleToRemove.clear()
    //
    //}

}