package com.example.lr4.di

import android.content.Context
import androidx.room.Room
import com.example.lr4.data.AppDatabase
import com.example.lr4.data.PlayerDao
import com.example.lr4.domain.PlayerRepository
import com.example.lr4.domain.PlayerRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt-модуль для предоставления зависимостей уровня приложения.
 *
 * Содержит фабрики для создания экземпляров базы данных Room,
 * DAO и репозитория игроков.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Предоставляет экземпляр базы данных [AppDatabase].
     *
     * @param context Контекст приложения.
     * @return Инициализированная база данных Room.
     */
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "team_splitter_db"
        ).build()

    /**
     * Предоставляет [PlayerDao] для доступа к таблице игроков.
     *
     * @param database Экземпляр базы данных [AppDatabase].
     * @return DAO-интерфейс [PlayerDao].
     */
    @Provides
    @Singleton
    fun providePlayerDao(database: AppDatabase): PlayerDao = database.playerDao()

    /**
     * Предоставляет реализацию [PlayerRepository].
     *
     * @param dao DAO для работы с таблицей игроков.
     * @return Репозиторий [PlayerRepositoryImpl].
     */
    @Provides
    @Singleton
    fun providePlayerRepository(dao: PlayerDao): PlayerRepository =
        PlayerRepositoryImpl(dao)
}
