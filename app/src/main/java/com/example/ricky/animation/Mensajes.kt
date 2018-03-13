package com.example.ricky.animation

/**
 * Created by Ricky on 23/02/2018.
 */
import android.util.Log

/**
 * Clase para manejo de datos
 */
data class Mensajes constructor(private var key2: String = "Hi"){
    //propiedad
    var key: String = ""
            // get por defecto
        get() = field
            // cambiamos el set
        set(value) {
            field = value + " :P"
        }
    // objeto
    companion object Compi {
        fun say(message: String) {
            Log.d("Mensajes",message)
        }
    }

}