package ru.gorbulevsv.composeukazania.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun FieldCheckBox(
   title: String,
   onClick: (v: Boolean) -> Unit = {},
   isChecked: MutableState<Boolean> = mutableStateOf(true),
   background: Color = MaterialTheme.colorScheme.background,
   color: Color = MaterialTheme.colorScheme.onPrimary,
   padding: PaddingValues = PaddingValues(
      start = 9.dp, end = 16.dp, top = 9.dp, bottom = 9.dp
   ),
   radius: Dp = 5.dp,
   modifier: Modifier = Modifier) {
   Row(
      modifier = modifier
         .fillMaxWidth()
         .height(IntrinsicSize.Min)
   ) {
      Button(
         contentPadding = padding,
         shape = RoundedCornerShape(radius),
         colors = ButtonDefaults.buttonColors(
            containerColor = background.copy(.1f), contentColor = color
         ),
         modifier = Modifier.weight(1f),
         onClick = { isChecked.value = !isChecked.value; onClick(isChecked.value) }) {
         Row(
            modifier = Modifier
               .fillMaxWidth()
               .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
         ) {
            Text(title)
            Box(
               modifier = Modifier
                  .clip(RoundedCornerShape(4.dp))
                  .clickable(
                     true,
                     "",
                     null,
                     interactionSource = null,
                     { isChecked.value = !isChecked.value; onClick(isChecked.value) })
            ) {
               if (isChecked.value) {
                  Icon(Icons.Default.CheckBox, "")
               } else {
                  Icon(
                     Icons.Default.CheckBoxOutlineBlank, ""
                  )
               }
            }
         }
      }
   }
}
