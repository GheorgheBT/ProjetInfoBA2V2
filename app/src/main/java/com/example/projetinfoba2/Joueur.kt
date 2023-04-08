package com.example.projetinfoba2

import android.content.Context
import android.graphics.*
import kotlin.math.abs


class Joueur(
    context: Context,
    x: Float,
    y: Float,
    joueurTaille: Float
    )
{
    //Aspect du joueur
    private var joueurImage = BitmapFactory.decodeResource(context.resources, R.drawable.pers_1, null)

    //Vie du joueur
    var joueurPosition = RectF(x, y, x + joueurTaille, y + joueurTaille) // position du joueur encodé dans un rectangle

    //Vie du joueur
    private var joueurVie = 5

    // point du joueur
    private val joueurPoint = 0

    //Vitesses du joueur
    private val vitesseMax = 20f
    private var vitesseX: Float = 0f
    private var vitesseY: Float = 0f

    //Coté de collision
    private var collisionSide : String = "NoCollision"

    fun draw(canvas: Canvas, paint: Paint, width: Float, height: Float) {
        canvas.drawBitmap(joueurImage, null, joueurPosition, null)
        paint.color = Color.BLACK
        paint.textSize = 50f
        canvas.drawText("Vies : $joueurVie", width, height, paint)
        canvas.drawText("Point: $joueurPoint", width, height - 100f, paint)
    }


    fun updatePosition(screenMinX : Float, screenMinY : Float, screenMaxX : Float, screenMaxY : Float) {
        // Limitation du mouvement du joueur si il se trouve à la bordure
        when{
            vitesseX > 0 -> if(joueurPosition.right > screenMaxX){vitesseX = 0f}
            vitesseX < 0 -> if(joueurPosition.left < screenMinX){vitesseX = 0f}
        }
        when{
            vitesseY < 0 -> if(joueurPosition.top < screenMinY){vitesseY = 0f}
            vitesseY > 0 -> if(joueurPosition.bottom > screenMaxY){vitesseY = 0f}
        }
        // Limitation du mouvement du joueur si en contact avec les obstacles
        when (collisionSide) {
            "RightCollision" -> if(vitesseX > -5f){vitesseX = -5f}
            "LeftCollision" -> if(vitesseX < -5f){vitesseX = -5f}
            "UpCollision" -> if(vitesseY < 0){vitesseY = 0f}
            "DownCollision" -> if (vitesseY > 0){vitesseY = 0f}
        }
        // Déplacement du joueur
        joueurPosition.right += vitesseX
        joueurPosition.left += vitesseX
        joueurPosition.top += vitesseY
        joueurPosition.bottom += vitesseY
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

        for(obstacle in obstacleList){ //Parcours de tous les obstacles de la liste d'obstacles
            val rectF = obstacle.obstaclePosition // Assignation des "RectF" des obstacles
            //Detection si il y a collision
            if (joueurPosition.right > rectF.left && joueurPosition.left < rectF.right && joueurPosition.top < rectF.bottom && joueurPosition.bottom > rectF.top) {
                isColliding = true
                collidedObject = rectF
            }
        }
        //Si il y a collision, détermination du coté de la collision (entre joueur et obstacle)
        if(isColliding){
            val xRealCollision = collidedObject.centerX() - joueurPosition.centerX() // Distance réelle entre les centres des 2 objets
            val yRealCollision = collidedObject.centerY() - joueurPosition.centerY()

            val xMinCollision = collidedObject.width()/2 + joueurPosition.width()/2 // Distances minimales entre les centres des 2 objets avant que la collision soit inévitable
            val yMinCollision = collidedObject.height()/2 + joueurPosition.height()/2

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
}