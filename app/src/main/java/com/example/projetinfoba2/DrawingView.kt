package com.example.projetinfoba2

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.media.MediaPlayer
import android.os.SystemClock
import android.util.AttributeSet
import android.view.SurfaceView

class DrawingView @JvmOverloads constructor (context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0): SurfaceView(context, attributes,defStyleAttr), Observer {

    private var endGameAlertDialog: AlertDialog? = null
    private var update  = true



    //Création d'un objet gamestatus pour relier le changement de difficulté aux objets
    val gameStatus = GameStatus()

    //Dimensions de l'écran en pixels
    private var screenWidth = 0f// Largeur de l'écran en pixels
    private var screenHeight = 0f // Hauteur de l'écran en pixels

    //Caractérisation de l'image de fond
    private val backgroundImage = BitmapFactory.decodeResource(resources, R.drawable.run_background)
    private var scaledBackgroundImage = Bitmap.createScaledBitmap(backgroundImage, 10000, 1500, true)
    private var backgroundspeed = 2f
    private var backgroundOffset = 0f

    //Création d'une valeur joueur
    val joueur : Joueur

    //Liste des obstacles
    private var obstacleList = mutableListOf<Obstacle>()

    //Liste des ennemis
    private var ennemiList = mutableListOf<Ennemi>()

    //Variables pour la gestion des projectiles
    private val projectileList = mutableListOf<Projectile>()
    private var projectileToRemove = mutableListOf<Projectile>()

    //liste pour la gestion des songs
    var mediaList = mutableListOf<MediaPlayer>()

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


    // Variables pour la gestion des tirs du joueur
    var isShooting = false // determine si le joueur doit tirer
    private var prevShootTimeJoueur = 0L // permet d'utiliser un intervalle de tir
    private var joueurBulletSize = screenHeight / 35f

    // Variables pour la gestion des tirs ennemis
    private var prevShootTimeEnnemi = 0L
    private var ennemiBulletSize = screenHeight / 30f
    private var intervalleTirEnnemi : Long = 400L



    private fun backgroundMove(speed: Float, canvas: Canvas?){
        backgroundOffset -= speed//
        if (backgroundOffset < -(scaledBackgroundImage.width/2)) { //
            backgroundOffset = ScreenData.leftScreenSide
        }
        canvas?.drawBitmap(scaledBackgroundImage, backgroundOffset, ScreenData.upScreenSide, null)
    }

    private fun updateObjects() {
        //Mise à jour des obstacles
        for (obstacle in obstacleList){
            obstacle.updatePosition(obstacleList, joueur)
        }

        //Mise à jour des ennemis
        for (ennemi in ennemiList){
            ennemi.updatePosition()
        }

        //Ajour de nouvelles balles
        if (isShooting){
            addJoueurBullet(joueur.intervalleTir, joueurBulletSize)
        }
        addEnnemiBullet( intervalleTirEnnemi, ennemiBulletSize)

        //Mise à jour des projectiles
        for (projectile in projectileList){
            when(projectile){
                is ProjectileJoueur ->{projectile.onCollision(joueur.scores , mediaList) }
                is ProjectileEnnemi ->{projectile.onCollision(null , mediaList)
                }
            }
            projectile.updatePosition()

            if (!projectile.isOnScreen){
                projectileToRemove.add(projectile)
            }
        }


        // Mise à jour du joueur
        joueur.onCollision(joueur.scores , mediaList)
        joueur.updatePosition()
        joueur.updateVie()

        destroyProjectiles()
    }

    private fun destroyProjectiles(){
        projectileList.removeAll(projectileToRemove)
        projectileToRemove.clear()
        // garbage collector
        System.gc()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {
            if(update) {
                //Mouvement du plan arrière
                backgroundMove(backgroundspeed, canvas)
                //Mise à jour des objets
                updateObjects()
            }

            //Addition des sons
            mediaPlayer()

            //Dessin des objets
            drawObjects(canvas)

            //Detection du fin de jeu
            detectEndGame()

            //Activation de la mise à jour des images
            postInvalidateOnAnimation()
        }
    }

