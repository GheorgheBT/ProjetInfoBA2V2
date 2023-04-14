package com.example.projetinfoba2

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
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

    private var cercleIntRayon : Float = 0f
    var cercleExtRayon : Float = 0f    //Cercle 2x plus petit que celui exterieur

    //Données sur l'aspect du bouton
    private val cercleIntPaint : Paint = Paint()
    private val cercleExtPaint : Paint = Paint()

    //Données sur l'existance des boutons
    private var joystickOnscreen : Boolean = true    //J'ai vu que les objets ont ce type de paramètre, pour les joystics je pense que ca restera toujours true donc c'est pas forcement utile mais pour l'instant je le laisse

    //Donnée si on appuie sur le joystick
    private var isJoystickPressed : Boolean = false

    //Données pour mettre a jour la position du joystick
    var deltaPosX : Float = 0f
    var deltaPosY : Float = 0f

    init {
        cercleIntPaint.color = context.getColor(R.color.JoystickIntColor)
        cercleExtPaint.color = context.getColor(R.color.JoystickExtColor)

        holder.setFormat(PixelFormat.TRANSPARENT) // Pour mettre l'arrière du joystick transparent
        setZOrderOnTop(true)
        setWillNotDraw(false) // permet d'activer Ondraw
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
    fun resetActuator(){
        deltaPosX = 0f
        deltaPosY = 0f
    }
    fun updateJoystickPosition(){
        cercleIntPosX = cercleExtPosX + deltaPosX
        cercleIntPosY = cercleExtPosY + deltaPosY
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        cercleIntPosX = (width/2).toFloat() + deltaPosX
        cercleIntPosY = (height/2).toFloat() + deltaPosY
        cercleExtPosX = (width/2).toFloat()
        cercleExtPosY = (height/2).toFloat()
        cercleIntRayon = min(width/2,height/3).toFloat() / 2f //On prend la valeur minimale entre la largeur et la longueur du "view" comme ca si la view est un rectangle, le cercle ne depasse pas du rectangle
        cercleExtRayon = min(width/2,height/3).toFloat()

        canvas?.drawColor(Color.TRANSPARENT)

        if (joystickOnscreen) {//dessine le grand cercle du joystick
            canvas?.drawCircle(
                cercleExtPosX,
                cercleExtPosY,
                cercleExtRayon,
                cercleExtPaint
            )
        }
        if (joystickOnscreen) {//dessine le petit cercle du joystick
            canvas?.drawCircle(
                cercleIntPosX,
                cercleIntPosY,
                cercleIntRayon,
                cercleIntPaint
            )
        }
    }
}