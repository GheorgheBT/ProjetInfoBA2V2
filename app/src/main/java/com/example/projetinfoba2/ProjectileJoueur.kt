package com.example.projetinfoba2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProjectileJoueur(context: Context, x: Float, y: Float, Taille: Float) : Projectile(), Deplacement, DetecterCollisionAvecScore{

    //Image du projectile
    override val image: Bitmap = BitmapFactory.decodeResource(context.resources,
        R.drawable.ballejoueur
    )
    //Rectangle de délimitation du projectile
    override var position: RectF = RectF(x, y - Taille/2, x + Taille, y + Taille/2)

    //Vitesses du projectile
    override var vitesseX: Float = 20f // la vitesse à laquelle la balle va se deplacer
    override var vitesseY : Float = 0f

    //Condition d'affichage de l'ennemi
    override var isOnScreen = true

    override fun draw(canvas: Canvas) {
        //Dessine le projectile dans le rectangle de délimitaion
        canvas.drawBitmap(image, null, position, null)
    }


    override fun updatePosition() {
        //Arret d'affichage si le projectile sort de l'écran
        if (ScreenData.screenWidth <position.left){
            isOnScreen = false
        }
        //Mise à jour de la position
        if (isOnScreen) {
            position.left += vitesseX
            position.right += vitesseX
        }
    }

    override val listeObjetsDeCollision: ArrayList<ObjetDeCollision> = ArrayList()
    override fun onCollision(scores: Scores) {
        runBlocking {   //On bloque la continuation du thread pendant le lancement des coroutines detectant les collisions
            for (obstacle in listeObjetsDeCollision) {
                launch { // Pour chaque boucle, lancement d'une coroutine pour que la detection de collision se fasse plus vite
                    if(obstacle is Obstacle){
                        if (obstacle.isOnScreen && isInContact(position, obstacle.position)) {
                            isOnScreen = false
                            if (obstacle.isDestructible) {
                                scores.updateScore()
                                obstacle.isOnScreen = false
                            }
                        }
                    }
                }
            }
        }
    }
}