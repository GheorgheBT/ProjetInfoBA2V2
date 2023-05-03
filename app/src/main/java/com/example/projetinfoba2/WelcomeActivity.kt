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

    private lateinit var btnPlay: ImageButton
    private lateinit var btnDiff : ImageButton
    private val diffImages = intArrayOf(R.drawable.difficulty_button_1_star, R.drawable.difficulty_button_2_stars, R.drawable.difficulty_button_3_stars)
    private val colors = intArrayOf(Color.GREEN, Color.YELLOW, Color.RED)
    private var i = 1
    private lateinit var mediaPlayer: MediaPlayer

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        // Initialiser le MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.intro)

        // Jouer la musique
        mediaPlayer.start()

        btnPlay = findViewById(R.id.btnPlay)
        btnPlay.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).also{
                it.putExtra("Difficulty", i)
                startActivity(it)

            }
        }

        btnDiff = findViewById(R.id.btnDiff)
        btnDiff.setOnClickListener{
            if (i == diffImages.size) {
                i = 0
                btnDiff.setImageResource(diffImages[i])
                btnDiff.setColorFilter(colors[i])
                i++
            } else {
                btnDiff.setImageResource(diffImages[i])
                btnDiff.setColorFilter(colors[i])
                i++
            }

        }
    }

    override fun onResume() {
        super.onResume()

        // Démarrez la lecture du fichier audio
        mediaPlayer.start()
    }

    override fun onPause() {
        super.onPause()

        // Arrêtez la lecture du fichier audio
        mediaPlayer.pause()
    }

    override fun onDestroy() {
        super.onDestroy()

        // Libérez les ressources utilisées par le mediaPlayer
        mediaPlayer.release()
    }


}