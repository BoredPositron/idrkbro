package com.example.whatever

class Student {
    var Name: String? = null
    var Pickup: Int? = null
    var Route: Int? = null
    var Dtime: String? = null
    var Ptime: String? = null


    constructor()

        constructor(Name: String, PickupNo: Int,  RouteNo: Int, Dtime: String, Ptime: String){
        this.Name = Name
        this.Pickup = PickupNo
        this.Route = RouteNo
        this.Dtime = Dtime
        this.Ptime = Ptime
    }
}