package com.korlab.foodex.delivery.Data

class Address(street: String, house: String, flat: String) {

    var street: String = ""
    var house: String = ""
    var flat: String? = null

    init {
        this.street = street
        this.house = house
        this.flat = flat
    }
}