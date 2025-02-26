package ru.gorbulevsv.composeukazania.components

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.ireward.htmlcompose.HtmlText
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import ru.gorbulevsv.composeukazania.models.Ukazania
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(DelicateCoroutinesApi::class)
@Composable
fun MyTextHtml(
    date: LocalDate,
    fontSize: TextUnit = 20.sp,
    lineHeight: TextUnit = 27.sp
) {
    var html = remember { mutableStateOf("") }

    rememberCoroutineScope().launch {
        html.value = getHtml(date)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        HtmlText(
            text = html.value,
            modifier = Modifier
                .padding(15.dp),
            fontSize = fontSize,
            style = TextStyle(
                fontSize = fontSize,
                lineHeight = lineHeight,
                fontFamily = FontFamily.Serif,
                color = if (isSystemInDarkTheme()) Color.White.copy(.9f) else Color.Black,
                textAlign = TextAlign.Start
            ),
            linkClicked = { link ->

            },
            URLSpanStyle = SpanStyle(
                color = if (isSystemInDarkTheme()) Color.White.copy(.75f) else Color.Black.copy(.75f),
                fontSize = fontSize * .7,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

suspend fun getHtml(date: LocalDate): String {
    var ukazania by mutableStateOf(Gson().toJson(Ukazania()))
    var url = "https://api.patriarchia.ru/v1/events/${
        DateTimeFormatter.ofPattern("yyyy-MM-dd").format(date.minusDays(13))
    }"
    try {
        val client = HttpClient(CIO)
        ukazania = client.get(url) {
            contentType(ContentType.Application.Json.withParameter("charset", "utf-8"))
        }.body()
        client.close()
    } catch (e: Exception) {
    }

    val result = Gson().fromJson<Ukazania>(
        ukazania,
        Ukazania::class.java
    ).content.replace("${DateTimeFormatter.ofPattern("d").format(date.minusDays(13))}. ", "")

    return if (result.trim() != "") result else "<div>На данную дату указания отсутствуют, возможно они появятся позже!</div>"
}