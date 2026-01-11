package com.example.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.tictactoe.ui.theme.TicTacToeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeTheme {
                TicTacToeGame()
            }
        }
    }
}

@Composable
fun TicTacToeGame() {
    var board by remember { mutableStateOf(Array(3) { Array(3) { R.drawable.empty } }) }
    var player by remember { mutableStateOf(R.drawable.x) }
    var done by remember { mutableStateOf(false) }
    var won by remember { mutableStateOf(R.drawable.empty) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (!done) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Player Turn: ",
                    color = Color.Black
                )
                Image(
                    painter = painterResource(id = player),
                    contentDescription = "Current Player",
                    modifier = Modifier
                        .size(32.dp)
                        .padding(start = 8.dp)
                )
            }
        } else {
            Text(
                text = "Game Over",
                color = Color.Black
            )
        }

        TicTacToeBoard(
            board = board,
            onClick = { r, c ->
                if (!done && board[r][c] == R.drawable.empty) {
                    board = board.mapIndexed { i, row ->
                        row.mapIndexed { j, cell ->
                            if (i == r && j == c) player else cell
                        }.toTypedArray()
                    }.toTypedArray()

                    if (hasWon(board)) {
                        done = true
                        won = player
                    } else if (isFull(board)) {
                        done = true
                    } else {
                        player = if (player == R.drawable.x) R.drawable.o else R.drawable.x
                    }
                }
            }
        )

        if (done) {
            if (won == R.drawable.empty) {
                Text(text = "It's a Draw!", color = Color.Black)
            } else {
                Text(
                    text = "Winner: ${if (won == R.drawable.x) "X" else "O"}",
                    color = Color.Black
                )
            }

            Button(
                onClick = {
                    board = Array(3) { Array(3) { R.drawable.empty } }
                    player = R.drawable.x
                    done = false
                    won = R.drawable.empty
                },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "New Game")
            }
        }
    }
}

@Composable
fun TicTacToeBoard(
    board: Array<Array<Int>>,
    onClick: (r: Int, c: Int) -> Unit
) {
    Column {
        for (r in 0..2) {
            Row {
                for (c in 0..2) {
                    TicTacToeCell(
                        mark = board[r][c],
                        onClick = { onClick(r, c) }
                    )
                }
            }
        }
    }
}

@Composable
fun TicTacToeCell(mark: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .padding(4.dp)
            .background(Color.LightGray)
    ) {
        Image(
            painter = painterResource(id = mark),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clickable(enabled = mark == R.drawable.empty, onClick = onClick)
        )
    }
}

fun hasWon(board: Array<Array<Int>>): Boolean {
    for (i in 0..2) {
        if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != R.drawable.empty) return true
        if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != R.drawable.empty) return true
    }
    if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != R.drawable.empty) return true
    if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != R.drawable.empty) return true

    return false
}

fun isFull(board: Array<Array<Int>>): Boolean {
    for (row in board) {
        for (cell in row) {
            if (cell == R.drawable.empty) return false
        }
    }
    return true
}

