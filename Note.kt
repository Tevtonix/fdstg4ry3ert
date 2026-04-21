package com.example.mynotes

class Note {
    val date: Any
    val text: Any

    // ЗАДАНИЕ №1: МОДЕЛЬ ДАННЫХ ДЛЯ НАШЕГО ПРИЛОЖЕНИЯ "МОИ ЗАМЕТКИ"
    data class Note(
        val id: Int,        // Уникальный номер заметки (как ключ в базе данных)
        val text: String,   // Основной текст заметки (что написал пользователь)
        val date: String    // Дата создания в формате "08.04.2026"
    )
}