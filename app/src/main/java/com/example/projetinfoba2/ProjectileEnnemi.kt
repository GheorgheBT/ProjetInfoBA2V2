package com.example.projetinfoba2

import android.content.Context
import android.graphics.*

class ProjectileEnnemi(context: Context, x: Float, y: Float, Taille: Float) : Projectile() {

    //Aspect de la balle
    override val image: Bitmap = BitmapFactory.decodeResource(context.resources,
        R.drawable.oeuf
    )

    //Position de la balle
    override val position = RectF(x, y, x + Taille, y + Taille) // encode la position de la balle dans un rectangle

    //Vitesses de la balle
    override var vitesseX = 0f // la vitesse à laquelle la balle va se deplacer
    override var vitesseY = 15f

    // Degats causé par la balle
    override val degats: Int = 100

    override var isOnScreen = true

    private var paint = Paint()

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(image, null, position, paint)
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
        joueur: Joueur
    )
    {
        val rightX= position.right
        val centerY = position.centerY()
        if (rightX >= joueur.position.left && rightX <= joueur.position.right && centerY >= joueur.position.top && centerY <= joueur.position.bottom) {
            isOnScreen = false
            joueur.scores.updateVie()
        }
    }

}