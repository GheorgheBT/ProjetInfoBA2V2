package com.example.projetinfoba2

import android.content.Context
import android.graphics.*
import android.widget.TextView
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.abs


class Joueur(context: Context, x: Float, y: Float, joueurTaille: Float) : ViewComponent
{
    //Aspect du joueur
    private var Image = BitmapFactory.decodeResource(context.resources, R.drawable.pers_1, null)
    //position du joueur
    var Position = RectF(x, y, x + joueurTaille, y + joueurTaille) // position du joueur encodé dans un rectangle

    //Vie du joueur
    var Vie = 0


    //point du joueur
    var Point = 5

    //Vitesses du joueur
    private val vitesseMax = 15f
    private var vitesseX: Float = 0f
    private var vitesseY: Float = 0f

    //Coté de collision
    private var collisionSide : String = "NoCollision"

    // vitess de rebond
    private var vitesseRebond = 0f

    //Vie du joueur
    val scores = Scores()
    lateinit var vieLabel : TextView

    // Bouclier du jouerur
    val bouclier = Bouclier()

    fun draw(canvas: Canvas, paint: Paint, width: Float, height: Float) {
        canvas.drawBitmap(Image, null, Position, null)
        paint.color = Color.BLACK
        paint.textSize = 50f
        //canvas.drawText("Vies : ${scores.getVie()}", width, height, paint)
        //canvas.drawText("Point: $Point", width, height - 100f, paint)
    }


    override fun updatePosition() {
        // Limitation du mouvement du joueur si il se trouve à la bordure
        when{
            vitesseX > 0 -> if(Position.right > gameData.screenWidth){vitesseX = 0f}
            vitesseX < 0 -> if(Position.left < gameData.leftScreenSide){vitesseX = 0f}
        }
        when{
            vitesseY < 0 -> if(Position.top < gameData.upScreenSide){vitesseY = 0f}
            vitesseY > 0 -> if(Position.bottom > gameData.screenHeight){vitesseY = 0f}
        }
        // Limitation du mouvement du joueur si en contact avec les obstacles
        when (collisionSide) {
            "RightCollision" -> if(vitesseX > 2*vitesseRebond){vitesseX = 2*vitesseRebond}
            "LeftCollision" -> if(vitesseX < -2*vitesseRebond){vitesseX = -2*vitesseRebond}
            "UpCollision" -> if(vitesseY < -vitesseRebond/2){vitesseY = -vitesseRebond/2}
            "DownCollision" -> if (vitesseY > vitesseRebond/2){vitesseY = vitesseRebond/2}
        }
        // Déplacement du joueur
        Position.right += vitesseX
        Position.left += vitesseX
        Position.top += vitesseY
        Position.bottom += vitesseY

    }
    fun setSpeed(normeX : Float, normeY : Float){
        //Assignation des vitesses en vonction de la vitesse maximale du joueur et des valeurs normées du joystick
        vitesseX = normeX * vitesseMax
        vitesseY = normeY * vitesseMax
    }

    fun getCollisionSide(obstacleList : MutableList<Obstacle>) {
        var isColliding = false
        var collidedObject = RectF()
        collisionSide = "NoCollision"

        runBlocking {   //On bloque la continuation du thread pendant le lancement des coroutines detectant les collisions
            for (obstacle in obstacleList) { //Parcours de tous les obstacles de la liste d'obstacles
                launch { // Pour chaque boucle, lancement d'une coroutine pour que la detection de collision se fasse plus vite
                    vitesseRebond = obstacle.Vitesse
                    val rectF = obstacle.Position // Assignation des "RectF" des obstacles
                    //Detection si il y a collision
                    if (Position.right > rectF.left && Position.left < rectF.right && Position.top < rectF.bottom && Position.bottom > rectF.top) {
                        isColliding = true
                        collidedObject = rectF
                    }
                }
            }
        }
        //Si il y a collision, détermination du coté de la collision (entre joueur et obstacle)
        if(isColliding){
            val xRealCollision = collidedObject.centerX() - Position.centerX() // Distance réelle entre les centres des 2 objets
            val yRealCollision = collidedObject.centerY() - Position.centerY()

            val xMinCollision = collidedObject.width()/2 + Position.width()/2 // Distances minimales entre les centres des 2 objets avant que la collision soit inévitable
            val yMinCollision = collidedObject.height()/2 + Position.height()/2

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
    fun updateVie(screenRectF: RectF){
        if(Position.right < screenRectF.left) {
            scores.updateVie(-5)
        }
        scores.showInfo(vieLabel)
    }
}