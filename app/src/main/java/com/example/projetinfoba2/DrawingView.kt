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


    val bird = Bird(ctx ,1000f,100f)
    val bulletList = mutableListOf<Balle>()


    override fun onDraw(canvas: Canvas?) {
        //super.onDraw(canvas)
        canvas?.drawColor(Color.BLACK)// Couleur de fond de la Drawingview

        canvas?.drawBitmap(scaledBackgroundImage, backgroundOffset, 0f, null) // deplace l'image d'arriere plan
        backgroundOffset -= backgroundspeed//
        if (backgroundOffset < -7000f) { //
            backgroundOffset = 0f
        }
        if (canvas != null) {
            bird.draw(canvas)
            for (bullet in bulletList) {
                bullet.draw(canvas)
                bullet.updatePosition()
            }
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
                    bulletList.add(Balle(ctx, x, y)) // ajouter une nouvelle balle a la liste des ablles deja existantes
                    invalidate() // Actualiser la vue
                    return true
                }
                else{
                    bird.updatePosition(x, y) // mettre a jour la poosition de l'oiseau
                    invalidate() // Actualiser la vue
                }
            }
        }
        return super.onTouchEvent(event)
    }
}