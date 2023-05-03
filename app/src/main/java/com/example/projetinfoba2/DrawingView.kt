package com.example.projetinfoba2

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.os.SystemClock
import android.util.AttributeSet
import android.view.SurfaceView

class DrawingView @JvmOverloads constructor (context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0): SurfaceView(context, attributes,defStyleAttr), Observer {

    var endGameAlertDialog: AlertDialog? = null
    private var update  = true

    //Création d'un objet gamestatus pour relier le changement de difficulté aux objets
    val gameStatus = GameStatus()

    //Dimensions de l'écran en pixels
    private var screenWidth = 0f// Largeur de l'écran en pixels
    private var screenHeight = 0f // Hauteur de l'écran en pixels

    //Caractérisation de l'image de fond
    private val backgroundImage = BitmapFactory.decodeResource(resources, R.drawable.run_bg_1)
    private var scaledBackgroundImage = Bitmap.createScaledBitmap(backgroundImage, 10000, 1500, true)
    private val backgroundspeed = 2f
    private var backgroundOffset = 0f

    private val paint = Paint()

    //Création d'une valeur joueur
    val joueur : Joueur


    //Liste des obstacles
    private var obstacleList = mutableListOf<Obstacle>()

    //Liste des ennemis
    private var ennemiList = mutableListOf<Ennemi>()

    //Variables pour la gestion des projectiles
    private val projectileList = mutableListOf<Projectile>()
    private var projectileToRemove = mutableListOf<Projectile>()


    init {
        //Ajout d'une relation observateur entre le game status et le rawingview
        gameStatus.add(this)

        //Initialisation des dimensions de l'ecran
        screenHeight = ScreenData.setScreenHeight(context)
        screenWidth = ScreenData.setScreenWidth(context)

        //Initialisation du fond d'ecran
        scaledBackgroundImage = Bitmap.createScaledBitmap(backgroundImage,
            ((screenHeight/backgroundImage.height)*backgroundImage.width).toInt(),
            screenHeight.toInt(), true)

        // Initialisation du joueur
        joueur = Joueur(context,  screenHeight / 8f)
        //Ajout d'une relation observateur entre le game status et les objets affectés par la difficulté
        gameStatus.add(joueur)

        //Initialisation des obstacles
        initObstacles(7)

        initEnnemies(2)
    }


    // Variables pour la gestion des tirs du joueyr
    var isShooting = false // determine si le joueur doit tirer
    private var prevShootTimeJoueur = 0L // permet d'utiliser un intervalle de tir
    var joueurBulletSize = screenHeight / 35f

    // Variables pour la gestion des tirs ennemis
    private var prevShootTimeEnnemi = 0L
    var ennemiBulletSize = screenHeight / 30f
    var intervalleTirEnnemi : Long = 400L



    private fun backgroundMove(speed: Float){
        backgroundOffset -= speed//
        if (backgroundOffset < -(scaledBackgroundImage.width - screenWidth)) { //
            backgroundOffset = ScreenData.leftScreenSide
        }
    }

    private fun update() {
        joueur.detectCollision(obstacleList)
        joueur.updatePosition()
        joueur.updateVie()

        for (projectile in projectileList){
            projectile.getCollision(obstacleList, joueur)
            projectile.updatePosition()
            if (!projectile.isOnScreen){
                projectileToRemove.add(projectile)
            }
        }

        for (obstacle in obstacleList){
            obstacle.updatePosition(obstacleList, joueur)
        }

        for (ennemi in ennemiList){
            ennemi.updatePosition()
        }

        if (isShooting){
            addJoueurBullet(joueur.intervalleTir, joueurBulletSize)
        }
        addEnnemiBullet( intervalleTirEnnemi, ennemiBulletSize)
    }

