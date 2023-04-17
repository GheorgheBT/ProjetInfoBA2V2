package com.example.projetinfoba2

import android.app.AlertDialog
import android.content.Context
import android.graphics.*
import android.os.SystemClock
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.SurfaceView
import android.view.WindowManager
import android.widget.TextView
import java.util.*

class DrawingView @JvmOverloads constructor (context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0): SurfaceView(context, attributes,defStyleAttr) {

    var drawing = true



    private var screenWidth = 0f// Largeur de l'écran en pixels
    private var screenHeight = 0f // Hauteur de l'écran en pixels
    private val backgroundImage = BitmapFactory.decodeResource(resources, R.drawable.run_background)
    private val scaledBackgroundImage = Bitmap.createScaledBitmap(backgroundImage, 10000, 1500, true)
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

    private val screenRect = RectF(0f, 0f ,screenWidth, screenHeight)
    val joueur = Joueur(context, 0f, screenHeight / 2f, screenHeight / 8f)

    private val projectileList = mutableListOf<Projectile>()
    private var projectileToRemove = mutableListOf<Projectile>()

    var ennemiList = mutableListOf<Ennemi>()
    private var ennemiToRemove = mutableListOf<Ennemi>()
    private var lastEnnemiTime = 0L

    var obstacleList = mutableListOf<Obstacle>()
    var obstacleToRemove = mutableListOf<Obstacle>()
    var lastObstacleTime = 0L

    //Variables pour la gesion des fps
    private var prevTime : Long = 0 // Sert a calculer l'intervalle entre chaque frame
    lateinit var fpsLabel : TextView
    var deltas = mutableListOf<Float>() // liste des n fps pour avoir une valeur stable

    // Variables pour la gestion des tirs
    var isShooting = false // determine si le joueur doit tirer
    private var prevShootTime : Long = 0 // permet d'utiliser un intervalle de tir


    private fun backgroundMove(speed: Float){
        backgroundOffset -= speed//
        if (backgroundOffset < -(scaledBackgroundImage.width - screenWidth)) { //
            backgroundOffset = 0f
        }
    }

    private fun update() {
        joueur.getCollisionSide(obstacleList)
        joueur.updatePosition(screenRect)


        for (projectile in projectileList){
            projectile.getCollision(obstacleList,obstacleToRemove)
            projectile.updatePositionBalle(screenRect)
            if (!projectile.isOnScreen){
                projectileToRemove.add(projectile)
            }
        }

        for (obstacle in obstacleList){
            obstacle.updatePosition(screenRect)
        }
        for (ennemi in ennemiList){
            ennemi.isOnScreen(screenRect)
           if (!ennemi.updatePosition()) {
               ennemiToRemove.add(ennemi)
           }
        }
        getFrameRate()
        if (isShooting){
            addBullet(400)
        }
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
        while (obstacle.Position.bottom >= screenRect.bottom || obstacle.Position.top <= screenRect.top ){ // force les obstacle a s'afficher entierement sur l'écrant
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
        if (canvas != null && drawing ) {
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
    fun run(){
        if (System.currentTimeMillis() - lastObstacleTime > 2000) {// Génère des obstacles toutes les 5 secondes
            generateObstacle()
        }
        if (System.currentTimeMillis() - lastEnnemiTime > 100000) {// Génère des oiseaux toutes les 10 secondes
            generateEnnemi()
        }
        backgroundMove(backgroundspeed)
        update()
        destroy()
        detectEndGame()
    }

    private fun addBullet(intervalle : Long){
        val currentShootTime = SystemClock.elapsedRealtime()
        if (currentShootTime - prevShootTime > intervalle) {
            projectileList.add(
                Projectile(
                    context,
                    joueur.Position.centerX(),
                    joueur.Position.centerY(),
                    0,
                    screenHeight / 35f
                )
            )
            prevShootTime = currentShootTime
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

    private fun showScoreDialog(score: Int, onRestartGame: () -> Unit) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Score")
        builder.setMessage("Votre score est de $score")

        builder.setPositiveButton("Nouvelle partie") { dialog, which ->
            onRestartGame()
        }

        builder.setNegativeButton("Quitter") { dialog, which ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    fun endGame(score: Int) {
        showScoreDialog(score) {
            println("ok")
            // redémarrer le jeu ici
        }
    }

    //  détecte la fin du jeu
    fun detectEndGame() {
        if (joueur.Vie == 0  ) {
            drawing = false
            endGame(joueur.Point) // afficher la boîte de dialogue
        }
    }



}