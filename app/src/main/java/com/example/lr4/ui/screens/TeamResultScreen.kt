package com.example.lr4.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lr4.data.Player
import com.example.lr4.viewmodel.PlayerViewModel

/**
 * Экран отображения результатов розыгрыша команд.
 *
 * После распределения игроков на **красную** и **зелёную** команды,
 * экран показывает две карточки с составами команд.
 * Если распределение невозможно (меньше двух присутствующих игроков),
 * пользователю выводится соответствующее сообщение.
 *
 * @param viewModel [PlayerViewModel], предоставляющий данные о командах.
 * @param onBack Колбэк для возврата на предыдущий экран.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamResultScreen(
    viewModel: PlayerViewModel,
    onBack: () -> Unit
) {
    val result by viewModel.teamResult.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Результаты розыгрыша",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        if (result.redTeam.isEmpty() && result.greenTeam.isEmpty()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Недостаточно игроков для розыгрыша",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
            return@Scaffold
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TeamList(
                modifier = Modifier.weight(1f),
                title = "Красные",
                team = result.redTeam,
                color = MaterialTheme.colorScheme.errorContainer
            )
            TeamList(
                modifier = Modifier.weight(1f),
                title = "Зелёные",
                team = result.greenTeam,
                color = MaterialTheme.colorScheme.tertiaryContainer
            )
        }
    }
}

/**
 * Отображает одну команду в виде карточки с её игроками.
 *
 * Карточка включает:
 * - название команды (заголовок);
 * - список игроков с их тегами, если они указаны.
 *
 * @param modifier [Modifier] для позиционирования и стилизации.
 * @param title Название команды (“Красные”, “Зелёные” и т.п.).
 * @param team Список игроков, входящих в команду.
 * @param color Цвет фона карточки команды.
 */
@Composable
fun TeamList(
    modifier: Modifier = Modifier,
    title: String,
    team: List<Player>,
    color: Color
) {
    ElevatedCard(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            HorizontalDivider(
                modifier = Modifier.padding(bottom = 8.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            )
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(team) { player ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = player.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        if (player.tag.isNotBlank()) {
                            Text(
                                text = "(${player.tag})",
                                fontSize = 14.sp,
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
        }
    }
}
