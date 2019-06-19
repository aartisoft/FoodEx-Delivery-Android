package com.korlab.foodex.delivery.Data

import java.util.*

class Task(name: Name, startAddress: Address, endAddress: Address, startTime: Date, endTime: Date, phone: String, type: Type, countBags: Int) {

    var name: Name? = null
    var startAddress: Address? = null
    var endAddress: Address? = null
    var startTime: Date? = null
    var endTime: Date? = null
    var phone: String = ""
    var type: Type = Type.BAG
    var startCountBags: Int = 0
    var countBags: Int = 0
    var isDone = false

    init {
        this.name = name
        this.startAddress = startAddress
        this.endAddress = endAddress
        this.startTime = startTime
        this.endTime = endTime
        this.phone = phone
        this.countBags = countBags
        this.startCountBags = countBags
    }

    enum class Type(val type: String) {
        BAG("Bag"),
        PACKAGE("Package"),
    }
}