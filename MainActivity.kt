package com.example.mynotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appName: String = "Мои заметки"
        var noteCount: Int = 3
        val greeting: String = "Привет из Kotlin!"
        Log.d("MainActivity", "App: $appName")
        Log.d("MainActivity", "Заметок: $noteCount")
        Log.d("MainActivity", "Привет: ${greeting.length} символов")
        val safeText: String = "Точно текст"
        var riskyText: String? = null
        Log.d("MainActivity", "Safe: ${safeText.length}")
        Log.d("MainActivity", "Risky: ${riskyText?.length ?: 0}")

        val textView: TextView = findViewById(R.id.textHello)
        val editText: EditText = findViewById(R.id.editTextNote)
        val button: Button = findViewById(R.id.buttonAdd)

        button.setOnClickListener {
            val inputText = editText.text.toString()
            if (inputText.isNotEmpty()) {        // Проверка пустоты
                Log.d("NotesApp", "Заметка: '$inputText'")
                editText.text.clear()            // Очистить поле
            } else {
                Log.d("NotesApp", "Пустая заметка!")
            }
        }

        val testNote = Note.Note(1, "Первая тестовая заметка", "13.04.2026")
        val testNoteCopy = testNote.copy(text = "Измененный текст")

        Log.d("NotesApp", "Оригинал: $testNote")
        Log.d("NotesApp", "Копия: $testNoteCopy")

        Log.d("NotesApp", "Равны ли? ${testNote == testNoteCopy}")

        val notesList = listOf(
            Note.Note(1, "Купить молоко и хлеб в магазине", "08.04.2026 09:30"),
            Note.Note(2, "Позвонить маме вечером после работы", "08.04.2026 18:00"),
            Note.Note(3, "Сходить в спортзал на тренировку", "08.04.2026 19:00"),
            Note.Note(4, "Прочитать статью про Jetpack Compose", "08.04.2026 20:30"),
            Note.Note(5, "Подготовить отчет для начальника", "09.04.2026 10:00")
        )
// ТЕСТИРУЕМ СПИСОК — выводим подробную информацию
        Log.d("NotesApp", "Всего заметок в приложении: ${notesList.size}")
        Log.d("NotesApp", "Первая заметка: ${notesList[0]}")
        Log.d("NotesApp", "Последняя заметка: ${notesList.last()}")
        Log.d("NotesApp", "Полный список:\n${notesList.joinToString("\n")}")
//  Магия Compose - заменяет XML!

        setContent {
            MaterialTheme {  // Современная Material3 тема
                NotesScreen(notesList)  // Главный экран приложения
            }
        }

        // Главный экран со списком (Composable функция!)
        @Composable
        fun NotesScreen(notes: List<Note>) {
            LazyColumn {
                items(notes) { note ->           // Для КАЖДОЙ заметки из списка
                    NoteItem(note = note)        // ← КОМПОЗИЦИЯ! Вызываем маленькую функцию
                }
            }

        fun NoteItem(note: Note) {
            // Material Design 3 карточка со скругленными углами
            Card(
                modifier = Modifier
                    .fillMaxWidth()                    // На всю ширину экрана
                    .padding(horizontal = 16.dp, vertical = 4.dp),  // Отступы между карточками
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(12.dp)      // Скругленные углы 12dp
            ) {
                // Внутри карточки — вертикальный контейнер Column
                Column(
                    modifier = Modifier.padding(16.dp)  // Отступы внутри карточки
                ) {
                    // ЗАГОЛОВОК ЗАМЕТКИ (жирный крупный текст)
                    Text(
                        text = note.text,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // ПРОБЕЛ между заголовком и датой
                    Spacer(modifier = Modifier.height(8.dp))

                    // ДАТА СОЗДАНИЯ (мелкий серый текст)
                    Text(
                        text = note.date,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        }
    }
}
