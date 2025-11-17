package com.example.lr4.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.lr4.data.Player
import com.example.lr4.ui.composables.ConfirmDialog
import com.example.lr4.ui.composables.EditPlayerDialog
import com.example.lr4.ui.composables.PlayerItemCard
import com.example.lr4.viewmodel.PlayerViewModel
import kotlinx.coroutines.launch

/**
 * Главный экран приложения, отображающий список игроков.
 *
 * Предоставляет пользователю возможность:
 * - добавлять, редактировать и удалять игроков;
 * - отмечать присутствие игроков на матче;
 * - очищать весь список игроков;
 * - запускать "розыгрыш" — случайное распределение присутствующих игроков по командам.
 *
 * В нижней части экрана расположены две кнопки:
 * - **FAB “+”** — добавление нового игрока;
 * - **Extended FAB “Розыгрыш”** — переход к экрану с результатами распределения.
 *
 * Экран использует [PlayerViewModel] для управления состоянием и Room через репозиторий.
 *
 * @param viewModel Экземпляр [PlayerViewModel], предоставляющий данные игроков и операции.
 * @param onNavigateToResults Колбэк, вызываемый при переходе на экран результатов.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerListScreen(
    viewModel: PlayerViewModel,
    onNavigateToResults: () -> Unit
) {
    val players by viewModel.players.collectAsState()
    val presentPlayersCount = players.count { it.isPresent }

    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf<Player?>(null) }
    var showClearConfirmDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf<Player?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Список игроков",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                actions = {
                    AnimatedVisibility(players.isNotEmpty()) {
                        IconButton(onClick = { showClearConfirmDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.DeleteSweep,
                                contentDescription = "Очистить список",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(Icons.Default.Add, "Добавить игрока")
                }

                ExtendedFloatingActionButton(
                    onClick = {
                        if (presentPlayersCount >= 2) {
                            viewModel.performDraw()
                            onNavigateToResults()
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    "Нужно выбрать хотя бы 2 игроков",
                                    withDismissAction = true
                                )
                            }
                        }
                    },
                    text = { Text("Розыгрыш ($presentPlayersCount)") },
                    icon = { Icon(Icons.Default.PlayArrow, null) },
                    expanded = true,
                    containerColor = if (presentPlayersCount < 2)
                        MaterialTheme.colorScheme.surfaceVariant
                    else
                        MaterialTheme.colorScheme.primary,
                    contentColor = if (presentPlayersCount < 2)
                        MaterialTheme.colorScheme.onSurfaceVariant
                    else
                        MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            if (players.isEmpty()) {
                Text(
                    text = "Нет игроков. Нажмите «+», чтобы добавить.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    items(players, key = { it.id }) { player ->
                        PlayerItemCard(
                            player = player,
                            onPresenceChange = { viewModel.updatePlayer(player.copy(isPresent = it)) },
                            onEdit = { showEditDialog = player },
                            onDelete = { showDeleteConfirmDialog = player }
                        )
                    }
                }
            }
        }
    }

    // --- Диалоги взаимодействия с пользователем ---

    if (showAddDialog) {
        EditPlayerDialog(
            title = "Добавить игрока",
            onDismiss = { showAddDialog = false },
            onConfirm = { name, tag ->
                viewModel.addPlayer(name, tag)
                showAddDialog = false
            }
        )
    }

    showEditDialog?.let { player ->
        EditPlayerDialog(
            title = "Редактировать игрока",
            initialName = player.name,
            initialTag = player.tag,
            onDismiss = { showEditDialog = null },
            onConfirm = { name, tag ->
                viewModel.updatePlayer(player.copy(name = name, tag = tag))
                showEditDialog = null
            }
        )
    }

    if (showClearConfirmDialog) {
        ConfirmDialog(
            title = "Очистить список?",
            text = "Вы уверены, что хотите удалить всех игроков? Это действие необратимо.",
            onConfirm = {
                viewModel.clearAllPlayers()
                showClearConfirmDialog = false
            },
            onDismiss = { showClearConfirmDialog = false }
        )
    }

    showDeleteConfirmDialog?.let { player ->
        ConfirmDialog(
            title = "Удалить игрока?",
            text = "Удалить игрока «${player.name}»?",
            onConfirm = {
                viewModel.deletePlayer(player)
                showDeleteConfirmDialog = null
            },
            onDismiss = { showDeleteConfirmDialog = null }
        )
    }
}
