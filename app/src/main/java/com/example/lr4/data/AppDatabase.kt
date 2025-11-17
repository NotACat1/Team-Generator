package com.example.lr4.data

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Главная точка доступа к базе данных приложения.
 *
 * Определяет все сущности (таблицы) и DAO-интерфейсы, используемые Room.
 * В данном случае содержит единственную таблицу — [Player].
 *
 * @see PlayerDao
 * @see Player
 */
@Database(entities = [Player::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Возвращает DAO для операций с таблицей игроков.
     *
     * @return [PlayerDao] для CRUD-операций с сущностью [Player].
     */
    abstract fun playerDao(): PlayerDao
}