    private fun drawObjects(canvas: Canvas){
        //Dessine le joueur
        joueur.draw(canvas)

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
    private fun addEnnemiBullet(intervalle : Long , size : Float){
        val currentShootTime = SystemClock.elapsedRealtime()
        if (currentShootTime - prevShootTimeEnnemi > intervalle) {
            for (ennemi in ennemiList){
                if (joueur.position.left <= ennemi.position.right && joueur.position.right>= ennemi.position.left) {
                    val projectile = ProjectileEnnemi(context, ennemi.position.centerX(), ennemi.position.centerY(),size)
                    projectileList.add(projectile)
                    projectile.add(joueur)
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

            for(obs in obstacleList)
                projectile.add(obs)

            prevShootTimeJoueur = currentShootTime
        }
    }

    private fun mediaPlayer(){
        for (media in mediaList){
            media.setOnCompletionListener {
                media.release()
            }
        }
    }

    private fun initObstacles(nombre : Int){
        //Instantiation des obstacles avec une certaine distance entre eux
        val pas = (screenWidth/nombre).toInt()
        for (i in screenWidth.toInt() .. 2*screenWidth.toInt() step pas){

            val borneSup = ScreenData.upScreenSide.toInt() + context.resources.getString(R.string.LongueurEnnemi).toFloat().toInt()
            val borneInf = screenHeight.toInt()
            val posY = (borneSup .. borneInf).random().toFloat()
            val obs = Obstacle(context, i.toFloat(), posY)

            //Addition des relations d'observer
            gameStatus.add(obs)
            //Addition des relations de collision
            joueur.add(obs)

            obstacleList.add(obs)

        }
    }

    private fun initEnnemies(nombre : Int){
        //Instantiation des ennemis avec une certaine distance entre eux
        val pas = (screenWidth/nombre).toInt()
        for (i in 0 until nombre){
            val posX = (i * pas).toFloat() - screenWidth
            val ennemi = Ennemi(context, posX, ScreenData.upScreenSide + context.resources.getString(R.string.LongueurEnnemi).toFloat())

            //Addition des relations d'observer
            gameStatus.add(ennemi)
            //Addition des relations de collision
            joueur.add(ennemi)

            ennemiList.add(ennemi)
        }
    }

    private fun detectEndGame() {
        //Détection du GameOver
        if (joueur.scores.isDead()) {
            update = false
            if (endGameAlertDialog == null){
                showScoreDialog()
            }
        }
    }

    private fun showScoreDialog() {
        //Ajout de la fenetre de GameOver
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Score")
        builder.setMessage("Votre score est de ${joueur.scores.getScore()}")
        builder.setPositiveButton("Nouvelle partie") { _: DialogInterface, _: Int ->
            endGame()
            //Recommencer l'application
            endGameAlertDialog?.dismiss()
            (context as MainActivity).finish()
        }
        builder.setNegativeButton("Quitter ") { _: DialogInterface, _: Int ->
            // Fermer l'application
            endGame()
            endGameAlertDialog?.dismiss()
            (context as MainActivity).closeAll()
        }
        endGameAlertDialog = builder.create()
        endGameAlertDialog?.setCanceledOnTouchOutside(false)
        endGameAlertDialog?.show()
    }

    private fun endGame() {
        //Réinitialisation des données du jeu
        endGameAlertDialog = null
        update = true
        obstacleList.clear()
        ennemiList.clear()
        projectileList.clear()
        joueur.reset()
        joueur.scores.resetVie()
        joueur.scores.resetScore()
        for (media in mediaList){
            media.release()
        }
    }

    override fun updateParameters(diff: Int) {
        //Changement des paramètres en fonction de la difficulté
        when(diff){
            1->{intervalleTirEnnemi = context.resources.getString(R.string.IntervalleTirEnnemiEasy).toLong()
                backgroundspeed = context.resources.getString(R.string.BackgroundSpeedEasy).toFloat()}
            2->{intervalleTirEnnemi = context.resources.getString(R.string.IntervalleTirEnnemiMedium).toLong()
                backgroundspeed = context.resources.getString(R.string.BackgroundSpeedMedium).toFloat() }
            3->{intervalleTirEnnemi = context.resources.getString(R.string.IntervalleTirEnnemiHard).toLong()
                backgroundspeed = context.resources.getString(R.string.BackgroundSpeedHard).toFloat() }
        }
    }
}