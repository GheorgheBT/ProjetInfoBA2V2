package com.example.projetinfoba2

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceView
import android.util.Log

class DrawingView @JvmOverloads constructor (context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0): SurfaceView(context, attributes,defStyleAttr) {
    val ctx = getContext() //donne le contexte de l'activité ou du fragment où on veut dessiner l'oiseau

    private val backgroundImage: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.run_background)
    private val scaledBackgroundImage: Bitmap = Bitmap.createScaledBitmap(backgroundImage, 10000, 1400, true)
    private val backgroundspeed = 5f
    var backgroundOffset = 0f




    val joueur = Joueur(ctx,1000f,1000f)

    val projectileList1 = mutableListOf<Projectile>()
    val projectileList2 = mutableListOf<Projectile>()

    var oiseauList = mutableListOf<Oiseau>()
    var oiseauToRemove = mutableListOf<Oiseau>()
    var lastOiseauTime = 0L

    var obstacleList =  mutableListOf<Obstacle>()
    var obstacleToRemove = mutableListOf<Obstacle>()
    var lastObstacleTime = 0L

    val boutonList = mutableListOf<Bouton>()

    val bouton0 = Bouton(ctx, 100f, 1000f, 0)
    val bouton1 = Bouton(ctx, 500f, 1000f, 1)
    val bouton2 = Bouton(ctx, 300f, 800f, 2)

    val joystick = Joystick(ctx,300f,750f, 80f,190f)

    init {
        boutonList.add(bouton0)
        boutonList.add(bouton1)
        boutonList.add(bouton2)
    }

    fun generateObstacle(x: Float, y: Float) {
        val obstacle = Obstacle(context, x, y)
        obstacleList.add(obstacle)
    }

    fun generateOiseau(x: Float, y: Float) {
        val oiseau = Oiseau(context, x, y)
        oiseauList.add(oiseau)
    }


    override fun onDraw(canvas: Canvas?) {
        //super.onDraw(canvas)
        canvas?.drawColor(Color.BLACK)// Couleur de fond de la Drawingview

        canvas?.drawBitmap(scaledBackgroundImage, backgroundOffset, 0f, null) // deplace l'image d'arriere plan
        backgroundOffset -= backgroundspeed//
        if (backgroundOffset < -7000f) { //
            backgroundOffset = 0f
        }

        if (canvas != null) {

            joystick.draw(canvas)

            joueur.draw(canvas)
            joueur.gravite()
            for (bouton in boutonList){
                bouton.draw(canvas)
            }
            // Génère des obstacles toutes les 5 secondes

            if (System.currentTimeMillis() - lastObstacleTime > 5000) {
                lastObstacleTime = System.currentTimeMillis()
                generateObstacle(3000f, 1200f)
            }

            // Génère des oiseaux toutes les 10 secondes

            if (System.currentTimeMillis() - lastOiseauTime > 10000) {
                lastOiseauTime = System.currentTimeMillis()
                generateOiseau(100f, 100f)
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
                if (oiseau.oiseauposition.left == joueur.joueurPosition.left){
                    val x = oiseau.oiseauposition.left
                    val y = oiseau.oiseauposition.bottom
                    projectileList1.add(Projectile(ctx, x, y,1)) // ajouter une nouvelle balle a la liste des ablles deja existantes
                }
                // Vérifie si un obstacle est sorti de l'écran et le supprime de la liste
                if (oiseau.oiseauposition.left > 5000) {
                    oiseauToRemove.add(oiseau)
                }
            }

            for (projetile  in projectileList1) {
                projetile.draw(canvas)
                projetile.updatePositionOeuf()
            }
            for (projectile in projectileList2) {
                projectile.draw(canvas)
                projectile.updatePositionBalle()
            }


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
                for (i in 0 until boutonList.size)
                    if (boutonList[i].isClicked(x,y)){
                       if (i==0){joueur.marcheArrier()}
                       if(i==1){joueur.marcheAvant()}
                       if (i==2){
                           joueur.saute()}
                    }
                //bird.updatePosition(x, y)
                //if (bird.isClicked(x, y)) { // L'utilisateur a cliqué sur l'oiseau
                //    projectileList1.add(Projectile(ctx, x, y,1)) // ajouter une nouvelle balle a la liste des ablles deja existantes
                //    invalidate() // Actualiser la vue
                //    return true
                //}

                if (joueur.isTouched(x,y)){
                    projectileList2.add(Projectile(ctx,x,y,0))
                    invalidate()
                }
                //else{
                //    bird.updatePosition(x, y) // mettre a jour la poosition de l'oiseau
                //    invalidate() // Actualiser la vue
                //}

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