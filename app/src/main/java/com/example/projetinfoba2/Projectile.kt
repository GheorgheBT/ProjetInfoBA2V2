package com.example.projetinfoba2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF


class Projectile(
    private val context: Context,
    private val x: Float,
    private val y: Float,
    val numeroProjectile: Int,
    projectileTaille: Float
) {

    private val listeProjectileImage =
        intArrayOf(R.drawable.ball, R.drawable.oeuf) // liste des images des balles
    private val projectileImage: Bitmap =
        BitmapFactory.decodeResource(context.resources, listeProjectileImage[numeroProjectile])
    val projectilePosition = RectF(
        x,
        y,
        x + projectileTaille,
        y + projectileTaille
    ) // encode la position de la balle dans un rectangle
    private val projectileVitesse = 5f // la vitesse eb x à laquelle la balle va se deplacer
    private val projectileDegats = 100

    fun draw(canvas: Canvas) {
        //dessine la balle dans le rectangle defini plus haut
        canvas.drawBitmap(projectileImage, null, projectilePosition, null)
    }

    fun updatePositionBalle() { // actualise la position de la balle selon sa vitesse
            projectilePosition.left += projectileVitesse
            projectilePosition.right += projectileVitesse
    }

    fun updatePositionOeuf() { // actualise la position de la balle selon sa vitesse
            projectilePosition.top += projectileVitesse
            projectilePosition.bottom += projectileVitesse
    }
}