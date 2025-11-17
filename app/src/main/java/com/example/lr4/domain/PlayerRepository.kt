package com.example.lr4.domain

import com.example.lr4.data.Player
import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс репозитория для абстракции доступа к данным игроков.
 *
 * Позволяет изолировать слой данных (Room, сеть и т.д.) от слоя бизнес-логики,
 * что упрощает тестирование и поддержку кода.
 */
interface PlayerRepository {

    /**
     * Возвращает поток списка всех игроков.
     */
    fun getAllPlayers(): Flow<List<Player>>

    /**
     * Добавляет нового игрока или обновляет существующего.
     *
     * @param player Игрок для добавления или обновления.
     */
    suspend fun addOrUpdatePlayer(player: Player)

    /**
     * Удаляет игрока.
     *
     * @param player Игрок для удаления.
     */
    suspend fun deletePlayer(player: Player)

    /**
     * Очищает всю таблицу игроков.
     */
    suspend fun clearAllPlayers()
}
