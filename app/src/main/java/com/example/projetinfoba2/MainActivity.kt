package com.example.projetinfoba2

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView


class MainActivity: Activity(){

    private lateinit var drawingView: DrawingView
    private lateinit var joystickView: JoystickView
    private lateinit var boutonTir : ImageButton

    private lateinit var scoreLabel : TextView // Pour l'affichage des différentes données du joueur
    private lateinit var barreVie : ImageView

    @Suppress("DEPRECATION")
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Permet de mettre le jeu en plein ecran
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)

        //Gestion affichage des scores
        scoreLabel = findViewById(R.id.scoreLabel)

        //Gestion de la barre de vie
        barreVie = findViewById(R.id.barreVie)

        // Gesion de la vue du jeu
        drawingView = findViewById(R.id.vMain)
        drawingView.setWillNotDraw(false)

        drawingView.joueur.scores.scoresLabel = scoreLabel
        drawingView.joueur.scores.barreVie = barreVie

        val difficulty = intent.getIntExtra("Difficulty", 1)
        drawingView.gameStatus.difficulty = difficulty

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

        joystickView.setOnTouchListener { v, event ->
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
                    if (joystickView.getIsPressed()) {
                        joystickView.setActuator(event.x, event.y)
                        joystickView.updateJoystickPosition()
                        joystickView.invalidate()
                    }
                }
            }
            drawingView.joueur.setSpeed(
                joystickView.deltaPosX / joystickView.cercleExtRayon,
                joystickView.deltaPosY / joystickView.cercleExtRayon
            )
            true
        }

    }
    fun closeAll() {
        //Ferme le jeu
        finishAffinity()

    }
}