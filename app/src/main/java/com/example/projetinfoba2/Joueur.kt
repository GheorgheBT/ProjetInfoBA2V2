package com.example.projetinfoba2

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.abs


class Joueur(context: Context, private var taille: Float) : Deplacement, Collider, Observer {

    //Aspect du joueur
    private var image = BitmapFactory.decodeResource(context.resources, R.drawable.ship1, null)

    //position du joueur
    override var position = RectF(ScreenData.leftScreenSide, ScreenData.screenHeight/2 -taille/2, ScreenData.leftScreenSide + taille, ScreenData.screenHeight/2 + taille/2) // position du joueur encodé dans un rectangle

    //Vitesses du joueur
    private var vitesseMax = 15f
    override var vitesseX = 0f
    override var vitesseY = 0f

    //Coté de collision
    private var collisionSide : String = "NoCollision"

    // vitess de rebond
    private var vitesseRebond = 0f

    //Vie du joueur
    val scores = Scores()

    // Intervalle de tir
    var intervalleTir : Long = 400

    //Context
    private val ctx = context
   fun draw(canvas: Canvas, paint: Paint, width: Float, height: Float) {
        canvas.drawBitmap(image, null, position, null)
   }

    override fun updatePosition() {
        // Limitation du mouvement du joueur si il se trouve à la bordure
        when{
            vitesseX > 0 -> if(position.right > ScreenData.screenWidth){vitesseX = 0f}
            vitesseX < 0 -> if(position.left < ScreenData.leftScreenSide){vitesseX = 0f}
        }
        when{
            vitesseY < 0 -> if(position.top < ScreenData.upScreenSide){vitesseY = 0f}
            vitesseY > 0 -> if(position.bottom > ScreenData.screenHeight){vitesseY = 0f}
        }
        // Limitation du mouvement du joueur si en contact avec les obstacles
        when (collisionSide) {
            "RightCollision" -> if(vitesseX > 2*vitesseRebond){vitesseX = 2*vitesseRebond}
            "LeftCollision" -> if(vitesseX < -2*vitesseRebond){vitesseX = -2*vitesseRebond}
            "UpCollision" -> if(vitesseY < -vitesseRebond/2){vitesseY = -vitesseRebond/2}
            "DownCollision" -> if (vitesseY > vitesseRebond/2){vitesseY = vitesseRebond/2}
        }
        // Déplacement du joueur
        super.updatePosition()
    }

    fun setSpeed(normeX : Float, normeY : Float){
        //Assignation des vitesses en vonction de la vitesse maximale du joueur et des valeurs normées du joystick
        vitesseX = normeX * vitesseMax
        vitesseY = normeY * vitesseMax
    }
    override fun detectCollision(obstacleList : MutableList<Obstacle>){
        collisionSide = "NoCollision"
        var isColliding = false
        var collidedObject = RectF()

        runBlocking {   //On bloque la continuation du thread pendant le lancement des coroutines detectant les collisions
            for (obstacle in obstacleList) { //Parcours de tous les obstacles de la liste d'obstacles
                launch {
                    if(obstacle.isOnScreen) {// Pour chaque boucle, lancement d'une coroutine pour que la detection de collision se fasse plus vite
                        val rectF = obstacle.position // Assignation des rectF
                        vitesseRebond = obstacle.vitesseX
                        //Detection si il y a collision
                        if (position.right > rectF.left && position.left < rectF.right && position.top < rectF.bottom && position.bottom > rectF.top) {
                            isColliding = true
                            scores.updateScore(-200)
                            collidedObject = rectF
                        }
                    }
                }
            }
        }

        //Si il y a collision, détermination du coté de la collision (entre joueur et obstacle)
        if(isColliding){
            val xRealCollision = collidedObject.centerX() - position.centerX() // Distance réelle entre les centres des 2 objets
            val yRealCollision = collidedObject.centerY() - position.centerY()

            val xMinCollision = collidedObject.width()/2 + position.width()/2 // Distances minimales entre les centres des 2 objets avant que la collision soit inévitable
            val yMinCollision = collidedObject.height()/2 + position.height()/2

            val dx = xMinCollision - abs(xRealCollision) //Différences entre les distances minimales et réelles
            val dy = yMinCollision - abs(yRealCollision)

            if (dx < dy){ // Signifie que le joueur se trouve très proche du coté droit ou gauche de l'obstacle
                when{
                    xRealCollision > 0 -> collisionSide = "RightCollision" // Le joueur se trouve a gauche
                    xRealCollision < 0 -> collisionSide = "LeftCollision" // Le joueur se trouve a droite
                }
            }
            else if (dy < dx){  // Signifie que le joueur se trouve très proche du coté haut ou bas de l'obstacle
                when{
                    yRealCollision > 0 -> collisionSide = "DownCollision" // Le joueur se trouve en haut
                    yRealCollision < 0 -> collisionSide = "UpCollision" // Le joueur se trouve en bas
                }
            }
        }
    }
    fun updateVie(){
        if(position.right < ScreenData.leftScreenSide) {
            scores.setVie(0)
        }
    }

    fun reset(){
        position = RectF(ScreenData.leftScreenSide, ScreenData.screenHeight/2, ScreenData.leftScreenSide + taille, ScreenData.screenHeight/2 + taille)
    }
    override fun updateDifficulty(diff : Int) {
        when(diff){
            1 -> {
                vitesseMax = ctx.resources.getString(R.string.PlayerSpeedEasy).toFloat()
                intervalleTir = ctx.resources.getString(R.string.PlayerShootIntervalEasy).toLong()
            }
            2 -> {
                vitesseMax = ctx.resources.getString(R.string.PlayerSpeedMedium).toFloat()
                intervalleTir = ctx.resources.getString(R.string.PlayerShootIntervalMedium).toLong()
            }
            3 -> {
                vitesseMax = ctx.resources.getString(R.string.PlayerSpeedHard).toFloat()
                intervalleTir = ctx.resources.getString(R.string.PlayerShootIntervalHard).toLong()
            }
        }
    }
}