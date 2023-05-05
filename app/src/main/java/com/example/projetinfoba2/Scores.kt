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
    private val listeBarreVieImage = intArrayOf(
        R.drawable.barre_vie_0,
        R.drawable.barre_vie_1,
        R.drawable.barre_vie_2,
        R.drawable.barre_vie_3,
        R.drawable.barre_vie_4
    )

    //Vie actuelle du joueur
    private var joueurVie = vieInitiale
        set(value) {
            //Changement de l'image de la barre de vie en fonction de la vie
            field = value
            barreVie.setImageResource(listeBarreVieImage[value])
        }

    private var joueurScore = scoreInitial
        set(value) {
            //Valeur mise à 0 si négative
            field = if(value > 0){
                value
            } else{
                0
            }
            //Mise à jour du texte des scores
            val texte = "SCORE : $field"
            scoresLabel.text = texte
        }

    fun updateVie(valeur : Int = -1){
        //Incrémentation de la vie
        joueurVie += valeur
    }

    fun setVie(valeur: Int){
        //Assignetion de la valeur de la vie
        joueurVie = valeur
    }
    fun resetVie(){
        //Réinitialisation de la vie
        joueurVie = vieInitiale
    }

    fun isDead() : Boolean{
        //Détection de mort
        if(joueurVie == 0) {
            return true
        }
        return false
    }

    fun updateScore(valeur : Int = 50){
        //Incrémentation du score
        joueurScore += valeur
    }

    fun resetScore(){
        //Réinitialisation du score
        joueurScore = scoreInitial
    }
    fun getScore() : Int{
        //Retourne le score
        return joueurScore
    }
}