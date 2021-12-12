package com.adab.myapplication.coffees.data

import java.util.*

data class Coffee (
    val _id: String,
    var originName: String,
    var popular: String,
    var roastedDate: String,
    ){
    override fun toString(): String = originName
}