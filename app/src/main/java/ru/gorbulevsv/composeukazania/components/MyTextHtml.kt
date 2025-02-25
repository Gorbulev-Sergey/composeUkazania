package ru.gorbulevsv.composeukazania.components

import android.annotation.SuppressLint
import androidx.compose.foundation.content.MediaType.Companion.HtmlText
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.ireward.htmlcompose.HtmlText
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import ru.gorbulevsv.composeukazania.getClearHtml
import java.time.LocalDate

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(DelicateCoroutinesApi::class)
@Composable
fun MyTextHtml(
    date: LocalDate,
    fontSize: TextUnit = 20.sp,
    lineHeight: TextUnit = 27.sp
) {
    var html = remember { mutableStateOf("") }
    var isHtmlContainsUkazania = remember { mutableStateOf(true) }

    rememberCoroutineScope().launch {
        html.value = getClearHtml(date)
        isHtmlContainsUkazania.value = !html.value.contains("Страница не найдена")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = if (isHtmlContainsUkazania.value) Arrangement.Top else Arrangement.Center
    ) {
        HtmlText(
            text = if (isHtmlContainsUkazania.value) html.value else "</br></br></br><div>На данную дату указания отсутствуют, возможно они появятся позже!</div>",
            modifier = Modifier
                .offset(0.dp, (-30).dp)
                .padding(15.dp),
            fontSize = fontSize,
            style = TextStyle(
                fontSize = fontSize,
                lineHeight = lineHeight,
                fontFamily = FontFamily.Serif,
                color = if (isSystemInDarkTheme()) Color.White.copy(.9f) else Color.Black,
                textAlign = if (isHtmlContainsUkazania.value) TextAlign.Start else TextAlign.Center
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