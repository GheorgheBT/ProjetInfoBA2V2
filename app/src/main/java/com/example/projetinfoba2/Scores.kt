package com.example.projetinfoba2

import android.widget.TextView

class Scores {
    private var joueurVie = 5
    private var joueurScore = 0

    fun updateVie(valeur : Int = -1){
        joueurVie += valeur
    }
    fun getVie() : Int{
        return joueurVie
    }
    fun updateScore(valeur : Int = 1){
        joueurScore += valeur
    }
    fun getScore() : Int{
        return joueurScore
    }
    fun showInfo(textView: TextView){
        val data = "Vie : $joueurVie \nScore : $joueurScore"
        textView.text = data
    }
}