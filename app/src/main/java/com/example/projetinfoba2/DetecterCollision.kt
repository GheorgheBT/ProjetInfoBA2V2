package com.example.projetinfoba2

import android.graphics.RectF
import android.media.MediaPlayer

interface DetecterCollision {
    //Liste des objets avec lesquels la collision a lieu
    val listeObjetsDeCollision: ArrayList<ObjetDeCollision>

    //Ajout d'objets de collision dans la liste
    fun add(obj: ObjetDeCollision) {
        listeObjetsDeCollision.add(obj)
    }
    //Suppression d'objets de collision dans la liste
    fun remove(obj: ObjetDeCollision) {
        listeObjetsDeCollision.remove(obj)
    }
    //Methode pour déterminer le comportement de l'objet lors de la collision
    fun onCollision(
        scores: Scores? = null,
        mediaLIst : MutableList<MediaPlayer>? = null
    )

    //Méthode pour déterminer si 2 rectangles sont en contact
    fun isInContact(objet1 : RectF, objet2 : RectF) : Boolean{
        return  objet1.right > objet2.left && objet1.left < objet2.right && objet1.bottom > objet2.top && objet1.top < objet2.bottom
    }
}