package ru.gorbulevsv.composeukazania

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import kotlinx.coroutines.launch
import ru.gorbulevsv.composeukazania.components.BottomCalendar
import ru.gorbulevsv.composeukazania.components.DatePickerModal
import ru.gorbulevsv.composeukazania.components.MyButton
import ru.gorbulevsv.composeukazania.components.MyTextHtml
import ru.gorbulevsv.composeukazania.ui.theme.Color21
import ru.gorbulevsv.composeukazania.ui.theme.Color22
import ru.gorbulevsv.composeukazania.ui.theme.Color33
import ru.gorbulevsv.composeukazania.ui.theme.Color36
import ru.gorbulevsv.composeukazania.ui.theme.ColorInfo
import ru.gorbulevsv.composeukazania.ui.theme.ComposeUkazaniaTheme
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import kotlin.math.ceil

class MainActivity : ComponentActivity() {
   var isInternetConnected by mutableStateOf(false)
   var isRefreshing by mutableStateOf(true)
   var isNewStyle by mutableStateOf(true)
   var isDateDialogShow by mutableStateOf(false)

   val pageCount = Int.MAX_VALUE - 1
   val centralPage = ceil((pageCount / 2).toDouble()).toInt()

   val borderRadius = 5.dp
   val padding = 14.dp
   val fontSize = 20.sp

   val colorTopBottomAppBar = Color33
   val colorButton = Color36.copy(.4f)
   val colorButtonText = Color22
   val colorDark = Color21
   val colorLight = Color22

   @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
   @OptIn(ExperimentalMaterial3Api::class)
   @SuppressLint(
      "CoroutineCreationDuringComposition", "SetJavaScriptEnabled", "UnrememberedMutableState"
   )
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      // Проверяем подключение к интернет (это делается до вызова Compose UI)
      val connectivityManager = getSystemService(ConnectivityManager::class.java)
      connectivityManager.registerDefaultNetworkCallback(object : NetworkCallback() {
         override fun onAvailable(network: Network) {
            // Интернет доступен
            isInternetConnected = true
         }

         override fun onLost(network: Network) {
            // Интернет потерян
            isInternetConnected = false
         }

         override fun onCapabilitiesChanged(
            network: Network, networkCapabilities: NetworkCapabilities) {
            // Состояние сети изменилось
         }

         override fun onLinkPropertiesChanged(
            network: Network, linkProperties: LinkProperties) {
            // Сеть по умолчанию изменила свойства соединения
         }
      })

