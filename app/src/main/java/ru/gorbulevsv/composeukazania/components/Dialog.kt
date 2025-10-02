package ru.gorbulevsv.composeukazania.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog as SystemDialog

@Composable
fun Dialog(
   title: String = "",
   isShow: MutableState<Boolean> = mutableStateOf(true),
   subTitle: @Composable () -> Unit = {},
   content: @Composable () -> Unit = {}) {
   if (isShow.value) {
      SystemDialog(
         onDismissRequest = { isShow.value = false }) {
         Surface(
            modifier = Modifier
               .wrapContentWidth()
               .wrapContentHeight(),
            shape = MaterialTheme.shapes.medium,
            tonalElevation = AlertDialogDefaults.TonalElevation
         ) {
            Column(modifier = Modifier.padding(bottom = 12.dp, top = 3.dp)) {
               // Заголовок
               Box(
                  modifier = Modifier
                     .fillMaxWidth()
                     .padding(
                        start = 18.dp, end = 3.dp
                     ), contentAlignment = Alignment.CenterEnd
               ) {
                  IconButton(onClick = { isShow.value = false }) {
                     Icon(
                        Icons.Default.Close, contentDescription = "Закрыть"
                     )
                  }
                  Text(
                     text = title,
                     modifier = Modifier.fillMaxWidth(),
                     fontWeight = FontWeight.Bold,
                     fontSize = 18.sp,
                     textAlign = TextAlign.Start
                  )
               }
               // Панель, в которую можно поместить доп. описание или любой контент
               subTitle()
               // Содержимое
               Column(
                  modifier = Modifier
                     .fillMaxWidth()
                     .verticalScroll(
                        rememberScrollState()
                     )
               ) {
                  content()
               }
            }
         }
      }
   }
}
