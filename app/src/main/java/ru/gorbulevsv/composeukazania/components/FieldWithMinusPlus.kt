package ru.gorbulevsv.composeukazania.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun FieldWithMinusPlus(
   title: String,
   badge: @Composable () -> Unit = {},
   onClick: () -> Unit = {},
   onMinus: () -> Unit = {},
   onPlus: () -> Unit = {},
   dialog: @Composable () -> Unit = {},
   background: Color = MaterialTheme.colorScheme.background,
   color: Color = MaterialTheme.colorScheme.onPrimary,
   padding: PaddingValues = PaddingValues(9.dp),
   radius: Dp = 5.dp,
) {
   Row(
      modifier = Modifier
         .fillMaxWidth()
         .height(IntrinsicSize.Min)
   ) {
      Button(
         contentPadding = PaddingValues(0.dp),
         shape = RoundedCornerShape(topStart = radius, bottomStart = radius),
         colors = ButtonDefaults.buttonColors(
            containerColor = background.copy(.1f), contentColor = color
         ),
         modifier = Modifier.weight(1f),
         onClick = onClick
      ) {
         Row(
            modifier = Modifier
               .fillMaxWidth()
               .padding(
                  start = padding.calculateLeftPadding(
                     LayoutDirection.Ltr
                  )
               )
               .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp, alignment = Alignment.Start)
         ) {
            Text(title)
            badge()
         }
      }

      Row(
         modifier = Modifier
            .width(IntrinsicSize.Min)
            .height(IntrinsicSize.Min)
      ) {
         Button(
            contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(0.dp),
            colors = ButtonDefaults.buttonColors(
               containerColor = background.copy(.3f), contentColor = color
            ),
            modifier = Modifier.weight(1f),
            onClick = onMinus
         ) {
            Text("-")
         }
         Button(
            contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(0.dp, radius, radius, 0.dp),
            colors = ButtonDefaults.buttonColors(
               containerColor = background.copy(.3f), contentColor = color
            ),
            modifier = Modifier.weight(1f),
            onClick = onPlus
         ) {
            Text("+")
         }
      }
      dialog()
   }
}
