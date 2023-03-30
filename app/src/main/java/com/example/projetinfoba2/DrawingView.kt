package com.example.projetinfoba2

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceView

class DrawingView @JvmOverloads constructor (context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0): SurfaceView(context, attributes,defStyleAttr) {
    val ctx = getContext() //donne le contexte de l'activité ou du fragment où on veut dessiner l'oiseau

    private val backgroundImage: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.run_background)
    private val scaledBackgroundImage: Bitmap = Bitmap.createScaledBitmap(backgroundImage, 10000, 1400, true)
    private val backgroundspeed = 5f
    var backgroundOffset = 0f


    val bouton0 = Bouton(ctx , 100f,1000f,0)
    val bouton1 = Bouton(ctx , 500f,1000f,1)
    val bird = Oiseau(ctx ,100f,100f)
    val joueur = Joueur(ctx,1000f,1000f)
    val projectileList1 = mutableListOf<Projectile>()
    val projectileList2 = mutableListOf<Projectile>()

    override fun onDraw(canvas: Canvas?) {
        //super.onDraw(canvas)
        canvas?.drawColor(Color.BLACK)// Couleur de fond de la Drawingview

        canvas?.drawBitmap(scaledBackgroundImage, backgroundOffset, 0f, null) // deplace l'image d'arriere plan
        backgroundOffset -= backgroundspeed//
        if (backgroundOffset < -7000f) { //
            backgroundOffset = 0f
        }
        if (canvas != null) {
            bouton0.draw(canvas)
            bouton1.draw(canvas)
            bird.draw(canvas)
            joueur.draw(canvas)
            for (projetile  in projectileList1) {
                projetile.draw(canvas)
                projetile.updatePositionOeuf()
            }
            for (projectile in projectileList2) {
                projectile.draw(canvas)
                projectile.updatePositionBalle()
            }
            bird.updatePosition()
        }
        // Planifier la prochaine mise à jour
        postInvalidateOnAnimation()
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                //bird.updatePosition(x, y)
                if (bird.isClicked(x, y)) { // L'utilisateur a cliqué sur l'oiseau
                    projectileList1.add(Projectile(ctx, x, y,1)) // ajouter une nouvelle balle a la liste des ablles deja existantes
                    invalidate() // Actualiser la vue
                    return true
                }
                if (joueur.isTouched(x,y)){
                    projectileList2.add(Projectile(ctx,x,y,0))
                    invalidate()
                    return true
                }
                //else{
                //    bird.updatePosition(x, y) // mettre a jour la poosition de l'oiseau
                //    invalidate() // Actualiser la vue
                //}
            }
        }
        return super.onTouchEvent(event)
    }
}