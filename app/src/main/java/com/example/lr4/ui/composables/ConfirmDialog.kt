package com.example.lr4.ui.composables

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Диалоговое окно подтверждения действия.
 *
 * Отображает заголовок, текст с описанием и две кнопки:
 * - **Да** — для подтверждения действия;
 * - **Отмена** — для отмены или закрытия окна.
 *
 * Используется для операций, требующих пользовательского подтверждения
 * (например, удаление игрока или очистка списка).
 *
 * @param title Заголовок диалога.
 * @param text Основное сообщение или пояснение к действию.
 * @param onConfirm Колбэк, вызываемый при подтверждении действия.
 * @param onDismiss Колбэк, вызываемый при закрытии диалога без подтверждения.
 */
@Composable
fun ConfirmDialog(
    title: String,
    text: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
            )
        },
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Да", color = MaterialTheme.colorScheme.onPrimary)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}
