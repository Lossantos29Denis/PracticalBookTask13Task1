package com.example.practicalbooktask13task1

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get reference to the article TextView
        val articleTextView = findViewById<TextView>(R.id.article)

        // Enable HTML formatting in the TextView
        val articleText = getString(R.string.article_text)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            articleTextView.text = Html.fromHtml(articleText, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            articleTextView.text = Html.fromHtml(articleText)
        }

        // Make links clickable
        articleTextView.movementMethod = LinkMovementMethod.getInstance()
    }
}