package com.example.lr4.domain

import com.example.lr4.data.Player
import com.example.lr4.data.PlayerDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Реализация [PlayerRepository], использующая локальную базу данных Room.
 *
 * @property dao DAO для выполнения операций с таблицей игроков.
 */
class PlayerRepositoryImpl @Inject constructor(
    private val dao: PlayerDao
) : PlayerRepository {

    override fun getAllPlayers(): Flow<List<Player>> = dao.getAllPlayers()

    override suspend fun addOrUpdatePlayer(player: Player) = dao.upsertPlayer(player)

    override suspend fun deletePlayer(player: Player) = dao.deletePlayer(player)

    override suspend fun clearAllPlayers() = dao.clearAllPlayers()
}
