package com.example.projetinfoba2

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.SurfaceView
import android.view.WindowManager
import java.util.*

class DrawingView @JvmOverloads constructor (context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0): SurfaceView(context, attributes,defStyleAttr) {

    private var screenWidth = 0f// Largeur de l'écran en pixels
    private var screenHeight = 0f // Hauteur de l'écran en pixels
    private val backgroundImage = BitmapFactory.decodeResource(resources, R.drawable.run_background)
    private val scaledBackgroundImage = Bitmap.createScaledBitmap(backgroundImage, 10000, 1400, true)
    private val backgroundspeed = 5f
    private var backgroundOffset = 0f
    val random = Random()
    val paint = Paint()


    init {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val displayMetrics = DisplayMetrics()
        display.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels.toFloat()
        screenHeight = displayMetrics.heightPixels.toFloat()
    }

    val screenRect = RectF(0f, 0f ,screenWidth, screenHeight )
    val joystick = Joystick(screenHeight / 4f, screenHeight / 1.35f, screenHeight / 14f, screenHeight / 7f)
    val boutonTir = Bouton(context, (screenWidth - 220f), screenHeight / 1.5f, screenHeight / 8f)
    val joueur = Joueur(context, 0f, screenHeight / 2f, screenHeight / 8f)

    private val projectileList = mutableListOf<Projectile>()
    private var projectileToRemove = mutableListOf<Projectile>()

    var ennemiList = mutableListOf<Ennemi>()
    private var ennemiToRemove = mutableListOf<Ennemi>()
    private var lastEnnemiTime = 0L

    var obstacleList = mutableListOf<Obstacle>()
    var obstacleToRemove = mutableListOf<Obstacle>()

    var lastObstacleTime = 0L

    private fun backgroundMove(speed: Float){
        backgroundOffset -= speed//
        if (backgroundOffset < -(scaledBackgroundImage.width - screenWidth)) { //
            backgroundOffset = 0f
        }
    }

    private fun update() {
        joueur.updatePosition(joystick,screenRect)
        for (obstacle in obstacleList){
            obstacle.isOnScreen(screenRect,joueur.joueurPosition)
            if (obstacle.updatePosition()){
                for (projectile in projectileList){
                    projectile.isOnScreen(screenRect,obstacle)
                    if(!projectile.updatePositionBalle()){
                        projectileToRemove.add(projectile)
                        if(!obstacle.obstacleOnScreen) {
                           obstacleToRemove.add(obstacle)
                       }
                    }
                }
            }
            else{obstacleToRemove.add(obstacle)}
         }
        for (ennemi in ennemiList){
            ennemi.isOnScreen(screenRect)
            if (!ennemi.updatePosition()) {
                ennemiToRemove.add(ennemi)
            }
        }
        obstacleList.removeAll(obstacleToRemove)
        obstacleToRemove.clear()
        projectileList.removeAll(projectileToRemove)
        projectileToRemove.clear()
        ennemiList.removeAll(ennemiToRemove)
        ennemiToRemove.clear()
    }

    private fun generateObstacle(){
        var y1 = random.nextInt(screenHeight.toInt())
        lastObstacleTime = System.currentTimeMillis()
        val obstacle = Obstacle(context, screenWidth, y1.toFloat())
        obstacleList.add(obstacle)

    }

    private fun generateEnnemi(){
        lastEnnemiTime = System.currentTimeMillis()
        val ennemi = Ennemi(context, 0f, screenHeight / 2)
        ennemiList.add(ennemi)
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas != null) {
            canvas.drawBitmap(scaledBackgroundImage, backgroundOffset, 0f, null)
            joystick.draw(canvas)
            boutonTir.draw(canvas)
            joueur.draw(canvas, paint, 1000f, 1000f)
            // Dessine tous les obstacles existants
            for (obstacle in obstacleList) {
                obstacle.draw(canvas)
            }
            // Dessine tous les oiseaux existants
            for (ennemi in ennemiList) {
                ennemi.draw(canvas)
            }
            // dessine tous les projectiles existants
            for (projetile in projectileList) {
                projetile.draw(canvas)
            }
        }
        run()
        postInvalidateOnAnimation()
    }

     private fun run(){
         if (System.currentTimeMillis() - lastObstacleTime > 2000) {// Génère des obstacles toutes les 5 secondes
             generateObstacle()
         }
         if (System.currentTimeMillis() - lastEnnemiTime > 100000) {// Génère des oiseaux toutes les 10 secondes
             generateEnnemi()
         }
         backgroundMove(backgroundspeed)
         update()
     }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {

            MotionEvent.ACTION_DOWN -> {
                if (boutonTir.isClicked(x, y)) {
                    var projectile = Projectile(context,joueur.joueurPosition.centerX(),joueur.joueurPosition.centerY(), 0, 50f)
                    projectileList.add(projectile)
                }
                if (joystick.isPressed(x, y)) {
                    joystick.setIsPressed(true)
                }
            }

            MotionEvent.ACTION_UP -> {
                joystick.setIsPressed(false)
                joystick.resetActuator()
                joystick.updateJoystickPosition()
                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {
                if (joystick.getIsPressed()){
                    joystick.setActuator(x,y)
                    joystick.updateJoystickPosition()
                    invalidate()
                }
            }
        }
        return true
        //return super.onTouchEvent(event) // Les events ection_move et action_up ne sont pas appelés si on utilise ce retour (j'ai pas trop compir pourquoi mais bon)
    }
}