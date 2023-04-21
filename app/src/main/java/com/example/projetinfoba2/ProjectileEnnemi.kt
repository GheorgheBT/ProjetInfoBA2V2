package com.example.projetinfoba2

import android.content.Context

class ProjectileEnnemi(context: Context, x: Float, y: Float, numeroProjectile: Int, projectileTaille: Float) : Projectile(context, x,y,  numeroProjectile, projectileTaille) {
    override val Vitesse = 0f

}