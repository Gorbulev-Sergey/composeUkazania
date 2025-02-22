package ru.gorbulevsv.composeukazania.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun BottomCalendar(
    date: LocalDate = LocalDate.now(),
    onClick: () -> Unit = {},
    background: Color,
    color: Color,
    borderRadius: Dp = 0.dp,
    fontSize: TextUnit = 20.sp,
    padding: PaddingValues = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
) {
    Row(
        modifier = Modifier
            .background(
                background,
                shape = RoundedCornerShape(borderRadius)
            )
            .clickable(onClick = onClick)
            .padding(padding)
    ) {
        Text(
            text = DateTimeFormatter.ofPattern("d MMMM").format(date),
            fontSize = fontSize,
            color = color,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold
        )
//        Text(
//            text = DateTimeFormatter.ofPattern("MMMM").format(date),
//            fontSize = fontSize,
//            color = color,
//            textAlign = TextAlign.Center,
//            fontFamily = FontFamily.Default
//        )

    }

}