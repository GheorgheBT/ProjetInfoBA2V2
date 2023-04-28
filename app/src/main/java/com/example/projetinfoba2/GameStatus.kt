package com.example.projetinfoba2

class GameStatus : Observable{
    override val observers:ArrayList<Observer> = ArrayList()
    var difficulty : Int = 1
        set(value) {
            field = value
            hasUpdatedDifficulty(value)
        }

    var isOnPause : Boolean = false
    fun changeState(observer : Observer){
        observer.updateDifficulty(difficulty)
    }
}