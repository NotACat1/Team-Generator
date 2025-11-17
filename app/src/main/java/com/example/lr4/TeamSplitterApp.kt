package com.example.lr4

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Класс [Application], являющийся точкой входа для Dagger Hilt.
 *
 * Обеспечивает инициализацию графа зависимостей на уровне приложения.
 */
@HiltAndroidApp
class TeamSplitterApp : Application()
