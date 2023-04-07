package com.example.projetinfoba2

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kotlin.math.pow
import kotlin.math.sqrt

//Objet non utilisé
class Joystick (private val centreX : Float, private val centreY : Float, private val cercleIntRayon : Float, val cercleExtRayon : Float){

    //positions des boutons
    private var cercleIntPosX : Float = centreX
    private var cercleIntPosY : Float = centreY

    private var cercleExtPosX : Float = centreX
    private var cercleExtPosY : Float = centreY

    //Données sur l'aspect du bouton
    private val cercleIntPaint : Paint = Paint()
    private val cercleExtPaint : Paint = Paint()

    //Données sur l'existance des boutons
    private var joystickIntOnscreen : Boolean = true // J'ai vu que les objets ont ce type de paramètre, pour les joystics je pense que ca restera toujours true donc c'est pas forcement utile mais pour l'instant je le laisse
    private var joystickExtOnscreen : Boolean = true

    //Donnée si on appuie sur le joystick
    private var isJoystickPressed : Boolean = false

    //Données pour mettre a jour la position du joystick
    private var deltaPosX : Float = 0f
    private var deltaPosY : Float = 0f

    //Methodes
    fun draw(canvas: Canvas) {
        cercleIntPaint.color = Color.TRANSPARENT
        cercleExtPaint.color = Color.TRANSPARENT

        if (joystickExtOnscreen) {//dessine le grand cercle du joystick
            canvas.drawCircle(
                cercleExtPosX,
                cercleExtPosY,
                cercleExtRayon,
                cercleExtPaint)
        }
        if (joystickIntOnscreen) {//dessine le petit cercle du joystick
            canvas.drawCircle(
                cercleIntPosX,
                cercleIntPosY,
                cercleIntRayon,
                cercleIntPaint)
        }
    }

    fun isPressed (posAppuiX: Float, posAppuiY : Float) : Boolean{
        val distanceAppuiX = posAppuiX - cercleExtPosX
        val distanceAppuiY = posAppuiY - cercleExtPosY
        val distanceAppui = sqrt( distanceAppuiX.pow(2) + distanceAppuiY.pow(2))
        return distanceAppui < cercleExtRayon
    }

    fun setIsPressed(isPressed : Boolean){
        isJoystickPressed = isPressed
    }

    fun getIsPressed() : Boolean{
        return isJoystickPressed
    }

    fun setActuator(posAppuiX : Float, posAppuiY: Float){
        val distanceAppuiX = posAppuiX - cercleExtPosX
        val distanceAppuiY = posAppuiY - cercleExtPosY
        val distanceAppui = sqrt( distanceAppuiX.pow(2) + distanceAppuiY.pow(2))

        if(distanceAppui < cercleExtRayon){
            deltaPosX = distanceAppuiX
            deltaPosY = distanceAppuiY
        }
        else{
            deltaPosX = distanceAppuiX / distanceAppui * cercleExtRayon
            deltaPosY = distanceAppuiY / distanceAppui * cercleExtRayon
        }
    }

    fun getDeltaPosJoystick(): FloatArray {
        return floatArrayOf(deltaPosX, deltaPosY)
    }
    fun resetActuator(){
        deltaPosX = 0f
        deltaPosY = 0f
    }
    fun updateJoystickPosition(){
        cercleIntPosX = cercleExtPosX + deltaPosX
        cercleIntPosY = cercleExtPosY + deltaPosY
    }

    fun destroy(){ // Detruit le joystick (a voir si c'est utile)
        joystickIntOnscreen = false
        joystickExtOnscreen = false
    }
}