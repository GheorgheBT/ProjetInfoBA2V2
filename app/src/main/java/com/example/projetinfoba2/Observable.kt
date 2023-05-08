package com.example.projetinfoba2

interface Observable {
    val observers: ArrayList<Observer>

    fun add(observer: Observer) {
        observers.add(observer)
    }
    fun remove(observer: Observer) {
        observers.remove(observer)
    }
    fun hasUpdatedDifficulty(diff : Int) {
        observers.forEach { it.updateParameters(diff) }
    }

}