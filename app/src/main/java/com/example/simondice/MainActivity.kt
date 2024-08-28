package com.example.simondice

import android.content.Context
import android.os.Bundle
import android.os.Message
import android.preference.PreferenceDataStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.transition.Visibility
import com.example.simondice.UI.gameViewModel
import kotlinx.coroutines.delay
import com.example.simondice.dataBinding.ActivityMainBinding
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "simon_says")

class MainActivity : AppCompatActivity() {
    private lateinit var message: TextView
    private val viewModel: gameViewModel by viewModels {
        gameViewModel.provideFactory(dataStore = dataStore)
    }
    private lateinit var boton: Button
    private lateinit var points: TextView
    private lateinit var max: TextView
    private lateinit var goku: ImageView
    private lateinit var vegeta: ImageView
    private lateinit var jiren: ImageView
    private lateinit var freezer: ImageView
    private lateinit var covergoku: LinearLayout
    private lateinit var covervegeta: LinearLayout
    private lateinit var coverjiren: LinearLayout
    private lateinit var coverfreezer: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        boton = findViewById(R.id.button)
        points = findViewById(R.id.puntaje)
        max = findViewById(R.id.maximo)
        goku = findViewById(R.id.goku)
        vegeta = findViewById(R.id.vegeta)
        jiren = findViewById(R.id.jiren)
        freezer = findViewById(R.id.freezer)
        covergoku = findViewById(R.id.covergoku)
        covervegeta = findViewById(R.id.covervegeta)
        coverjiren = findViewById(R.id.coverjiren)
        coverfreezer = findViewById(R.id.coverfreezer)
        message = findViewById(R.id.message)
        binding = ActivityMainBinding.inflate(layoutInflater)
        var counter: Int = 0
        var won: Boolean = false
        var lost: Boolean = false
        var turn: Boolean = false
        var wrong: Boolean = false
        var started: Boolean = false

        var random = (1..4).random()
        goku.setOnClickListener{
            viewModel.imageClicked(1)
        }
        vegeta.setOnClickListener{
            viewModel.imageClicked(2)
        }
        jiren.setOnClickListener{
            viewModel.imageClicked(3)
        }
        freezer.setOnClickListener{
            viewModel.imageClicked(4)
        }
        boton.setOnClickListener{
            viewModel.startGame()
        }

        viewModel.sequence.observe(this) { number ->
            when(number) {
                1 -> {
                    covergoku.visibility = View.VISIBLE
                    covervegeta.visibility = View.INVISIBLE
                    coverjiren.visibility = View.INVISIBLE
                    coverfreezer.visibility = View.INVISIBLE
                }
                2 -> {
                    covergoku.visibility = View.INVISIBLE
                    covervegeta.visibility = View.VISIBLE
                    coverjiren.visibility = View.INVISIBLE
                    coverfreezer.visibility = View.INVISIBLE
                }
                3 -> {
                    covergoku.visibility = View.INVISIBLE
                    covervegeta.visibility = View.INVISIBLE
                    coverjiren.visibility = View.VISIBLE
                    coverfreezer.visibility = View.INVISIBLE
                }
                4 -> {
                    covergoku.visibility = View.INVISIBLE
                    covervegeta.visibility = View.INVISIBLE
                    coverjiren.visibility = View.INVISIBLE
                    coverfreezer.visibility = View.VISIBLE
                }
                0 -> {
                    covergoku.visibility = View.INVISIBLE
                    covervegeta.visibility = View.INVISIBLE
                    coverjiren.visibility = View.INVISIBLE
                    coverfreezer.visibility = View.INVISIBLE
                }
            }
        }

        viewModel.message.observe(this) { str ->
            message.text = str
        }

        viewModel.score.observe(this) { str ->
            points.text = "Puntos: $str"
        }

        viewModel.highscore.observe(this) { str ->
            max.text = "Maxima Puntuación: $str"
        }
    }
}