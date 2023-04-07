package com.example.projetinfoba2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
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
    private val scaledBackgroundImage =
        Bitmap.createScaledBitmap(backgroundImage, 10000, 1400, true)
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


    val joystick =
        Joystick(screenHeight / 4f, screenHeight / 1.35f, screenHeight / 14f, screenHeight / 7f)

    val boutonTir = Bouton(context, (screenWidth - 220f), screenHeight / 1.5f, screenHeight / 8f)

    val joueur = Joueur(context, 0f, screenHeight / 2f, screenHeight / 8f)

    private val projectileList = mutableListOf<Projectile>()
    private val projectileToRemove = mutableListOf<Projectile>()

    private var oiseauList = mutableListOf<Oiseau>()
    private var oiseauToRemove = mutableListOf<Oiseau>()
    private var lastOiseauTime = 0L

    var obstacleList = mutableListOf<Obstacle>()
    var obstacleToRemove = mutableListOf<Obstacle>()
    var lastObstacleTime = 0L

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawBitmap(
            scaledBackgroundImage,
            backgroundOffset,
            0f,
            null
        ) // deplace l'image d'arriere plan
        backgroundOffset -= backgroundspeed//
        if (backgroundOffset < -(scaledBackgroundImage.width - screenWidth)) { //
            backgroundOffset = 0f
        }

        if (canvas != null) {
            joystick.draw(canvas)
            boutonTir.draw(canvas)
            joueur.draw(canvas, paint, 1000f, 1000f)

            // Génère des obstacles toutes les 5 secondes
            if (System.currentTimeMillis() - lastObstacleTime > 2000) {
                var y1 = random.nextInt(screenHeight.toInt())
                lastObstacleTime = System.currentTimeMillis()
                val obstacle = Obstacle(context, screenWidth, y1.toFloat())
                obstacleList.add(obstacle)
            }

            // Génère des oiseaux toutes les 10 secondes

            if (System.currentTimeMillis() - lastOiseauTime > 100000) {
                lastOiseauTime = System.currentTimeMillis()
                val oiseau = Oiseau(context, 0f, screenHeight / 2)
                oiseauList.add(oiseau)
            }

            // Dessine tous les obstacles existants
            for (obstacle in obstacleList) {
                obstacle.draw(canvas)
                obstacle.updatePosition()
                // Vérifie si un obstacle est sorti de l'écran et le supprime de la liste
                if (obstacle.obstaclePosition.right < 0) {
                    obstacleToRemove.add(obstacle)
                }
            }
            // Dessine tous les oiseaux existants
            for (oiseau in oiseauList) {
                oiseau.draw(canvas)
                oiseau.updatePosition()

                //fais tomber un oeuf si l'oiseau est au dessus du joueur
                //if (oiseau.oiseauposition.left == joueur.joueurPosition.left) {
                //    val x = oiseau.oiseauposition.left
                //    val y = oiseau.oiseauposition.bottom
                //    val projectile = Projectile(context, x, y, 1,200f)
                //    projectileList.add(projectile) // ajouter une nouvelle balle a la liste des ablles deja existantes
                //}

                // Vérifie si un oiseau est sorti de l'écran et le supprime de la liste
                if (oiseau.oiseauposition.left > screenWidth) {
                    oiseauToRemove.add(oiseau)
                }
            }

            for (projetile in projectileList) {
                projetile.draw(canvas)
                projetile.updatePositionBalle()

                if (projetile.projectilePosition.left > screenWidth) {
                    projectileToRemove.add(projetile)
                }
            }

            joueur.updatePosition(joystick)

            ///////////////////////////////////////////////cette partie sera rajoutée dans un thread plus tar
            for (obstacle in obstacleList) {
                if (obstacle.isTouched(
                        joueur.joueurPosition.centerX(),
                        joueur.joueurPosition.centerY()
                    )
                ) {
                    joueur.joueurVie -= 1
                    obstacleToRemove.add(obstacle)
                }
                for (projectile in projectileList) {


                    if (obstacle.isTouched(
                            projectile.projectilePosition.centerX(),
                            projectile.projectilePosition.centerY()
                        )
                    ) {
                        obstacleToRemove.add(obstacle)
                        projectileToRemove.add(projectile)
                        println(true)
                    }

                }
            }
            ////////////////////////////////////////////////////////////////////////////////

            //println(projectileList)
            //println(projectileToRemove)

            projectileList.removeAll(projectileToRemove)
            projectileToRemove.clear()

            oiseauList.removeAll(oiseauToRemove)
            oiseauToRemove.clear()

            obstacleList.removeAll(obstacleToRemove)
            obstacleToRemove.clear()
        }
        // Planifier la prochaine mise à jour
        postInvalidateOnAnimation()
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (boutonTir.isClicked(x, y)) {
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