package com.korlab.foodex.delivery.Data

class Name(first: String, last: String, middle: String) {

    var first: String = ""
    var last: String = ""
    var middle: String? = ""

    init {
        this.first = first
        this.last = last
        this.middle = middle
    }

    fun getFullName(): String {
        return "$last $first $middle"
    }
}