package com.example.lr4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.lr4.ui.TeamGeneratorScreen
import com.example.lr4.ui.TeamsResultScreen
import com.example.lr4.data.Player
import com.example.lr4.ui.theme.AppTheme

/**
 * Главная точка входа в приложение.
 *
 * Управляет навигацией между экранами и хранит состояние игроков.
 *
 * Архитектура:
 *  - Навигация построена на простом состоянии `screen`, т.к. приложение имеет всего два экрана.
 *  - Списки игроков и команд хранятся в `remember` (state in composition).
 *  - При масштабировании легко заменить на `Navigation Compose` и ViewModel.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                var screen by remember { mutableStateOf("main") }
                var players by remember { mutableStateOf(listOf<Player>()) }
                var redTeam by remember { mutableStateOf(listOf<Player>()) }
                var greenTeam by remember { mutableStateOf(listOf<Player>()) }

                when (screen) {
                    // Экран управления списком игроков
                    "main" -> TeamGeneratorScreen(
                        players = players,
                        onPlayersChange = { players = it },
                        onGenerate = { reds, greens ->
                            redTeam = reds
                            greenTeam = greens
                            screen = "result"
                        }
                    )

                    // Экран отображения результата розыгрыша
                    "result" -> TeamsResultScreen(
                        redTeam = redTeam,
                        greenTeam = greenTeam,
                        onBack = { screen = "main" }
                    )
                }
            }
        }
    }
}
