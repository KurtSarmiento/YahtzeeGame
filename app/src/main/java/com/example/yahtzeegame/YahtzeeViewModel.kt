package com.example.yahtzeegame

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class YahtzeeViewModel : ViewModel() {
    var diceValues by mutableStateOf(
        listOf(1, 1, 1, 1, 1)
    )
        private set
    var categoryScores by mutableStateOf(
        emptyList<CategoryScore>()
    )
        private set
    var rollCount by mutableStateOf(0)
        private set
    var heldDice by mutableStateOf(
        List(5) { false }
    )
        private set
    var usedCategories by mutableStateOf(
        emptySet<YahtzeeCategory>()
    )
        private set
    var savedScores by mutableStateOf(
        emptyList<CategoryScore>()
    )
        private set
    val gameOver: Boolean
        get() = usedCategories.size == YahtzeeCategory.entries.size
    val totalScore: Int
        get() = savedScores.sumOf { it.score }

    private fun rollDice(): Int {
        return Random.nextInt(1, 7)
    }

    fun rollWithCoroutine() {
        if (rollCount >= 3 || gameOver) {
            return
        }
        rollCount++
        viewModelScope.launch {
            repeat(10) {
                diceValues = diceValues.mapIndexed { index, value ->
                    if (heldDice[index]) {
                        value
                    } else {
                        rollDice()
                    }
                }
                delay(100)
            }
            evaluateDice()
        }
    }

    private fun evaluateDice() {
        categoryScores =
            YahtzeeCategory.entries
                .filter { category ->
                    category !in usedCategories
                }
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

    fun newGame() {
        diceValues = List(5) { 1 }
        rollCount = 0
        heldDice = List(5) { false }
        categoryScores = emptyList()
        savedScores = emptyList()
        usedCategories = emptySet()
    }

    fun toggleHold(index: Int) {
        if (rollCount == 0 || rollCount >= 3 || gameOver) {
            return
        }
        heldDice = heldDice.toMutableList().also {
            it[index] = !it[index]
        }
    }

    fun selectCategory(category: YahtzeeCategory) {
        if (rollCount == 0 || category in usedCategories) {
            return
        }
        val score = DiceRules.scoreFor(
            category = category,
            dice = diceValues
        )
        savedScores = savedScores + CategoryScore(
            category = category,
            score = score
        )
        usedCategories = usedCategories + category
        startNextTurn()
    }

    private fun startNextTurn() {
        rollCount = 0
        heldDice = List(5) { false }
        categoryScores = emptyList()
    }
}
