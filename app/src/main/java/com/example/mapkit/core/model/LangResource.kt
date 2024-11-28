package com.example.mapkit.core.model


data class LangResource(
    val name: Int,
    val label: Int
)


enum class AppLanguages(
    val value: String,
    val firebaseTopic: String
) {
    UZBEK("uz", "ANDROID_TOPIC_UZ"),
    TURKISH("tr", "ANDROID_TOPIC_TR"),
    ENGLISH("en", "ANDROID_TOPIC_EN"),
    RUSSIAN("ru", "ANDROID_TOPIC_RU")
}