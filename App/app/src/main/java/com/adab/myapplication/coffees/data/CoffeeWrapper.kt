package com.adab.myapplication.coffees.data

class CoffeeWrapper (
    var originName: String,
    var popular: String,
    var roastedDate: String,
){
    override fun toString(): String = "$originName $popular $roastedDate"
}