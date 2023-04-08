package com.example.projetinfoba2

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
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
        joueur.updatePosition(0f,0f,screenWidth,screenHeight)
        for (obstacle in obstacleList){
            obstacle.isOnScreen(screenRect,joueur.joueurPosition) // le joueur demande à l'obstacle si ils sont en contacte si oui "obstacleOnScreen" passe en false
            if (obstacle.updatePosition()){ // si il n'y pas contacte  l'obstacle se met a jour
                for (projectile in projectileList){
                    projectile.isOnScreen(screenRect,obstacle) // le projectile demande a l'obstacle si ils sont en contacte ou pas si c'est la cas "projectileOnscreen" passe ne false
                    if(!projectile.updatePositionBalle()){ // le cas ou il y'a contacte => la position du projectile refuse de se mettre a jour => soit il a toucher un obstacle soit il est hors du screen
                        projectileToRemove.add(projectile)
                        if(!obstacle.obstacleOnScreen) { //le cas ou le projectile a touche l'obstacle
                           obstacleToRemove.add(obstacle)
                            println("ok1")
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
            joueur.draw(canvas, paint, 1000f, 1000f)
            // Dessine tous les obstacles existants
            for (obstacle in obstacleList) {
                obstacle.draw(canvas)
            }
            // Dessine tous les oiseaux existants
           //for (ennemi in ennemiList) {
           //     ennemi.draw(canvas)
            //}
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


    fun addBullet(){
        projectileList.add(
            Projectile(
                context,
                joueur.joueurPosition.centerX(),
                joueur.joueurPosition.centerY(),
                0,
                50f
            )
        )
    }
}