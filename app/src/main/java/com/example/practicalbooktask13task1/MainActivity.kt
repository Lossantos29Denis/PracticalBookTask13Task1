package com.example.practicalbooktask13task1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practicalbooktask13task1.ui.theme.PracticalBookTask13Task1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticalBookTask13Task1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ArticleScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ArticleScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Article Heading
        Text(
            text = stringResource(id = R.string.article_title),
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.colorPrimary))
                .padding(dimensionResource(id = R.dimen.padding_regular)),
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        // Article Subheading
        Text(
            text = stringResource(id = R.string.article_subtitle),
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_regular)),
            style = MaterialTheme.typography.bodyLarge
        )

        // Scrollable Article Text
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(dimensionResource(id = R.dimen.padding_regular))
        ) {
            val articleText = parseHtmlText(stringResource(id = R.string.article_text))
            Text(
                text = articleText,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = (MaterialTheme.typography.bodyMedium.lineHeight.value +
                    dimensionResource(id = R.dimen.line_spacing).value).sp
            )
        }
    }
}

@Composable
fun parseHtmlText(text: String) = buildAnnotatedString {
    val urlPattern = Regex("(https?://[^\\s]+|www\\.[^\\s]+)")
    var currentIdx = 0

    while (currentIdx < text.length) {
        when {
            text.startsWith("<b><i>", currentIdx) -> {
                val endTag = text.indexOf("</i></b>", currentIdx)
                if (endTag != -1) {
                    val content = text.substring(currentIdx + 6, endTag)
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)) {
                        append(content)
                    }
                    currentIdx = endTag + 8
                } else {
                    append(text[currentIdx])
                    currentIdx++
                }
            }
            text.startsWith("<b>", currentIdx) -> {
                val endTag = text.indexOf("</b>", currentIdx)
                if (endTag != -1) {
                    val content = text.substring(currentIdx + 3, endTag)
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(content)
                    }
                    currentIdx = endTag + 4
                } else {
                    append(text[currentIdx])
                    currentIdx++
                }
            }
            text.startsWith("<i>", currentIdx) -> {
                val endTag = text.indexOf("</i>", currentIdx)
                if (endTag != -1) {
                    val content = text.substring(currentIdx + 3, endTag)
                    withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                        append(content)
                    }
                    currentIdx = endTag + 4
                } else {
                    append(text[currentIdx])
                    currentIdx++
                }
            }
            else -> {
                // Check for URLs
                val remainingText = text.substring(currentIdx)
                val urlMatch = urlPattern.find(remainingText)
                if (urlMatch != null && urlMatch.range.first == 0) {
                    withStyle(
                        style = SpanStyle(
                            color = Color.Blue,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append(urlMatch.value)
                    }
                    currentIdx += urlMatch.value.length
                } else {
                    append(text[currentIdx])
                    currentIdx++
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArticleScreenPreview() {
    PracticalBookTask13Task1Theme {
        ArticleScreen()
    }
}