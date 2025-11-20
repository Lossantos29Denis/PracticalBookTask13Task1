package com.example.practicalbooktask13task1

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private var commentCounter = 0
    private lateinit var scrollView: ScrollView
    private lateinit var commentsContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get reference to the article TextView
        val articleTextView = findViewById<TextView>(R.id.article)

        // Enable HTML formatting in the TextView (API 24+ always uses FROM_HTML_MODE_LEGACY)
        val articleText = getString(R.string.article_text)
        articleTextView.text = Html.fromHtml(articleText, Html.FROM_HTML_MODE_LEGACY)

        // Make links clickable
        articleTextView.movementMethod = LinkMovementMethod.getInstance()

        // Get references to ScrollView, comments container and button
        scrollView = findViewById(R.id.scroll_view)
        commentsContainer = findViewById(R.id.comments_container)
        val addCommentButton = findViewById<Button>(R.id.button_add_comment)

        // Set up button click listener
        addCommentButton.setOnClickListener {
            showAddCommentDialog()
        }
    }

    private fun showAddCommentDialog() {
        // Create EditText for user input
        val editText = EditText(this).apply {
            hint = "Escribe tu comentario aquí..."
            setPadding(40, 40, 40, 40)
            minLines = 3
            maxLines = 6
        }

        // Create and show AlertDialog
        AlertDialog.Builder(this)
            .setTitle("Agregar Comentario")
            .setMessage("Escribe tu opinión sobre el artículo:")
            .setView(editText)
            .setPositiveButton("Publicar") { dialog, _ ->
                val commentText = editText.text.toString().trim()
                if (commentText.isNotEmpty()) {
                    addComment(commentText)
                } else {
                    Toast.makeText(this, "El comentario no puede estar vacío", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun addComment(userComment: String) {
        commentCounter++

        // Create a new TextView for the comment
        val commentView = TextView(this).apply {
            // Get current date and time
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val currentDate = dateFormat.format(Date())

            // Set comment text with HTML formatting
            val commentText = "<b>Usuario $commentCounter</b> - <i>$currentDate</i><br/>" +
                    userComment

            text = Html.fromHtml(commentText, Html.FROM_HTML_MODE_LEGACY)

            // Style the comment
            setPadding(20, 20, 20, 20)
            textSize = 14f
            setBackgroundColor(ContextCompat.getColor(this@MainActivity, android.R.color.darker_gray))
            setTextColor(ContextCompat.getColor(this@MainActivity, android.R.color.black))

            // Set layout params with margin
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 16)
            }
        }

        // Add the comment to the container
        commentsContainer.addView(commentView)

        // Scroll to the bottom to show the new comment
        commentView.post {
            scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }

        // Show confirmation
        Toast.makeText(this, "Comentario agregado", Toast.LENGTH_SHORT).show()
    }
}