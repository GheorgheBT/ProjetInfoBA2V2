package com.example.projetinfoba2

import android.content.Context
import android.graphics.*
import android.os.SystemClock
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceView
import android.widget.TextView
import java.util.*

class DrawingView @JvmOverloads constructor (context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0): SurfaceView(context, attributes,defStyleAttr) {

    private var drawing= true
    var upadate  = true

    val gameStatus = GameStatus()

    private var screenWidth = 0f// Largeur de l'écran en pixels
    private var screenHeight = 0f // Hauteur de l'écran en pixels
    private val backgroundImage = BitmapFactory.decodeResource(resources, R.drawable.run_background)
    private val scaledBackgroundImage = Bitmap.createScaledBitmap(backgroundImage, 10000, 1500, true)
    private val backgroundspeed = 5f
    private var backgroundOffset = 0f
    val random = Random()
    val paint = Paint()

    val joueur : Joueur
    init {
        screenHeight = ScreenData.setScreenHeight(context)
        screenWidth = ScreenData.setScreenWidth(context)
        joueur = Joueur(context, 0f, screenHeight / 2f, screenHeight / 8f)

        //Ajour d'une relation observateur entre le game status et les objets affectés par la difficulté
        gameStatus.add(joueur)
    }

    private val screenRect = RectF(0f, 0f ,screenWidth, screenHeight)

    private val projectileList = mutableListOf<Projectile>()
    private var projectileToRemove = mutableListOf<Projectile>()

    var ennemiList = mutableListOf<Ennemi>()
    private var ennemiToRemove = mutableListOf<Ennemi>()
    private var lastEnnemiTime = 0L

    var obstacleList = mutableListOf<Obstacle>()
    var obstacleToRemove = mutableListOf<Obstacle>()
    var lastObstacleTime = 0L

    //Variables pour la gesion des fps
    private var prevTime =  0L // Sert a calculer l'intervalle entre chaque frame
    lateinit var fpsLabel : TextView
    var deltas = mutableListOf<Float>() // liste des n fps pour avoir une valeur stable

    // Variables pour la gestion des tirs
    var isShooting = false // determine si le joueur doit tirer
    private var prevShootTimeJoueur = 0L // permet d'utiliser un intervalle de tir

    // Variables pour la gestion des tirs ennemis
    private var prevShootTimeEnnemi = 0L

    private fun backgroundMove(speed: Float){
        backgroundOffset -= speed//
        if (backgroundOffset < -(scaledBackgroundImage.width - screenWidth)) { //
            backgroundOffset = 0f
        }
    }

    private fun update() {
        joueur.detectCollision(obstacleList)
        joueur.updatePosition()
        joueur.updateVie()

        for (projectile in projectileList){
            projectile.getCollision(obstacleList,obstacleToRemove, joueur)
            projectile.updatePosition()
            if (!projectile.isOnScreen){
                projectileToRemove.add(projectile)
            }
        }

        for (obstacle in obstacleList){
            obstacle.updatePosition()
        }
        for (ennemi in ennemiList){
            ennemi.updatePosition()
        }
        getFrameRate()

        if (isShooting){
            addJoueurBullet(joueur.intervalleTir)
        }
        addEnnemiBullet(400)
    }

    fun destroy(){
        obstacleList.removeAll(obstacleToRemove)
        obstacleToRemove.clear()
        projectileList.removeAll(projectileToRemove)
        projectileToRemove.clear()
        ennemiList.removeAll(ennemiToRemove)
        ennemiToRemove.clear()
    }
    private fun generateObstacle(){
        var obstTop = random.nextInt(screenHeight.toInt())
        var obstacle = Obstacle(context, screenWidth, obstTop.toFloat())
        while (obstacle.position.bottom >= screenRect.bottom || obstacle.position.top <= screenRect.top ){ // force les obstacle a s'afficher entierement sur l'écrant
            obstTop = random.nextInt(screenHeight.toInt())
            obstacle = Obstacle(context, screenWidth, obstTop.toFloat())
        }
        obstacleList.add(obstacle)
        lastObstacleTime = System.currentTimeMillis()
    }

    private fun generateEnnemi(){
        lastEnnemiTime = System.currentTimeMillis()
        val ennemi = Ennemi(context, 0f, screenHeight / 20)
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
            for (ennemi in ennemiList) {
                ennemi.draw(canvas)
            }

            // dessine tous les projectiles existants
            for (projetile in projectileList) {
                projetile.draw(canvas)
            }
            postInvalidateOnAnimation()

        }
        run()
    }
    private fun run(){

        backgroundMove(backgroundspeed)
        if (upadate){

            if (System.currentTimeMillis() - lastObstacleTime > 2000) {// Génère des obstacles toutes les 5 secondes
                generateObstacle()
            }
            if (System.currentTimeMillis() - lastEnnemiTime > 5000) {// Génère des oiseaux toutes les 10 secondes
                generateEnnemi()
            }

            update()
            destroy()

        }
       detectEndGame()

    }

    private fun addEnnemiBullet(intervalle : Long){
        val currentShootTime = SystemClock.elapsedRealtime()
        if (currentShootTime - prevShootTimeEnnemi > intervalle) {
            for (ennemi in ennemiList){
                if (joueur.position.left <= ennemi.position.right && joueur.position.right>= ennemi.position.left) {
                    val projectile = ProjectileEnnemi(context, ennemi.position.centerX(), ennemi.position.centerY(),screenHeight / 35f )
                    projectileList.add(projectile)
                }
            }
            prevShootTimeEnnemi = currentShootTime
        }
    }

    private fun addJoueurBullet(intervalle : Long){
        val currentShootTime = SystemClock.elapsedRealtime()
        if (currentShootTime - prevShootTimeJoueur > intervalle) {
            val projectile = ProjectileJoueur(context, joueur.position.centerX(), joueur.position.centerY(), screenHeight / 35f)
            projectileList.add( projectile)
            prevShootTimeJoueur = currentShootTime
        }
    }

    private fun getFrameRate(){
        val currTime = SystemClock.elapsedRealtime()
        val deltaTime =  (currTime - prevTime).toFloat()/1000
        deltas.add(1f/deltaTime)
        if (deltas.size > 8){
            deltas.removeAt(0)
        }
        val average = deltas.average()
        fpsLabel.text = average.toInt().toString()
        prevTime = currTime
    }

    fun detectEndGame() {
        if (joueur.scores.getVie() == 0 ) {
            upadate = false
        }
    }

    fun restartGame() {
        obstacleList.clear()
        ennemiList.clear()
        projectileList.clear()
        joueur.position.top = screenHeight / 2
        joueur.position.left = 0f
        joueur.scores.resetVie()
        joueur.scores.resetScore()
    }
}