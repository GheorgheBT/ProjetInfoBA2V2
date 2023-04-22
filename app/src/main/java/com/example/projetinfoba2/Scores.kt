package com.example.projetinfoba2

import android.widget.TextView

class Scores{
    private val vieInitiale = 5
    private val scoreInitial = 0
    private var joueurVie = vieInitiale
    private var joueurScore = scoreInitial

    fun updateVie(valeur : Int = -1){
        joueurVie += valeur
    }
    fun resetVie(){
        joueurVie = vieInitiale
    }
    fun getVie() : Int{
        return joueurVie
    }
    fun updateScore(valeur : Int = 1){
        joueurScore += valeur
    }
    fun resetScore(){
        joueurScore = scoreInitial
    }
    fun getScore() : Int{
        return joueurScore
    }
    fun showInfo(textView: TextView){
        val data = "Vie : $joueurVie \nScore : $joueurScore"
        textView.text = data
    }
}