package com.example.mapkit.app.model

import com.yandex.mapkit.geometry.Point
import java.io.Serializable

data class SearchResult(
    val id:Int =0,
    val name: String? =null,
    var address: String?=null,
    val distance: String?=null,
    val point:Point?=null
):Serializable