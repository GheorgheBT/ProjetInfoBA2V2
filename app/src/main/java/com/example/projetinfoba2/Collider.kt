package com.example.projetinfoba2

import android.graphics.RectF
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

interface Collider {
    fun detectCollision(obstacleList : MutableList<Obstacle>){
    }
}