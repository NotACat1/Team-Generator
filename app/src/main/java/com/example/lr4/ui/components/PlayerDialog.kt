package com.example.lr4.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue

/**
 * Диалог редактирования или добавления игрока.
 *
 * Используется для изменения имени игрока или создания нового.
 * Отображается как Material 3 [AlertDialog] с текстовым полем ввода.
 *
 * @param initialName Начальное значение поля ввода (если редактируем существующего игрока).
 * @param onConfirm Вызывается при нажатии кнопки "Сохранить"; передаёт итоговое имя.
 * @param onDismiss Вызывается при закрытии диалога без сохранения (кнопка "Отмена" или вне области диалога).
 *
 * Особенности реализации:
 *  - Состояние текста хранится через `remember` с типом [TextFieldValue], чтобы сохранять курсор и выделение.
 *  - Передача `trim()` гарантирует, что пробелы не будут учитываться при сохранении имени.
 */
@Composable
fun PlayerDialog(
    initialName: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf(TextFieldValue(initialName)) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Редактировать игрока") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Имя игрока") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(text.text.trim()) },
                enabled = text.text.isNotBlank() // не даём сохранить пустое имя
            ) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}
