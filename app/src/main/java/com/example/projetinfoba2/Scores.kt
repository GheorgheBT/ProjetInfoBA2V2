package com.example.projetinfoba2

import android.widget.ImageView
import android.widget.TextView

class Scores{
    //Vie et score du joueur au début du jeu
    private val vieInitiale = 4
    private val scoreInitial = 0

    //Barre de vie
    lateinit var barreVie : ImageView

    //Texte du score
    lateinit var scoresLabel : TextView

    //Liste d'images pour la barre de vie
    private val listeObstacleImage = intArrayOf(
        R.drawable.barre_vie_0,
        R.drawable.barre_vie_1,
        R.drawable.barre_vie_2,
        R.drawable.barre_vie_3,
        R.drawable.barre_vie_4
    )

    //Vie actuelle du joueur
    private var joueurVie = vieInitiale
        set(value) {
            field = value
            barreVie.setImageResource(listeObstacleImage[value])
        }

    private var joueurScore = scoreInitial
        set(value) {
            field = value
            val texte = "SCORE : $value"
            scoresLabel.text = texte
        }

    fun updateVie(valeur : Int = -1){
        joueurVie += valeur
    }

    fun setVie(valeur: Int){
        joueurVie = valeur
    }
    fun isDead() : Boolean{
        if(joueurVie == 0) {
            return true
        }
        return false
    }
    fun resetVie(){
        joueurVie = vieInitiale
    }

    fun updateScore(valeur : Int = 50){
        joueurScore += valeur
    }
    fun resetScore(){
        joueurScore = scoreInitial
    }
    fun getScore() : Int{
        return joueurScore
    }
}