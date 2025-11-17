package com.example.lr4.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lr4.data.Player
import com.example.lr4.domain.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Модель представления для управления состоянием списка игроков и результатом распределения команд.
 *
 * Отвечает за обработку пользовательских действий и взаимодействие с [PlayerRepository].
 *
 * @property repository Репозиторий игроков.
 */
@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val repository: PlayerRepository
) : ViewModel() {

    /** Поток со всеми игроками, автоматически обновляемый при изменениях в базе данных. */
    val players = repository.getAllPlayers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Внутренний StateFlow, хранящий результаты распределения игроков по командам. */
    private val _teamResult = MutableStateFlow(TeamDrawResult())
    /** Публичный поток результатов для UI. */
    val teamResult = _teamResult.asStateFlow()

    /**
     * Добавляет нового игрока с указанным именем и тегом.
     *
     * @param name Имя игрока.
     * @param tag Дополнительный тег.
     */
    fun addPlayer(name: String, tag: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            repository.addOrUpdatePlayer(Player(name = name.trim(), tag = tag.trim()))
        }
    }

    /**
     * Обновляет данные существующего игрока.
     *
     * @param player Игрок для обновления.
     */
    fun updatePlayer(player: Player) {
        viewModelScope.launch { repository.addOrUpdatePlayer(player) }
    }

    /**
     * Удаляет игрока из базы данных.
     *
     * @param player Игрок для удаления.
     */
    fun deletePlayer(player: Player) {
        viewModelScope.launch { repository.deletePlayer(player) }
    }

    /** Удаляет всех игроков из базы данных. */
    fun clearAllPlayers() {
        viewModelScope.launch { repository.clearAllPlayers() }
    }

    /**
     * Формирует команды из списка присутствующих игроков.
     *
     * Игроки перемешиваются случайным образом и делятся на две команды:
     * **красную** и **зелёную**. Если игроков меньше двух — результат очищается.
     */
    fun performDraw() {
        val allPlayers = players.value
        val presentPlayers = allPlayers.filter { it.isPresent }

        if (presentPlayers.size < 2) {
            _teamResult.value = TeamDrawResult()
            return
        }

        val shuffledPlayers = presentPlayers.shuffled()
        val midpoint = (shuffledPlayers.size + 1) / 2
        val redTeam = shuffledPlayers.subList(0, midpoint)
        val greenTeam = shuffledPlayers.subList(midpoint, shuffledPlayers.size)

        _teamResult.value = TeamDrawResult(redTeam = redTeam, greenTeam = greenTeam)
    }
}

/**
 * Модель данных, описывающая результат распределения игроков по командам.
 *
 * @property redTeam Список игроков красной команды.
 * @property greenTeam Список игроков зелёной команды.
 */
data class TeamDrawResult(
    val redTeam: List<Player> = emptyList(),
    val greenTeam: List<Player> = emptyList()
)
