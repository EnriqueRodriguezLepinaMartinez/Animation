package com.example.ricky.animation

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import org.jetbrains.anko.toast
import kotlin.coroutines.experimental.CoroutineContext

class MainActivity : AppCompatActivity() {

    // private var expectAnimMove : ExpectAnim = ExpectAnim()
    // dispatches execution onto the Android main UI thread
    private val uiContext: CoroutineContext = UI

    // represents a common pool of shared threads as the coroutine dispatcher
    private val bgContext: CoroutineContext = CommonPool

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Listener del boton animacion que ejecuta una tarea de animacion en segundo plano
        animacion.setOnClickListener {
            tareaSegundoPlano()
        }
        // Listener del boton Domotta que me 'abre' la Main2Activity con el menu inferior
        val intento : Intent = Intent(this,MainActivity::class.java)
        domotta.setOnClickListener{
            toast("Soy Domotta")
            startActivity(intento)
        }
    }

    /**
     * launch coroutine in UI context
     */

    var j : Int = 1
    var job2 : Job? = null

    private fun tareaSegundoPlano() = launch(uiContext) {
        // tarea child en el contexto de este hilo/thread
        // contador de 30 hacia 1
        val task2 = async(bgContext) {
            for (i in 30 downTo 1) { // countdown from 10 to 1
                //texto2.text = "Countdown $i ..." // update text
                Log.d("Task2", "Contandor: ${i}")
                delay(200) // wait half a second
            }
        }
        job2 = task2
        // non ui thread (child task)
        // lanzamos la tarea
        val result = task2.start()
        // tambien podemos job2?.start()

        // domotta bajando/subiendo
        // esta tarea es la tarea principal
        j *= -1
        // animamos a domotta, main task in UI
        val objectAnimator = ObjectAnimator.ofFloat(
                domotta,
                "translationY",
                300f*j)
        objectAnimator.duration = 3000L
        objectAnimator.interpolator
        objectAnimator.start()

        for (i in 10 downTo 1) { // countdown from 10 to 1
            texto.text = "Countdown $i ..." // update text
            delay(500) // wait half a second

            // si la animacion para, paro la tarea 2
            if (!objectAnimator.isRunning) job2!!.cancel()
        }
        // finalizamos la tarea principal
        texto.text = "Done!"
    }
}
