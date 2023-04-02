package com.example.projetinfoba2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.SurfaceView
import android.view.WindowManager

class DrawingView @JvmOverloads constructor (context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0): SurfaceView(context, attributes,defStyleAttr) {
    val ctx = getContext() //donne le contexte de l'activité ou du fragment où on veut dessiner
    var screenWidth = 0f// Largeur de l'écran en pixels
    var screenHeight = 0f // Hauteur de l'écran en pixels
    private val backgroundImage: Bitmap =
        BitmapFactory.decodeResource(resources, R.drawable.run_background)
    private val scaledBackgroundImage: Bitmap =
        Bitmap.createScaledBitmap(backgroundImage, 10000, 1400, true)
    private val backgroundspeed = 5f
    var backgroundOffset = 0f

    init {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val displayMetrics = DisplayMetrics()
        display.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels.toFloat()
        screenHeight = displayMetrics.heightPixels.toFloat()
    }


    val joueur = Joueur(ctx, 0f, screenHeight / 2)

    val projectileList = mutableListOf<Projectile>()
    val projectileToRemove = mutableListOf<Projectile>()

    var oiseauList = mutableListOf<Oiseau>()
    var oiseauToRemove = mutableListOf<Oiseau>()
    var lastOiseauTime = 0L

    var obstacleList = mutableListOf<Obstacle>()
    var obstacleToRemove = mutableListOf<Obstacle>()
    var lastObstacleTime = 0L

    val boutonTir = Bouton(ctx, 1500f, 800f, 0)

    val joystick = Joystick(300f,750f, 80f,190f)



    override fun onDraw(canvas: Canvas?) {

        // super.onDraw(canvas)

        canvas?.drawColor(Color.BLACK)// Couleur de fond de la Drawingview

        canvas?.drawBitmap(
            scaledBackgroundImage,
            backgroundOffset,
            0f,
            null
        ) // deplace l'image d'arriere plan
        backgroundOffset -= backgroundspeed//
        if (backgroundOffset < -7000f) { //
            backgroundOffset = 0f
        }

        if (canvas != null) {

            joystick.draw(canvas)
            joueur.draw(canvas)

            // Génère des obstacles toutes les 5 secondes
            if (System.currentTimeMillis() - lastObstacleTime > 5000) {
                lastObstacleTime = System.currentTimeMillis()
                val obstacle = Obstacle(context, x, y)
                obstacleList.add(obstacle)
            }

            // Génère des oiseaux toutes les 10 secondes

            if (System.currentTimeMillis() - lastOiseauTime > 10000) {
                lastOiseauTime = System.currentTimeMillis()
                val oiseau = Oiseau(context, x, y)
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
                if (oiseau.oiseauposition.left == joueur.joueurPosition.left) {
                    val x = oiseau.oiseauposition.left
                    val y = oiseau.oiseauposition.bottom
                    projectileList.add(
                        Projectile(
                            ctx,
                            x,
                            y,
                            1
                        )
                    ) // ajouter une nouvelle balle a la liste des ablles deja existantes
                }
                // Vérifie si un obstacle est sorti de l'écran et le supprime de la liste
                if (oiseau.oiseauposition.left > 5000) {
                    oiseauToRemove.add(oiseau)
                }
            }

            for (projetile in projectileList) {
                projetile.draw(canvas)
                projetile.updatePositionBalle()
            }



            oiseauList.removeAll(oiseauToRemove)
            oiseauToRemove.clear()

            obstacleList.removeAll(obstacleToRemove)
            obstacleToRemove.clear()

            joueur.updatePosition(joystick)
        }
        // Planifier la prochaine mise à jour
        postInvalidateOnAnimation()
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

                if (joueur.isTouched(x,y)){
                    projectileList.add(Projectile(ctx, x, y, 0))
                    invalidate()
                }

                if(joystick.isPressed(x,y)){
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