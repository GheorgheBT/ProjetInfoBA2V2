package com.example.projetinfoba2

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.ColorFilter
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity


class WelcomeActivity : AppCompatActivity() {

    lateinit var btnPlay: ImageButton
    lateinit var btnDiff : ImageButton
    val diffImages = intArrayOf(R.drawable.difficulty_button_1_star, R.drawable.difficulty_button_2_stars, R.drawable.difficulty_button_3_stars)
    val colors = intArrayOf(Color.GREEN, Color.YELLOW, Color.RED)
    var i = 1

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

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
}