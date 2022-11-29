package com.example.whatever

class Student {
    var Name: String? = null
    var PickupNo: String? = null
    var RouteNo: String? = null
    var Grade: String? = null

    constructor()

    constructor(Name: String, PickupNo: String,  RouteNo: String, Grade: String, ot: Boolean? = null){
        this.Name = Name
        this.PickupNo = PickupNo
        this.RouteNo = RouteNo
        this.Grade = Grade
    }
}