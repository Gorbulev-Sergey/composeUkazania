package ru.gorbulevsv.composeukazania.models

import kotlinx.serialization.Serializable

@Serializable
data class Ukazania(val day: String = "", val calendar_text: String = "", val content: String = "")