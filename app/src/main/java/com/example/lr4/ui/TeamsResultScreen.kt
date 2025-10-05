package com.example.lr4.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.lr4.data.Player
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

/**
 * Экран отображения результатов розыгрыша команд.
 *
 * @param redTeam Список игроков команды "красных".
 * @param greenTeam Список игроков команды "зелёных".
 * @param onBack Callback для возврата на экран редактирования списка.
 *
 * UI-принципы:
 *  - Простая, симметричная компоновка.
 *  - Используются цвета Material 3: `error` для красных, `primary` для зелёных.
 *  - Показ коротких ID для различения игроков с одинаковыми именами.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamsResultScreen(
    redTeam: List<Player>,
    greenTeam: List<Player>,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Результаты розыгрыша") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Команда красных
            Text("🔴 Красные", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
            redTeam.forEach { Text("• ${it.name} (${it.id.take(6)}...)") }

            Spacer(modifier = Modifier.height(20.dp))

            // Команда зелёных
            Text("🟢 Зелёные", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            greenTeam.forEach { Text("• ${it.name} (${it.id.take(6)}...)") }
        }
    }
}
