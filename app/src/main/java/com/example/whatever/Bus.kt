package com.example.whatever

class Bus {
    var routeNumber: String? = null
    var Carer: String? = null
    var previousStop: String? = null
    var nextStop: String? = null

    constructor()

    constructor(routeNumber: String?, Carer: String?, previousStop: String?, nextStop: String?){
        this.routeNumber = routeNumber
        this.Carer = Carer
        this.previousStop = previousStop
        this.nextStop = nextStop
    }
}