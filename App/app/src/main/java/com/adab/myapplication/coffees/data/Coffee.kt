package com.adab.myapplication.coffees.data

import java.util.*

data class Coffee (
    val _id: String,
    var originName: String,
    var roastedDate: Date,
    var popular: Boolean
    ){
    override fun toString(): String = originName
}