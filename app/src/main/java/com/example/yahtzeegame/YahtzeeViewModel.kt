package com.example.yahtzeegame

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.random.Random
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
//import android.os.Handler
//import android.os.Looper
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class YahtzeeViewModel : ViewModel() {
//    private val mainHandler = Handler(
//        Looper.getMainLooper()
//    )
    var diceValues by mutableStateOf(
        listOf(1, 1, 1, 1, 1)
    )
        private set

    var categoryScores by mutableStateOf(
        emptyList<CategoryScore>()
    )
        private set

    private fun rollDice(): Int {
        return Random.nextInt(1, 7)
    }

//    fun rollWithoutCoroutine() {
//        Thread {
//            repeat(10) {
//                val newValues = List(5) {
//                    rollDice()
//                }
//                mainHandler.post {
//
//                    diceValues = newValues
//                }
//                Thread.sleep(100)
//            }
//        }.start()
//    }
    fun rollWithCoroutine() {
        viewModelScope.launch {
            repeat(10) {
                diceValues = List(5) {
                    rollDice()
                }
                evaluateDice()
                delay(100)
            }
        }
    }

    private fun evaluateDice() {
        categoryScores =
            DiceRules
                .getAvailableCategories(diceValues)
                .map { category ->

                    CategoryScore(
                        category = category,
                        score = DiceRules.scoreFor(
                            category = category,
                            dice = diceValues
                        )
                    )
                }
    }
}

@Composable
fun Die(value: Int) {

    val diceImage = when (value) {

        1 -> R.drawable.die_1
        2 -> R.drawable.die_2
        3 -> R.drawable.die_3
        4 -> R.drawable.die_4
        5 -> R.drawable.die_5
        6 -> R.drawable.die_6

        else -> R.drawable.die_1
    }

    Image(
        painter = painterResource(
            id = diceImage
        ),
        contentDescription = "Dice showing $value"
    )
}