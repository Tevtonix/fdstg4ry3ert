package com.example.myapp

import com.example.myapplication10.R

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var editTextNote: EditText
    private lateinit var buttonAdd: Button
    private lateinit var recyclerViewNotes: RecyclerView

    private val notesList = mutableListOf<Note>()
    private lateinit var adapter: NoteAdapter

    private val prefsName = "notes_prefs"
    private val notesKey = "notes_key"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextNote = findViewById(R.id.editTextNote)
        buttonAdd = findViewById(R.id.buttonAdd)
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes)

        loadNotes()

        adapter = NoteAdapter(
            notes = notesList,
            onClick = { position ->
                showEditDialog(position)
            },
            onLongClick = { position ->
                deleteNote(position)
            }
        )

        recyclerViewNotes.layoutManager = LinearLayoutManager(this)
        recyclerViewNotes.adapter = adapter

        buttonAdd.setOnClickListener {
            val text = editTextNote.text.toString().trim()
            if (text.isNotEmpty()) {
                val currentDate = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                    .format(Date())

                notesList.add(
                    Note(
                        text = text,
                        createdAt = currentDate
                    )
                )
                adapter.notifyItemInserted(notesList.size - 1)
                saveNotes()
                editTextNote.text.clear()
            } else {
                Toast.makeText(this, "Введите текст заметки", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteNote(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Удаление")
            .setMessage("Удалить эту заметку?")
            .setPositiveButton("Да") { _, _ ->
                notesList.removeAt(position)
                adapter.notifyItemRemoved(position)
                saveNotes()
                Toast.makeText(this, "Заметка удалена", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Нет", null)
            .show()
    }

    private fun showEditDialog(position: Int) {
        val editText = EditText(this)
        editText.setText(notesList[position].text)

        AlertDialog.Builder(this)
            .setTitle("Редактировать заметку")
            .setView(editText)
            .setPositiveButton("Сохранить") { _, _ ->
                val newText = editText.text.toString().trim()
                if (newText.isNotEmpty()) {
                    notesList[position].text = newText
                    adapter.notifyItemChanged(position)
                    saveNotes()
                } else {
                    Toast.makeText(this, "Текст не может быть пустым", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun saveNotes() {
        val sharedPreferences = getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val jsonArray = JSONArray()
        for (note in notesList) {
            val jsonObject = JSONObject()
            jsonObject.put("text", note.text)
            jsonObject.put("createdAt", note.createdAt)
            jsonArray.put(jsonObject)
        }

        editor.putString(notesKey, jsonArray.toString())
        editor.apply()
    }

    private fun loadNotes() {
        val sharedPreferences = getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString(notesKey, null)

        if (jsonString != null) {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val text = jsonObject.getString("text")
                val createdAt = jsonObject.getString("createdAt")
                notesList.add(Note(text, createdAt))
            }
        }
    }
}
