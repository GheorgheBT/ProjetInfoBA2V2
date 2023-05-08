package com.example.projetinfoba2

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity


class WelcomeActivity : AppCompatActivity() {
    //Bouton de lancement du jeu
    private lateinit var btnPlay: ImageButton
    //Bouton de sélection de la difficulté
    private lateinit var btnDiff : ImageButton

    //Liste des images du bouton de difficulté
    private val diffImages = intArrayOf(R.drawable.difficulty_button_1_star, R.drawable.difficulty_button_2_stars, R.drawable.difficulty_button_3_stars)
    //Liste des couleurs pour chaque difficulté
    private val colors = intArrayOf(Color.GREEN, Color.YELLOW, Color.RED)
    //Indice de sélection de couleur et image
    private var indice = 1
    //Ajout de musique de jeu
    private lateinit var mediaPlayer: MediaPlayer

    @Suppress("DEPRECATION")
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        //Initialisation le MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.intro)
        mediaPlayer.start()

        //Gestion du bouton commencer
        btnPlay = findViewById(R.id.btnPlay)
        btnPlay.setOnClickListener {
            Intent(this, MainActivity::class.java).also{
                //Création de l'activité principale, en passant comme donnée la difficulté de jeu
                it.putExtra("Difficulty", indice)
                startActivity(it)
            }
        }

        //Gestion du bouton de difficulté
        btnDiff = findViewById(R.id.btnDiff)
        btnDiff.setOnClickListener{
            //Changement de la difficulté de jeu, de l'image du bouton et des couleurs à chaque clic
            if (indice == diffImages.size) {
                indice = 0
                updateDifficulty()
                indice++
            } else {
                updateDifficulty()
                indice++
            }

        }
    }

    private fun updateDifficulty(){
        btnDiff.setImageResource(diffImages[indice])
        btnDiff.setColorFilter(colors[indice])
        btnPlay.setColorFilter(colors[indice])
    }
    override fun onResume() {
        //Reprise du jeu
        super.onResume()

        // Démarrez la lecture du fichier audio
        mediaPlayer.start()
    }

    override fun onPause() {
        //Jeu en pause
        super.onPause()

        // Arrêtez la lecture du fichier audio
        mediaPlayer.pause()
    }

    override fun onDestroy() {
        //Arret du jeu
        super.onDestroy()

        // Libérez les ressources utilisées par le mediaPlayer
        mediaPlayer.release()
    }


}