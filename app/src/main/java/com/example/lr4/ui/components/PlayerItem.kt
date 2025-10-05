package com.example.lr4.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lr4.data.Player

/**
 * Компонент отображения одного игрока в списке.
 *
 * Визуально представляет карточку (Material 3 [Card]) с именем, коротким ID, чекбоксом и кнопками редактирования/удаления.
 *
 * @param player Объект игрока, данные которого отображаются.
 * @param onCheckedChange Колбэк при изменении состояния "присутствует" (чекбокс).
 * @param onEdit Колбэк при нажатии кнопки "Редактировать".
 * @param onDelete Колбэк при нажатии кнопки "Удалить".
 *
 * UX-рекомендации:
 *  - Отображение укороченного ID (`take(6)`) помогает различать игроков с одинаковыми именами.
 *  - Используются Material Icons, полностью совместимые с Material 3.
 *  - Небольшая тень (`cardElevation`) добавляет визуальной глубины.
 */
@Composable
fun PlayerItem(
    player: Player,
    onCheckedChange: (Boolean) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Checkbox(
                checked = player.isPresent,
                onCheckedChange = onCheckedChange
            )

            // Основная информация о игроке
            Column(modifier = Modifier.weight(1f)) {
                Text(player.name, style = MaterialTheme.typography.bodyLarge)
                Text(
                    "ID: ${player.id.take(6)}…",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Кнопка редактирования
            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Редактировать игрока"
                )
            }

            // Кнопка удаления
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Удалить игрока"
                )
            }
        }
    }
}
