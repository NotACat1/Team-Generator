package com.example.lr4.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lr4.data.Player
import com.example.lr4.ui.components.PlayerDialog
import com.example.lr4.ui.components.PlayerItem
import kotlin.random.Random
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete

/**
 * Основной экран для управления списком игроков и формирования команд.
 *
 * Отвечает за:
 *  - добавление, редактирование и удаление игроков;
 *  - отметку присутствующих;
 *  - случайное распределение по командам;
 *  - очистку списка.
 *
 * @param players Текущий список игроков (передаётся "снаружи" для управления состоянием на уровне Activity).
 * @param onPlayersChange Callback для обновления списка игроков.
 * @param onGenerate Callback, вызываемый после генерации команд (возвращает списки красных и зелёных).
 *
 * Архитектурные особенности:
 *  - Состояние игроков и экранов хранится вне (state hoisting).
 *  - UI полностью реактивный: каждый вызов onPlayersChange пересобирает список.
 *  - Compose Scaffold + LazyColumn обеспечивают современный Material 3 UX.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamGeneratorScreen(
    players: List<Player>,
    onPlayersChange: (List<Player>) -> Unit,
    onGenerate: (List<Player>, List<Player>) -> Unit
) {
    // Ввод имени нового игрока
    var playerName by remember { mutableStateOf("") }

    // Текущий игрок для редактирования (если открыт диалог)
    var editDialogPlayer by remember { mutableStateOf<Player?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Розыгрыш команд") })
        },
        floatingActionButton = {
            // FAB для полной очистки списка игроков
            if (players.isNotEmpty()) {
                ExtendedFloatingActionButton(
                    text = { Text("Очистить") },
                    onClick = { onPlayersChange(emptyList()) },
                    icon = { Icon(Icons.Default.Delete, contentDescription = "Очистить список") }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Поле ввода имени нового игрока
            OutlinedTextField(
                value = playerName,
                onValueChange = { playerName = it },
                label = { Text("Имя игрока") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Кнопка добавления нового игрока
            Button(
                onClick = {
                    if (playerName.isNotBlank()) {
                        onPlayersChange(players + Player(name = playerName.trim()))
                        playerName = ""
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Добавить")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Список игроков (с чекбоксами, редактированием и удалением)
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(players, key = { it.id }) { player ->
                    PlayerItem(
                        player = player,
                        onCheckedChange = { checked ->
                            // Изменение статуса "присутствует"
                            onPlayersChange(
                                players.map {
                                    if (it.id == player.id) it.copy(isPresent = checked) else it
                                }
                            )
                        },
                        onEdit = { editDialogPlayer = player },
                        onDelete = {
                            onPlayersChange(players.filter { it.id != player.id })
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Кнопка формирования команд
            Button(
                onClick = {
                    // Берём только отмеченных игроков и перемешиваем
                    val present = players.filter { it.isPresent }.shuffled(Random)
                    val half = present.size / 2
                    val reds = present.take(half)
                    val greens = present.drop(half)
                    onGenerate(reds, greens)
                },
                enabled = players.any { it.isPresent },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Сформировать команды")
            }

            // Диалог редактирования имени игрока
            editDialogPlayer?.let { player ->
                PlayerDialog(
                    initialName = player.name,
                    onConfirm = { newName ->
                        onPlayersChange(players.map {
                            if (it.id == player.id) it.copy(name = newName) else it
                        })
                        editDialogPlayer = null
                    },
                    onDismiss = { editDialogPlayer = null }
                )
            }
        }
    }
}
