package com.example.whatever

class Student {
    var Name: String? = null
    var PickupLoc: String? = null
    var PickupNo: String? = null
    var DropLoc: String? = null
    var DropNo: String? = null
    var RouteNo: String? = null

    constructor(){}

    constructor(Name: String, PickupLoc: String, PickupNo: String, DropLoc: String, DropNo: String, RouteNo: String){
        this.Name = Name
        this.PickupLoc = PickupLoc
        this.PickupNo = PickupNo
        this.DropLoc = DropLoc
        this.DropNo = DropNo
        this.RouteNo = RouteNo
    }
}