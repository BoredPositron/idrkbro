package com.example.whatever

class Student {
    var Name: String? = null
    var PickupNo: String? = null
    var DropNo: String? = null
    var RouteNo: String? = null
    var Grade: String? = null

    constructor(){}

    constructor(Name: String, PickupNo: String, DropNo: String, RouteNo: String, Grade: String){
        this.Name = Name
        this.PickupNo = PickupNo
        this.DropNo = DropNo
        this.RouteNo = RouteNo
        this.Grade = Grade
    }
}