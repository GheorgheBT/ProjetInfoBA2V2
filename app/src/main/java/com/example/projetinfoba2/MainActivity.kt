package com.example.projetinfoba2

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.PixelFormat
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.View
import android.view.View.OnTouchListener


class MainActivity: Activity(), View.OnClickListener{

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.joystickView->{

            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
        joystickView.onJoystickTouch(event)
        Log.d("TAG","AAA")
    }

    lateinit var drawingView: DrawingView
    lateinit var joystickView: JoystickView
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        drawingView = findViewById(R.id.vMain)
        drawingView.setWillNotDraw(false)
        drawingView.invalidate()
        //Affichage du joystick
        joystickView = findViewById<JoystickView>(R.id.joystickView)
        joystickView.setWillNotDraw(false)
        joystickView.setZOrderOnTop(true)
        val sfhTrackHolder: SurfaceHolder = joystickView.getHolder()
        sfhTrackHolder.setFormat(PixelFormat.TRANSPARENT)
        joystickView.setOnTouchListener(OnTouchListener { v, event ->
            joystickView.onJoystickTouch(event)
            true
        })




        joystickView.invalidate()

    }
}