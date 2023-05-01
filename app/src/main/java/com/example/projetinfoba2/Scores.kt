package com.example.projetinfoba2

import android.widget.TextView

open class Scores{
    private val vieInitiale = 5
    private val scoreInitial = 0
    private var joueurVie = vieInitiale
    private var joueurScore = scoreInitial

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

    fun getVie() : Int{
        return joueurVie
    }
    fun updateScore(valeur : Int = 1){
        joueurScore += valeur
        if (joueurScore < 0 ){
            isDead()
        }
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