package com.example.projetinfoba2

import android.content.Context
import android.graphics.*

class ProjectileEnnemi(context: Context, x: Float, y: Float, Taille: Float) : Projectile(), DetecterCollision {

    //Aspect de la balle
    override val image: Bitmap = BitmapFactory.decodeResource(context.resources,
        R.drawable.balleennemie
    )

    //Délimitation rectangulaire de la balle
    override var position = RectF(x, y, x + Taille, y + Taille) // encode la position de la balle dans un rectangle

    //Vitesses de la balle
    override var vitesseX = 0f // la vitesse à laquelle la balle va se deplacer
    override var vitesseY = 15f


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

    override val listeObjetsDeCollision: ArrayList<ObjetDeCollision> = ArrayList()

    override fun onCollision() {
        for (obj in listeObjetsDeCollision){
            if (obj !is Joueur){ // On passe au prochain objet si cette condition n'est ps respectée
                continue
            }
            if (isInContact(position, obj.position)) {
                isOnScreen = false
                obj.scores.updateVie()
            }
        }
    }

}