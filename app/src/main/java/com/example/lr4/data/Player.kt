package com.example.lr4.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Сущность игрока, хранимая в локальной базе данных Room.
 *
 * Каждая запись представляет одного игрока, участвующего в распределении по командам.
 *
 * @property id Уникальный идентификатор игрока (генерируется автоматически).
 * @property name Имя игрока.
 * @property tag Дополнительная метка для различения игроков с одинаковыми именами.
 * @property isPresent Флаг присутствия игрока (используется при формировании команд).
 */
@Entity(tableName = "players")
data class Player(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val tag: String = "",
    val isPresent: Boolean = false
)
