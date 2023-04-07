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


class MainActivity: Activity(), View.OnClickListener{

    lateinit var drawingView: DrawingView
    lateinit var joystickView: JoystickView
    lateinit var boutonTir : ImageButton

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawingView = findViewById(R.id.vMain)
        drawingView.setWillNotDraw(false)
        drawingView.invalidate()

        //Gestion du bouton
        boutonTir = findViewById(R.id.boutonTir)
        boutonTir.setOnClickListener(this)

        //Affichage du joystick
        joystickView = findViewById<JoystickView>(R.id.joystickView)
        joystickView.setWillNotDraw(false)
        joystickView.setZOrderOnTop(true)
        val sfhTrackHolder: SurfaceHolder = joystickView.holder
        sfhTrackHolder.setFormat(PixelFormat.TRANSPARENT)

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

    override fun onClick(p0: View?) {
        when(p0) {
            is ImageButton ->{
                drawingView.addBullet()
            }
        }
        // Si on decide d'ajouter d'autres boutons, leur comportement est ici
    }
}