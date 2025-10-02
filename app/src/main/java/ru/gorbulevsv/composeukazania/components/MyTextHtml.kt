package ru.gorbulevsv.composeukazania.components

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.LocaleList
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

const val MESSAGE: String = "<b>ПОКА НИЧЕГО НЕТ</b><div>На данную дату указания отсутствуют, возможно они появятся позже!</div>"

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(DelicateCoroutinesApi::class)
@Composable
fun MyTextHtml(
   date: LocalDate, fontSize: TextUnit = 20.sp, lineHeight: TextUnit = 27.sp) {
   var html by remember { mutableStateOf("") }
   val coroutineScope = rememberCoroutineScope()
   val scrollableState = rememberScrollState()
   val selectedLink = remember { mutableStateOf("") }

   coroutineScope.launch {
      html = getHtml(date)
   }

   Column(
      modifier = Modifier
         .fillMaxSize()
         .verticalScroll(scrollableState),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = if (html == MESSAGE) Arrangement.Center else Arrangement.Top
   ) {
      HtmlText(
         text = if (html == MESSAGE) MESSAGE else html,
         modifier = Modifier.padding(top = 5.dp, bottom = 15.dp, start = 15.dp, end = 15.dp),
         fontSize = fontSize,
         style = TextStyle(
            fontSize = fontSize,
            lineHeight = lineHeight,
            fontFamily = FontFamily.Serif,
            color = if (isSystemInDarkTheme()) Color.White.copy(.9f) else Color.Black,
            textAlign = if (html == MESSAGE) TextAlign.Center else TextAlign.Start
         ),
         linkClicked = { link ->
            selectedLink.value = link
         },
         URLSpanStyle = SpanStyle(
            color = if (isSystemInDarkTheme()) Color.White.copy(.75f) else Color.Black.copy(
               .75f
            ), fontSize = fontSize * .7, fontWeight = FontWeight.Bold
         )
      )
   }
}

suspend fun getHtml(date: LocalDate): String {
   var ukazania by mutableStateOf(Gson().toJson(Ukazania()))
   val url = "http://api.patriarchia.ru/v1/events/${
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
      ukazania, Ukazania::class.java
   ).content.replace("${DateTimeFormatter.ofPattern("d").format(date.minusDays(13))}. ", "")


   return if (result.trim() == "") MESSAGE else result
}
