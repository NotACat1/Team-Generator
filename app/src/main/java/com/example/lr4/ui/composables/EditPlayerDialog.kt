package com.example.lr4.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.compose.ui.text.font.FontWeight

/**
 * Диалоговое окно для добавления или редактирования игрока.
 *
 * Содержит два поля ввода:
 * - **Имя игрока** (обязательное);
 * - **Тег** (необязательное пояснение, например “Вратарь”).
 *
 * При открытии диалога автоматически фокусируется поле имени
 * и отображается клавиатура для быстрого ввода.
 *
 * @param title Заголовок окна (“Добавить игрока” или “Редактировать игрока”).
 * @param initialName Начальное значение поля имени.
 * @param initialTag Начальное значение поля тега.
 * @param onDismiss Колбэк, вызываемый при закрытии диалога без сохранения.
 * @param onConfirm Колбэк, вызываемый при подтверждении изменений.
 * Передаёт значения имени и тега без лишних пробелов.
 */
@Composable
fun EditPlayerDialog(
    title: String,
    initialName: String = "",
    initialTag: String = "",
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var tag by remember { mutableStateOf(initialTag) }
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium)
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Имя игрока") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    singleLine = true
                )
                OutlinedTextField(
                    value = tag,
                    onValueChange = { tag = it },
                    label = { Text("Тег (например, 'Вратарь')") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(name.trim(), tag.trim()) },
                enabled = name.isNotBlank()
            ) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )

    // Автофокус и показ клавиатуры при открытии диалога
    LaunchedEffect(Unit) {
        delay(150)
        focusRequester.requestFocus()
        keyboard?.show()
    }
}
