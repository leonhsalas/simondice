package com.example.simondice

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.simondice.ui.GameViewModel
import com.example.simondice.databinding.ActivityMainBinding

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "simon_says")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: GameViewModel by viewModels {
        GameViewModel.provideFactory(dataStore = dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel



        binding.goku.setOnClickListener{
            viewModel.imageClicked(1)
        }
        binding.vegeta.setOnClickListener{
            viewModel.imageClicked(2)
        }
        binding.jiren.setOnClickListener{
            viewModel.imageClicked(3)
        }
        binding.freezer.setOnClickListener{
            viewModel.imageClicked(4)
        }
        binding.button.setOnClickListener{
            viewModel.startGame()
        }

        viewModel.sequence.observe(this) { number ->
            when(number) {
                1 -> {
                    binding.covergoku.visibility = View.VISIBLE
                    binding.covervegeta.visibility = View.INVISIBLE
                    binding.coverjiren.visibility = View.INVISIBLE
                    binding.coverfreezer.visibility = View.INVISIBLE
                }
                2 -> {
                    binding.covergoku.visibility = View.INVISIBLE
                    binding.covervegeta.visibility = View.VISIBLE
                    binding.coverjiren.visibility = View.INVISIBLE
                    binding.coverfreezer.visibility = View.INVISIBLE
                }
                3 -> {
                    binding.covergoku.visibility = View.INVISIBLE
                    binding.covervegeta.visibility = View.INVISIBLE
                    binding.coverjiren.visibility = View.VISIBLE
                    binding.coverfreezer.visibility = View.INVISIBLE
                }
                4 -> {
                    binding.covergoku.visibility = View.INVISIBLE
                    binding.covervegeta.visibility = View.INVISIBLE
                    binding.coverjiren.visibility = View.INVISIBLE
                    binding.coverfreezer.visibility = View.VISIBLE
                }
                0 -> {
                    binding.covergoku.visibility = View.INVISIBLE
                    binding.covervegeta.visibility = View.INVISIBLE
                    binding.coverjiren.visibility = View.INVISIBLE
                    binding.coverfreezer.visibility = View.INVISIBLE
                }
            }
        }
    }

}