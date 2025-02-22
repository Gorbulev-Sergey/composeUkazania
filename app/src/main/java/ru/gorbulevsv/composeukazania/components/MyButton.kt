package ru.gorbulevsv.composeukazania.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MyButton(
    text: String = "",
    onClick: () -> Unit = {},
    color: Color = Color.Black,
    background: Color = Color.LightGray,
    shape: RoundedCornerShape = RoundedCornerShape(0.dp),
    fontSize: TextUnit = 20.sp
) {
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = background,
            contentColor = color
        ),
        shape = shape,
        onClick = onClick
    ) {
        Text(
            text = text,
            color = color,
            modifier = Modifier.padding(horizontal = 0.dp, vertical = 5.dp),
            softWrap = false,
            fontSize = fontSize
        )
    }
}