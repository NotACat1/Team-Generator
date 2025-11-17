package com.example.lr4.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) для доступа к таблице игроков в базе данных.
 *
 * Определяет основные операции вставки, удаления и получения данных.
 * Использует Kotlin [Flow] для автоматического обновления UI при изменениях.
 */
@Dao
interface PlayerDao {

    /**
     * Вставляет нового игрока или обновляет существующего.
     *
     * @param player Объект [Player] для добавления или обновления.
     */
    @Upsert
    suspend fun upsertPlayer(player: Player)

    /**
     * Удаляет указанного игрока.
     *
     * @param player Объект [Player], который нужно удалить.
     */
    @Delete
    suspend fun deletePlayer(player: Player)

    /**
     * Удаляет всех игроков из таблицы.
     */
    @Query("DELETE FROM players")
    suspend fun clearAllPlayers()

    /**
     * Возвращает поток всех игроков, отсортированных по имени.
     *
     * @return [Flow] со списком игроков [Player].
     */
    @Query("SELECT * FROM players ORDER BY name ASC")
    fun getAllPlayers(): Flow<List<Player>>
}
