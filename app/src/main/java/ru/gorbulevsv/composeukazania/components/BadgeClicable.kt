package ru.gorbulevsv.composeukazania.components

import android.service.autofill.OnClickAction
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BadgeClicable(
   text: String,
   onClick: () -> Unit = {},
   background: Color = MaterialTheme.colorScheme.background,
   color: Color = MaterialTheme.colorScheme.onPrimary,
   radius: Dp = 5.dp,
   padding: PaddingValues = PaddingValues(6.dp, 4.dp),
   modifier: Modifier = Modifier,
   fontFamily: FontFamily = FontFamily.Default) {
   Box(
      modifier = modifier
         .background(
            color = background, shape = RoundedCornerShape(radius)
         )
         .clickable(true, "", null, null, onClick)
         .padding(padding)
   ) {
      Text(
         text, color = color, fontFamily = fontFamily
      )
   }
}
