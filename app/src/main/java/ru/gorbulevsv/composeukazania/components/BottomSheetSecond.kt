package ru.gorbulevsv.composeukazania.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetSecond(
   title: String = "",
   actions: @Composable () -> Unit = {},
   isShow: MutableState<Boolean>,
   horizontalPadding: Dp = 15.dp,
   bottomPanel: @Composable () -> Unit = {},
   content: @Composable () -> Unit) {
   val sheetState = rememberModalBottomSheetState()
   if (isShow.value) {
      ModalBottomSheet(
         shape = MaterialTheme.shapes.medium,
         onDismissRequest = { isShow.value = false },
         sheetState = sheetState
      ) {
         val scrollState = rememberScrollState()
         Column(
            modifier = Modifier
               .padding(
                  bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
               )
               .padding(horizontalPadding, 0.dp, horizontalPadding, 14.dp)
               .fillMaxSize(),
         ) {
            Box(
               modifier = Modifier
                  .fillMaxWidth()
                  .padding(
                     start = 1.dp, end = 1.dp, bottom = 0.dp
                  ),
               contentAlignment = Alignment.Center
            ) {
               Text(
                  text = title,
                  fontSize = 18.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.fillMaxWidth(),
                  textAlign = TextAlign.Center
               )
               Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                  actions()
               }
            }

            Column(
               modifier = Modifier
                  .fillMaxWidth()
                  .weight(1f)
                  .verticalScroll(scrollState)
            ) {
               content()
            }
            bottomPanel()
         }
      }
   }
}