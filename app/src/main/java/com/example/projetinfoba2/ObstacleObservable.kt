package com.example.projetinfoba2

interface ObstacleObservable {
    val observers: ArrayList<ObstacleObserver>

    fun add(observer: ObstacleObserver) {
        observers.add(observer);
    }
    fun remove(observer: ObstacleObserver) {
        observers.remove(observer);
    }
    fun hasUpdatedPosition(posX : Float) {
        observers.forEach {it.updatePosition(posX)}
    }
    fun hasUpdatedWidth(size : Float){
        observers.forEach{it.updateWidth(size)}
    }
}