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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.ceil
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import ru.gorbulevsv.composeukazania.components.BottomCalendar
import ru.gorbulevsv.composeukazania.components.DatePickerModal
import ru.gorbulevsv.composeukazania.components.MyButton
import ru.gorbulevsv.composeukazania.components.MyTextHtml
import ru.gorbulevsv.composeukazania.ui.theme.Color21
import ru.gorbulevsv.composeukazania.ui.theme.Color22
import ru.gorbulevsv.composeukazania.ui.theme.Color33
import ru.gorbulevsv.composeukazania.ui.theme.Color36
import ru.gorbulevsv.composeukazania.ui.theme.Color4
import ru.gorbulevsv.composeukazania.ui.theme.ColorInfo
import ru.gorbulevsv.composeukazania.ui.theme.ComposeUkazaniaTheme
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.ZoneOffset
import java.util.Date

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
    val regex =
        """<div class="main" id="main">[.\s\S]*</div>[.\s\S]*<div class="sidebar">""".toRegex()

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint(
        "CoroutineCreationDuringComposition", "SetJavaScriptEnabled",
        "UnrememberedMutableState"
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
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                // Состояние сети изменилось
            }

            override fun onLinkPropertiesChanged(
                network: Network,
                linkProperties: LinkProperties
            ) {
                // Сеть по умолчанию изменила свойства соединения
            }
        })

        enableEdgeToEdge()
        setContent {
            ComposeUkazaniaTheme {
                var date = mutableStateOf(LocalDate.now())
                val pullToRefreshState = rememberPullToRefreshState()
                val coroutineScope = rememberCoroutineScope()
                var pagerState = rememberPagerState(
                    pageCount = { pageCount }, initialPage = centralPage
                )
                Scaffold(
                    topBar = {
                        Column(
                            modifier = Modifier
                                .background(colorTopBottomAppBar)
                                .padding(
                                    WindowInsets.systemBars
                                        .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
                                        .asPaddingValues()
                                )
                                .padding(horizontal = padding, vertical = 10.dp)
                                .fillMaxWidth(),
                        ) {
                            Text(
                                "Богослужебные указания".uppercase(),
                                color = Color4,
                                fontWeight = FontWeight.Bold,
                                fontSize = fontSize * .9,
                                fontFamily = FontFamily.SansSerif
                            )
                            if (isInternetConnected) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (isNewStyle) {
                                            DateTimeFormatter.ofPattern("E., d MMMM yyyy")
                                                .format(date.value.plusDays((pagerState.currentPage - centralPage).toLong()))
                                        } else {
                                            DateTimeFormatter.ofPattern("E., ")
                                                .format(date.value.plusDays((pagerState.currentPage - centralPage).toLong())) +
                                                    DateTimeFormatter.ofPattern("d MMMM yyyy")
                                                        .format(date.value.plusDays((pagerState.currentPage - centralPage).toLong() - 13))
                                        },
                                        fontSize = fontSize * .9,
                                        color = colorLight,
                                        fontFamily = FontFamily.Default,
                                        modifier = Modifier.clickable(
                                            onClick = {
                                                coroutineScope.launch {
                                                    pagerState.animateScrollToPage(centralPage)
                                                }
                                            }
                                        )
                                    )
                                    Text(
                                        text = if (isNewStyle) "новый ст." else "старый ст.",
                                        modifier = Modifier
                                            .padding(start = 10.dp)
                                            .background(
                                                colorButton,
                                                shape = RoundedCornerShape(borderRadius)
                                            )
                                            .clickable(onClick = { isNewStyle = !isNewStyle })
                                            .padding(horizontal = 10.dp, vertical = 4.dp),
                                        color = colorButtonText,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                }
                            }
                        }
                    },
                    bottomBar = {
                        if (isInternetConnected) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .background(colorTopBottomAppBar)
                                    .padding(
                                        WindowInsets.systemBars
                                            .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
                                            .asPaddingValues()
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
                                    }
                                )
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
                                    }
                                )
                            }
                        }
                    }
                ) {
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
                                    modifier = Modifier
                                        .align(Alignment.TopCenter),
                                    isRefreshing = isRefreshing,
                                    containerColor = Color36,
                                    color = colorLight,
                                    state = pullToRefreshState
                                )
                            },
                        ) {
                            if (isInternetConnected) {
                                HorizontalPager(
                                    state = pagerState,
                                    beyondViewportPageCount = 1
                                ) { pageIndex ->
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .verticalScroll(rememberScrollState()),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        if (!isRefreshing) {
                                            MyTextHtml(
                                                date = date.value.plusDays((pageIndex - centralPage).toLong())
                                            )
                                        }
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
                                        10.dp,
                                        alignment = Alignment.CenterVertically
                                    )
                                ) {
                                    Text(
                                        "Интернет отсутствует".uppercase(),
                                        modifier = Modifier.fillMaxWidth(),
                                        fontSize = fontSize,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        "Проверьте, пожалуйста, включен ли интернет на вашем устройстве",
                                        modifier = Modifier.fillMaxWidth(),
                                        fontSize = fontSize, textAlign = TextAlign.Center
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

fun getUrl(date: LocalDate): String {
    return "http://www.patriarchia.ru/bu/${
        DateTimeFormatter.ofPattern("yyyy-MM-dd").format(date)
    }/print.html"
}

suspend fun getHtml(url: String): String {
    var result = mutableStateOf("")
    try {
        val client = HttpClient(CIO)
        val response: HttpResponse = client.get(url)
        result.value = response.body()
        client.close()
    } catch (e: Exception) {
    }
    return result.value
}

suspend fun getClearHtml(date: LocalDate): String {
    var url = "http://www.patriarchia.ru/bu/${
        DateTimeFormatter.ofPattern("yyyy-MM-dd").format(date)
    }/print.html"
    val regex =
        """<div class="main" id="main">[.\s\S]*</div>[.\s\S]*<div class="sidebar">""".toRegex()
    var result = regex.find(getHtml(url))?.value.toString()
        .replace("\n\r", "")
        .replace("""<div class="main" id="main">""", "")
        .replace("""</div><div class="sidebar">""", "")
        .replace("Богослужебные указания за", "")
        .replace(date.minusDays(13).dayOfMonth.toString() + ". ", "")
        .replace(
            DateTimeFormatter.ofPattern("d MMMM yyyy")
                .format(date) + " года", ""
        )
    return result
}