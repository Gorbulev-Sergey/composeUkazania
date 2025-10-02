package ru.gorbulevsv.composeukazania.ui.theme
import ru.gorbulevsv.composeukazania.R

data class MyFont(val title: String, val r: Int = 0, val url: String? = null)

val fonts = listOf(
   MyFont("Inter", R.font.inter),
   MyFont("Jost", R.font.jost),
   MyFont("Lora", R.font.lora),
   MyFont("Manrope", R.font.manrope),
   MyFont("Montserrat", R.font.montserrat),
   MyFont("Open Sans", R.font.open_sans),
   MyFont("Oswald", R.font.oswald),
   MyFont("PT Sans", R.font.pt_sans),
   MyFont("PT Serif", R.font.pt_serif),
   MyFont("Raleway", R.font.raleway),
   MyFont("Roboto", R.font.roboto),
   MyFont("Rubik", R.font.rubik),
   MyFont("Segoe UI", R.font.segoe_ui),
   MyFont("Times New Roman", R.font.times),
)