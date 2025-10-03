package ru.gorbulevsv.composeukazania.components

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
   title: String = "",
   isShow: MutableState<Boolean>,
   horizontalPadding: Dp = 14.dp,
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
            Text(
               text = title,
               fontSize = 18.sp,
               fontWeight = FontWeight.Bold,
               modifier = Modifier
                  .padding(bottom = 7.dp)
                  .fillMaxWidth(),
               textAlign = TextAlign.Center
            )
            Column(modifier = Modifier
               .fillMaxWidth()
               .weight(1f)
               .verticalScroll(scrollState)) {
               content()
            }
            bottomPanel()
         }
      }
   }
}