      enableEdgeToEdge()
      setContent {
         ComposeUkazaniaTheme {
            var date = mutableStateOf(LocalDate.now())
            val pullToRefreshState = rememberPullToRefreshState()
            val coroutineScope = rememberCoroutineScope()
            val pagerState = rememberPagerState(
               pageCount = { pageCount }, initialPage = centralPage
            )

            val colorBackground = if (isSystemInDarkTheme()) colorDark else Color.White
            val colorText = if (isSystemInDarkTheme()) Color.White else colorDark
            Scaffold(topBar = {
               CenterAlignedTopAppBar(
                  navigationIcon = {
                  IconButton(
                     onClick = { isNewStyle = !isNewStyle },
                     modifier = Modifier.padding(start = 4.dp),
                     colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color("#D6C1A6".toColorInt())
                     )
                  ) {
                     Text(
                        text = if (isNewStyle) "н.\nст." else "ст.\nст.",
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center
                     )
                  }
               }, title = {
                  Column(horizontalAlignment = Alignment.CenterHorizontally) {
                     Text(
                        "Богослужебные указания",
                        style = MaterialTheme.typography.titleLarge
                     )
                     Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                           text = if (isNewStyle) {
                              DateTimeFormatter.ofPattern("E., d MMMM yyyy н.ст.").format(
                                 date.value.plusDays(
                                    (pagerState.currentPage - centralPage).toLong()
                                 )
                              )
                           } else {
                              DateTimeFormatter.ofPattern("E., ").format(
                                 date.value.plusDays(
                                    (pagerState.currentPage - centralPage).toLong()
                                 )
                              ) + DateTimeFormatter.ofPattern(
                                 "d MMMM yyyy ст.ст."
                              ).format(date.value.plusDays((pagerState.currentPage - centralPage).toLong() - 13))
                           },
                           style = MaterialTheme.typography.bodyLarge,
                           modifier = Modifier.clickable(
                              onClick = {
                                 coroutineScope.launch {
                                    pagerState.animateScrollToPage(centralPage)
                                 }
                              })
                        )
                     }
                  }
               }, actions = {
                  IconButton(onClick = {}) {
                     Icon(Icons.Default.Settings, "")
                  }
               }, colors = TopAppBarDefaults.topAppBarColors(
                  containerColor = Color("#E6D9C9".toColorInt()),
                  titleContentColor = MaterialTheme.colorScheme.onBackground,
                  navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                  actionIconContentColor = MaterialTheme.colorScheme.onBackground
               )
               )
            }, bottomBar = {
               val hide = true
               if (isInternetConnected && !hide) {
                  Row(
                     horizontalArrangement = Arrangement.SpaceBetween,
                     verticalAlignment = Alignment.CenterVertically,
                     modifier = Modifier
                        .background(colorTopBottomAppBar)
                        .padding(
                           WindowInsets.systemBars.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom).asPaddingValues()
                        )
                        .fillMaxWidth()
                        .padding(horizontal = padding, vertical = 9.dp)
                  ) {
                     MyButton(
                        text = "Назад",
                        color = colorButtonText,
                        background = Color.Transparent,
                        shape = RoundedCornerShape(borderRadius),
                        fontSize = 16.sp,
                        onClick = {
                           coroutineScope.launch {
                              pagerState.animateScrollToPage(pagerState.currentPage - 1)
                           }
                        })
                     BottomCalendar(
                        date = date.value.plusDays((pagerState.currentPage - centralPage).toLong()),
                        onClick = { isDateDialogShow = true },
                        fontSize = fontSize * .8,
                        color = colorLight,
                        background = colorButton,
                        borderRadius = borderRadius,
                        padding = PaddingValues(horizontal = 11.dp, vertical = 9.dp)
                     )
                     MyButton(
                        text = "Вперёд",
                        color = colorButtonText,
                        background = Color.Transparent,
                        shape = RoundedCornerShape(borderRadius),
                        fontSize = 16.sp,
                        onClick = {
                           coroutineScope.launch {
                              pagerState.animateScrollToPage(pagerState.currentPage + 1)
                           }
                        })
                  }
               }
            }, floatingActionButton = {
               FloatingActionButton(
                  onClick = { isDateDialogShow = true },
                  shape = MaterialTheme.shapes.small,
                  elevation = FloatingActionButtonDefaults.elevation(
                     defaultElevation = 0.dp, pressedElevation = 0.dp
                  ),
                  containerColor = Color("#E6D9C9".toColorInt()),
                  contentColor = MaterialTheme.colorScheme.onBackground,
                  modifier = Modifier.padding(bottom = 8.dp)
               ) {
                  Column(
                     horizontalAlignment = Alignment.CenterHorizontally,
                     modifier = Modifier.padding(8.dp, 6.dp)
                  ) {
                     Icon(Icons.Default.DateRange, "")
                     Text(
                        text = if (isNewStyle) {
                           DateTimeFormatter.ofPattern("d MMMM\nн.ст.").format(
                              date.value.plusDays(
                                 (pagerState.currentPage - centralPage).toLong()
                              )
                           )
                        } else {
                           DateTimeFormatter.ofPattern(
                              "d MMMM\nст.ст."
                           ).format(date.value.plusDays((pagerState.currentPage - centralPage).toLong() - 13))
                        },
                        textAlign = TextAlign.Center,
                        lineHeight = 14.sp
                     )
                  }
               }
            }) {

               Column(
                  modifier = Modifier
                     .fillMaxSize()
                     .background(if (isSystemInDarkTheme()) colorDark else Color.White),
                  horizontalAlignment = Alignment.CenterHorizontally,
                  verticalArrangement = Arrangement.Center
               ) {
                  PullToRefreshBox(
                     modifier = Modifier.padding(it),
                     state = pullToRefreshState,
                     isRefreshing = isRefreshing,
                     onRefresh = {
                        isRefreshing = true
                        coroutineScope.launch {
                           pagerState.animateScrollToPage(pagerState.currentPage)
                        }
                     },
                     indicator = {
                        Indicator(
                           modifier = Modifier.align(Alignment.TopCenter),
                           isRefreshing = isRefreshing,
                           containerColor = Color36,
                           color = colorLight,
                           state = pullToRefreshState
                        )
                     },
                  ) {
                     if (isInternetConnected) {
                        HorizontalPager(
                           state = pagerState, beyondViewportPageCount = 1
                        ) { pageIndex ->
                           if (!isRefreshing) {
                              MyTextHtml(
                                 date = date.value.plusDays((pageIndex - centralPage).toLong())
                              )
                           }
                        }
                     } else {
                        Column(
                           modifier = Modifier
                              .fillMaxSize()
                              .background(
                                 ColorInfo.copy(.75f)
                              )
                              .padding(horizontal = 20.dp, vertical = 50.dp),
                           verticalArrangement = Arrangement.spacedBy(
                              5.dp, alignment = Alignment.CenterVertically
                           )
                        ) {
                           Text(
                              "Интернет отсутствует".uppercase(),
                              modifier = Modifier.fillMaxWidth(),
                              fontSize = fontSize,
                              fontWeight = FontWeight.Bold,
                              textAlign = TextAlign.Center,
                              fontFamily = FontFamily.Serif
                           )
                           Text(
                              "Проверьте, пожалуйста, включен ли интернет на вашем устройстве!",
                              modifier = Modifier.fillMaxWidth(),
                              fontSize = fontSize,
                              textAlign = TextAlign.Center,
                              fontFamily = FontFamily.Serif
                           )
                        }
                     }

                     coroutineScope.launch {
                        isRefreshing = false
                     }
                  }

                  if (isDateDialogShow) {
                     DatePickerModal(
                        onDateSelected = { v ->
                        if (v != null) {
                           coroutineScope.launch {
                              val d = LocalDate.parse(
                                 dateFromLong(v.toLong()),
                                 DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                              )
                              pagerState.animateScrollToPage((centralPage + d.toEpochDay() - date.value.toEpochDay()).toInt())
                           }
                        }
                     },
                                     onDismiss = { isDateDialogShow = false },
                                     date = date.value.plusDays((pagerState.currentPage - centralPage).toLong())
                     )
                  }
               }
            }
         }
      }
   }
}

@SuppressLint("SimpleDateFormat")
fun dateFromLong(time: Long): String {
   val date = Date(time)
   val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
   return format.format(date)
}