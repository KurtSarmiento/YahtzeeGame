package com.example.yahtzeegame

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button

@Composable
fun YahtzeeScreen(
    viewModel: YahtzeeViewModel
) {

    Column(
        modifier = Modifier.padding(16.dp)
    ) {

        Text(
            text = "Yahtzee"
        )

        Row {

            viewModel.diceValues.forEach { value ->

                Die(
                    value = value
                )
            }
        }

        Button(
            onClick = {
                viewModel.rollWithCoroutine()
            }
        ) {
            Text("Roll")
        }

        viewModel.categoryScores.forEach { result ->

            Text(
                text = "${result.category}: ${result.score}"
            )
        }
    }
}