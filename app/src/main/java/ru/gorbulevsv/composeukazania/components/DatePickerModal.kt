package ru.gorbulevsv.composeukazania.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
   onDateSelected: (Long?) -> Unit,
   onDismiss: () -> Unit,
   date: LocalDate,
   colors: DatePickerColors = DatePickerDefaults.colors()) {
   val datePickerState = rememberDatePickerState(
      initialSelectedDateMillis = date.toEpochSecond(
         LocalTime.parse("00:00:00"), ZoneOffset.UTC
      ) * 1000
   )
   DatePickerDialog(
      shape = MaterialTheme.shapes.medium,
                    onDismissRequest = onDismiss,
                    confirmButton = {
                       TextButton(onClick = {
                          onDateSelected(datePickerState.selectedDateMillis)
                          onDismiss()
                       }) {
                          Text("OK")
                       }
                    },
                    dismissButton = {
                       TextButton(onClick = onDismiss) {
                          Text("Отмена")
                       }
                    }) {
      DatePicker(
         title = {
            Text(
               text = "Выберите дату (н.ст.):",
               modifier = Modifier
                  .fillMaxWidth()
                  .padding(
                     top = 14.dp, start = 14.dp, end = 14.dp
                  ),
               fontSize = 18.sp,
               textAlign = TextAlign.Center,
               fontWeight = FontWeight.Bold
            )
         }, state = datePickerState, colors = colors
      )
   }
}