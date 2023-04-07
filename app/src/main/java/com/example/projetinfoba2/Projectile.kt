package com.example.projetinfoba2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF


class Projectile( private val context: Context, private val x: Float, private val y: Float, val numeroProjectile: Int, projectileTaille: Float) {

    private val listeProjectileImage = intArrayOf(R.drawable.ball, R.drawable.oeuf) // liste des images des balles
    private val projectileImage: Bitmap = BitmapFactory.decodeResource(context.resources, listeProjectileImage[numeroProjectile])
    val projectilePosition = RectF(x, y, x + projectileTaille, y + projectileTaille) // encode la position de la balle dans un rectangle
    private val projectileVitesse = 5f // la vitesse eb x à laquelle la balle va se deplacer
    private val projectileDegats = 100
    private var projectileOnScreen = true

    fun draw(canvas: Canvas) {
        //dessine la balle dans le rectangle defini plus haut
        canvas.drawBitmap(projectileImage, null, projectilePosition, null)
    }

    fun updatePositionBalle() : Boolean { // actualise la position de la balle selon sa vitesse
        if (projectileOnScreen) {
            projectilePosition.left += projectileVitesse
            projectilePosition.right += projectileVitesse
            return true
        }
        else {
            return false
        }
    }

    fun updatePositionOeuf() { // actualise la position de la balle selon sa vitesse
        projectilePosition.top += projectileVitesse
        projectilePosition.bottom += projectileVitesse
    }



    private fun isTouched(rect : RectF): Boolean { // detect si le un l'obstacle est en contacte avec un autre
        var x = projectilePosition.centerX()
        var y = projectilePosition.centerY()
        return x >= rect.left && x <= rect.right && y >= rect.bottom && y <= rect.top
    }

    fun isOnScreen(screenRect : RectF, obstacle: Obstacle){ // determine si l'obstacle doit toujour etre affiche
        if (screenRect.right<projectilePosition.left || isTouched(obstacle.obstaclePosition) ){
            projectileOnScreen = false

            if (isTouched(obstacle.obstaclePosition)){
                obstacle.obstacleOnScreen = false
            }
        }

    }




}