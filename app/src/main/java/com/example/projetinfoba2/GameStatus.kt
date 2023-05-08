package com.example.projetinfoba2

class GameStatus : Observable{
    //Liste des observers affectés par le changement de difficulté
    override val observers:ArrayList<Observer> = ArrayList()

    //Notification des observers lors de la modification de la difficulté
    private var difficulty : Int = 1
        set(value) {
            field = value
            hasUpdatedDifficulty(value)
        }

    fun updateDifficulty(diff : Int){
        difficulty = diff
    }

}