    private fun destroy(){
        projectileList.removeAll(projectileToRemove)
        projectileToRemove.clear()
        // garbage collector
        System.gc()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {

            canvas.drawBitmap(scaledBackgroundImage, backgroundOffset, ScreenData.upScreenSide, null)
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
        if (update){
            backgroundMove(backgroundspeed)
            update()
            destroy()
        }
       detectEndGame()
    }

    private fun addEnnemiBullet(intervalle : Long , size : Float){
        val currentShootTime = SystemClock.elapsedRealtime()
        if (currentShootTime - prevShootTimeEnnemi > intervalle) {
            for (ennemi in ennemiList){
                if (joueur.position.left <= ennemi.position.right && joueur.position.right>= ennemi.position.left) {
                    val projectile = ProjectileEnnemi(context, ennemi.position.centerX(), ennemi.position.centerY(),size)
                    projectileList.add(projectile)
                }
            }
            prevShootTimeEnnemi = currentShootTime
        }
    }

    private fun addJoueurBullet(intervalle : Long ,size: Float){
        val currentShootTime = SystemClock.elapsedRealtime()
        if (currentShootTime - prevShootTimeJoueur > intervalle) {
            val projectile = ProjectileJoueur(context, joueur.position.centerX(), joueur.position.centerY(),size )
            projectileList.add( projectile)
            prevShootTimeJoueur = currentShootTime
        }
    }

    private fun initObstacles(nombre : Int){
        val pas = (screenWidth/nombre).toInt()
        for (i in screenWidth.toInt() .. 2*screenWidth.toInt() step pas){

            val borneSup = ScreenData.upScreenSide.toInt() + context.resources.getString(R.string.LongueurEnnemi).toFloat().toInt()
            val borneInf = screenHeight.toInt()
            val posY = (borneSup .. borneInf).random().toFloat()
            val obs = Obstacle(context, i.toFloat(), posY)
            obstacleList.add(obs)
            gameStatus.add(obs)
        }
    }

    private fun initEnnemies(nombre : Int){
        val pas = (screenWidth/nombre).toInt()
        for (i in 0 until nombre){
            val posX = (i * pas).toFloat() - screenWidth
            val ennemi = Ennemi(context, posX, ScreenData.upScreenSide + context.resources.getString(R.string.LongueurEnnemi).toFloat())
            gameStatus.add(ennemi)
            ennemiList.add(ennemi)

        }
    }

    private fun detectEndGame() {
        if (joueur.scores.isDead()) {
            update = false
            if (endGameAlertDialog == null){
                showScoreDialog()
            }
        }
    }

    private fun showScoreDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Score")
        builder.setMessage("Votre score est de ${joueur.scores.getScore()}")
        builder.setPositiveButton("Nouvelle partie") { _: DialogInterface, _: Int ->
            restartGame()
            update = true
            endGameAlertDialog?.dismiss()
            endGameAlertDialog = null
        }
        builder.setNegativeButton("Quitter ") { _: DialogInterface, _: Int ->
            endGameAlertDialog?.dismiss()
            (context as MainActivity).finish()
        }
        endGameAlertDialog = builder.create()
        endGameAlertDialog?.setCanceledOnTouchOutside(false)
        endGameAlertDialog?.show()
    }

    private fun restartGame() {
        for (obstacle in obstacleList){
            obstacle.resetObstacle()
        }
        for (ennemi in ennemiList){
            ennemi.resetPosition()
        }
        projectileList.clear()
        joueur.reset()
        joueur.scores.resetVie()
        joueur.scores.resetScore()
    }

    override fun updateDifficulty(diff: Int) {
        when(diff){
            1->{intervalleTirEnnemi = context.resources.getString(R.string.IntervalleTirEnnemiEasy).toLong()}
            2->{intervalleTirEnnemi = context.resources.getString(R.string.IntervalleTirEnnemiMedium).toLong()}
            3->{intervalleTirEnnemi = context.resources.getString(R.string.IntervalleTirEnnemiHard).toLong()}
        }
    }
}