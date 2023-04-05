package com.example.projetinfoba2

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Debug
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceView
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

class JoystickView @JvmOverloads constructor(context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0) : SurfaceView(context, attributes, defStyleAttr){

    //Données sur les positions et dimensions des boutons
    private var cercleIntPosX : Float = (width/2).toFloat()
    private var cercleIntPosY : Float = (height/2).toFloat()

    private var cercleExtPosX : Float = (width/2).toFloat()
    private var cercleExtPosY : Float = (height/2).toFloat()

    private var cercleIntRayon : Float = 0f //On prend la valeur minimale entre la largeur et la longueur du "view" comme ca si la view est un rectangle, le cercle ne depasse pas du rectangle
    private var cercleExtRayon : Float =0f    //Cercle 3x plus petit que celui exterieur

    //Données sur l'aspect du bouton
    private val cercleIntPaint : Paint = Paint()
    private val cercleExtPaint : Paint = Paint()

    //Données sur l'existance des boutons
    private var joystickIntOnscreen : Boolean = true    //J'ai vu que les objets ont ce type de paramètre, pour les joystics je pense que ca restera toujours true donc c'est pas forcement utile mais pour l'instant je le laisse
    private var joystickExtOnscreen : Boolean = true

    //Donnée si on appuie sur le joystick
    private var isJoystickPressed : Boolean = false

    //Données pour mettre a jour la position du joystick
    private var deltaPosX : Float = 0f
    private var deltaPosY : Float = 0f
    //joystick listener


    fun onJoystickTouch(motionEvent: MotionEvent?) {
        when (motionEvent?.action) {
            MotionEvent.ACTION_DOWN -> {
                if (isPressed(motionEvent.x, motionEvent.y)) {
                    setIsPressed(true)
                }
            }
            MotionEvent.ACTION_UP -> {
                setIsPressed(false)
                resetActuator()
                updateJoystickPosition()
                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {
                if (getIsPressed()){
                    setActuator(motionEvent.x, motionEvent.y)
                    updateJoystickPosition()
                    //Log.d("TAG","AAAAEAFDW")
                    invalidate()
                }
            }
        }

    }

    init {
        cercleIntPaint.color = context.getColor(R.color.JoystickIntColor)
        cercleExtPaint.color = context.getColor(R.color.JoystickExtColor)
        Log.d("TAG", width.toString())
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
        //Log.d("TAG",cercleIntPosX.toString())
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        cercleIntPosX = (width/2).toFloat() + deltaPosX
        cercleIntPosY = (height/2).toFloat() + deltaPosY
        cercleExtPosX = (width/2).toFloat()
        cercleExtPosY = (height/2).toFloat()
        cercleIntRayon = min(width/2,height/3).toFloat() / 2f
        cercleExtRayon = min(width/2,height/3).toFloat()

        canvas?.drawColor(Color.TRANSPARENT)
        if (joystickExtOnscreen) {//dessine le grand cercle du joystick
            //Log.d("TAG", cercleExtRayon.toString())
            canvas?.drawCircle(
                cercleExtPosX,
                cercleExtPosY,
                cercleExtRayon,
                cercleExtPaint
            )
        }
        if (joystickIntOnscreen) {//dessine le petit cercle du joystick
            canvas?.drawCircle(
                cercleIntPosX,
                cercleIntPosY,
                cercleIntRayon,
                cercleIntPaint
            )
        }
    }
}
