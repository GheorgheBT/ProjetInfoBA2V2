
package com.example.projetinfoba2
/*
import android.content.Context
import android.graphics.RectF
import java.util.Random

class EnsembleObstacle(context: Context, centerX: Float) : ObstacleObservable{

    override val observers: ArrayList<ObstacleObserver> = ArrayList()

    private var obstacleHaut : ObstacleInedstructible = ObstacleInedstructible()
    private var obstacleBas : ObstacleInedstructible = ObstacleInedstructible()
    private var obstacleCentre : ObstacleDestructible = ObstacleDestructible()


    private var posY : Float = 0f
        set(value) {
            field = value
            hasUpdatedPosition(posX)
        }
    private var posX : Float = centerX
        set(value) {
            field = value
            hasUpdatedPosition(value)
        }

    private var vitesseX : Float = context.resources.getFloat(R.dimen.LargeurObstacle)
    private var vitesseY : Float = 0f

    private var dimY : Float = ScreenData.screenHeight
    private var dimX : Float = 100f
        set(value){
            field = value
            hasUpdatedWidth(value)
        }

    private var position = RectF(posX - dimX/2, ScreenData.upScreenSide , posX + dimX/2, ScreenData.screenHeight)

    init {
        add(obstacleHaut)
        add(obstacleBas)
        add(obstacleCentre)
        posY = (ScreenData.upScreenSide.toInt() .. ScreenData.screenHeight.toInt()).random().toFloat()
    }
    fun drawShape(hastTop : Boolean, hasBottom : Boolean, hasCenter : Boolean, centerPosition : Float){
        obstacleHaut.isVisible = hastTop
        obstacleBas.isVisible = hasBottom
        obstacleCentre.isVisible = hasCenter

        obstacleCentre.draw()
    }

    fun updatePosition(){
        if (position.right < ScreenData.leftScreenSide){
            position.right += (ScreenData.screenWidth + position.width())
            position.left += (ScreenData.screenWidth + position.width())
        }

        position.right += vitesseX
        position.left += vitesseY

    }

}*/