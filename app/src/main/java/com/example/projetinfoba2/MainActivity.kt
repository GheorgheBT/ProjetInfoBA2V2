package com.example.projetinfoba2

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.PixelFormat
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView


class MainActivity: Activity(){

    lateinit var drawingView: DrawingView
    lateinit var joystickView: JoystickView
    lateinit var boutonTir : ImageButton
    lateinit var fpsLabel : TextView // Pour voir nos fsp (seulement durant le developpement)

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Permet de mettre le jeu en plein ecran
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)

        // Gestion du texte des fps
        fpsLabel = findViewById(R.id.fpsText)

        // Gesion de la vue du jeu
        drawingView = findViewById(R.id.vMain)
        drawingView.setWillNotDraw(false)
        drawingView.fpsLabel = fpsLabel
        drawingView.invalidate()

        //Gestion du bouton
        boutonTir = findViewById(R.id.boutonTir)

        boutonTir.setOnTouchListener { view, event ->
            when (event.action){
                MotionEvent.ACTION_DOWN -> {
                    drawingView.isShooting = true
                }
                MotionEvent.ACTION_UP -> {
                    drawingView.isShooting = false
                }
            }
            true
        }

        //Gestion du joystick
        joystickView = findViewById(R.id.joystickView)

        joystickView.setOnTouchListener(OnTouchListener {v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (joystickView.isPressed(event.x, event.y)) {
                        joystickView.setIsPressed(true)
                    }
                }
                MotionEvent.ACTION_UP -> {
                    joystickView.setIsPressed(false)
                    joystickView.resetActuator()
                    joystickView.updateJoystickPosition()
                    joystickView.invalidate()
                }
                MotionEvent.ACTION_MOVE -> {
                    if (joystickView.getIsPressed()){
                        joystickView.setActuator(event.x, event.y)
                        joystickView.updateJoystickPosition()
                        joystickView.invalidate()
                    }
                }
            }
            drawingView.joueur.setSpeed(joystickView.deltaPosX/joystickView.cercleExtRayon, joystickView.deltaPosY/ joystickView.cercleExtRayon)
            true
        })
    }

}