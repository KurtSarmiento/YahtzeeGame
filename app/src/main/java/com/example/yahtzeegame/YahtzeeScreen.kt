package com.example.yahtzeegame

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun YahtzeeScreen(
    viewModel: YahtzeeViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Yahtzee",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            viewModel.diceValues.forEachIndexed { index, value ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Die(
                        value = value,
                        onClick = {
                            viewModel.toggleHold(index)
                        }
                    )

                    if (viewModel.heldDice[index]) {
                        Text(
                            text = "HOLD",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 12.sp
                        )
                    } else {
                        Text(
                            text = "",
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Rolls: ${viewModel.rollCount}/3",
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.rollWithCoroutine()
            }
        ) {
            Text("Roll")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (viewModel.rollCount > 0) {
            Text(
                text = "Choose a Category:",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            viewModel.categoryScores.forEach { result ->
                Button(
                    onClick = {
                        viewModel.selectCategory(
                            result.category
                        )
                    },
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Text(
                        text = "${result.category}: ${result.score}"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(
            text = "Saved Scores",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        viewModel.savedScores.forEach { result ->
            Text(
                text = "${result.category}: ${result.score}",
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Total Score: ${viewModel.totalScore}",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        if (viewModel.gameOver) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Game Over!",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.error
            )

            Text(
                text = "Final Score: ${viewModel.totalScore}",
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.newGame()
                }
            ) {
                Text("New Game")
            }
        }
    }
}

@Composable
fun Die(
    value: Int,
    onClick: () -> Unit
) {
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
        painter = painterResource(id = diceImage),
        contentDescription = "Dice showing $value",
        modifier = Modifier
            .size(52.dp)
            .clickable { onClick() }
    )
}
