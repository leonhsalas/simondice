package com.example.simondice.UI

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class gameViewModel(
    private val dataStore: DataStore<Preferences>
): ViewModel() {
     private val _message: MutableLiveData<String> = MutableLiveData()

    val message: LiveData<String> = _message

    private val _score: MutableLiveData<Int> = MutableLiveData(0)

    val score: LiveData<Int> = _score


    private val _sequence: MutableLiveData<Int> = MutableLiveData()

    val sequence: LiveData<Int> = _sequence

    private var list: MutableList<Int> = mutableListOf()

    private var isSequencePlaying = false

    private var currentTurn = 0

    private var started = false

    val highscore = dataStore.data.map { it[intPreferencesKey("high_score")] ?: 0 }.asLiveData()

    fun startGame() {
        if (started == false) {
            started = true
            nextSequence()
        }
    }
    fun nextSequence() {
        viewModelScope.launch {
            list.add((1..4).random())
            isSequencePlaying = true
            _message.value = "Espera..."
            delay(1200)
            list.forEach { number ->
                _sequence.value = number
                delay(600)
                _sequence.value = 0
                delay(300)
            }
            _message.value = "Tu Turno"
            isSequencePlaying = false
        }
    }

    fun imageClicked(Image: Int) {
        if (!isSequencePlaying && list.size > 0) {
            printImage(Image)
            val isTurnCorrect = list[currentTurn] == Image
            if(!isTurnCorrect) {
                _message.value = "!Ja, Ja, Ja has perdido!"
                resetGame()
            } else {
                if (list.size - 1 == currentTurn) {
                    currentTurn = 0
                    _score.value = _score.value?.plus(1)
                    saveHighestScore()
                    nextSequence()
                } else {
                    currentTurn ++
                }
            }
        }
    }

    fun printImage(image: Int) {
        viewModelScope.launch {
            _sequence.value = image
            delay(600)
            _sequence.value = 0
        }
    }

    fun resetGame() {
        started = false
        currentTurn = 0
        list = mutableListOf()
        _score.value = 0
    }

    fun saveHighestScore() {
        val highscore = highscore.value ?: 0
        if (_score.value!! > highscore) {
            viewModelScope.launch {
                dataStore.edit { it[intPreferencesKey("high_score")] = score.value!! }
            }
        }
    }

    companion object {
        fun provideFactory(dataStore: DataStore<Preferences>): ViewModelProvider.Factory = object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return gameViewModel(dataStore) as T
            }
        }
    }
}