package com.example.projetinfoba2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

class Obstacle ( context: Context,  x: Float, y: Float) : Observer, MoveRandomly{

    //Images des obstacles
    private val listeObstacleImage = intArrayOf(
        R.drawable.ateroide1,
        R.drawable.asteroide2,
        R.drawable.asteroide3,
        R.drawable.ateroide4,
        R.drawable.asteroide5
    )

    //Images des obstacles destructibles
    private val listeObstacleDestructibleImage = intArrayOf(
        R.drawable.ateroide_or1,
        R.drawable.asteroide_or2,
        R.drawable.asteroide_or3,
        R.drawable.ateroide_or4,
        R.drawable.asteroide_or5
    )

    // position de l'obstacle (dans un RectF)
    var position = RectF()

    //Aspect de l'obstacle
    private lateinit var image : Bitmap

    // vitesse de l'obstacle
    var vitesseX = -5f
    private var multiplicateurVitesse = 1f

    //Dimensions de l'obstacle
    private var height : Float = ScreenData.screenHeight/20
    private var width : Float = height
    //Proprieté de destructibilité
    var isDestructible = false

    //Proprieté d'affichage
    var isOnScreen = true

    // Probalibité que l'obsatcle soit destructible
    private var probaDest : Float = 0f

    private val ctx : Context = context


    init {
        //Definit la destructibilité de l'obstacle
        setDestructibility()
        //Definit l'image
        setImage()
        //Definit le rectangle de la position
        position = RectF(x, y, x + width, y + height)
    }
    fun resetObstacle(){
        //
        //Deplacement de l'obstacle a droite de l'ecran
        position.left += ScreenData.screenWidth + width
        position.right += ScreenData.screenWidth + width

        //Randomisation des dimensions
        width = ((ScreenData.screenHeight/30).toInt() ..(ScreenData.screenHeight/20).toInt()).random().toFloat()
        height = width

        //Randomisation du caractère destructible
        setDestructibility()

        //Réinitialisation de la visibilité
        isOnScreen = true

        //Mise à jour de l'image
        setImage()

        //Randomisation de la position verticale
        val borneSup = ScreenData.upScreenSide.toInt() + height.toInt() + ctx.resources.getString(R.string.LongueurEnnemi).toFloat().toInt()
        val borneInf = ScreenData.screenHeight.toInt() - height.toInt()
        val yPos = (borneSup .. borneInf).random().toFloat()

        // Assignation des nouvelles dimensions/positions
        position = RectF(position.left, yPos - height/2,position.left + width, yPos + height/2)
    }

    private fun setDestructibility(){
        // Definit la destructibilité en fonction d'une probabilité entre 0 et 1
        val valeur = Random.nextFloat()
        isDestructible = valeur < probaDest
    }
    private fun setImage(){
        // Choix de l'image en fonction des caractéristiques de l'obstacle
        image = when(isDestructible){
            false -> {
                val index = Random.nextInt(listeObstacleImage.size)
                BitmapFactory.decodeResource(ctx.resources, listeObstacleImage[index])
            }
            true ->{
                val index = Random.nextInt(listeObstacleDestructibleImage.size)
                BitmapFactory.decodeResource(ctx.resources, listeObstacleDestructibleImage[index])
            }
        }
    }
    fun draw(canvas: Canvas) {
        //Dessine l'obstacle dans le rectangle defini auparavant
        if (isOnScreen) {
            canvas.drawBitmap(image, null, position, null)
        }
    }

    override fun updatePosition(obstacleList: MutableList<Obstacle>, joueur: Joueur){
        //Detection si l'obstacle sort de l'ecran
        if(position.right < ScreenData.leftScreenSide){
            joueur.scores.updateScore(10)
            resetObstacle()
            detectIncreaseSpeed(obstacleList)
        }
        //Translation de l'obstacle à gauche
        position.left += vitesseX * multiplicateurVitesse
        position.right += vitesseX * multiplicateurVitesse
    }

    private fun detectIncreaseSpeed(obstacleList : MutableList<Obstacle>){
        var iSpeed = true
        val thisObject = this // On reférencie cet objet pour l'utiliser dans la coroutine
        runBlocking {//On bloque le thread principal pendand l'execution de la boucle
            for (obs in obstacleList) {
                launch { // lancement des coroutines
                    if (obs != thisObject && (obs.position.bottom > position.top && obs.position.top < position.bottom)) { // Detection si les autres obstacles se trouvent sur la trajectoire de celui-ci
                        iSpeed = false
                    }
                }
            }
        }
        multiplicateurVitesse = if (iSpeed){ Random.nextDouble(1.0, 2.0).toFloat() } else { 1f }
    }

    override fun updateDifficulty(diff: Int) {
        //Changement des paramètres des obstacles en fonction de la difficulté du jeu
        when(diff){
            1 -> {
                vitesseX = ctx.resources.getString(R.string.VitesseObstacleEasy).toFloat()
                probaDest = ctx.resources.getString(R.string.ProbaDestructibleEasy).toFloat()
            }
            2 -> {
                vitesseX = ctx.resources.getString(R.string.VitesseObstacleMedium).toFloat()
                probaDest = ctx.resources.getString(R.string.ProbaDestructibleMedium).toFloat()
            }
            3 -> {
                vitesseX = ctx.resources.getString(R.string.VitesseObstacleHard).toFloat()
                probaDest = ctx.resources.getString(R.string.ProbaDestructibleHard).toFloat()
            }
        }
    }
}
