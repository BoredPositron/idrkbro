package com.example.whatever

class StudentInOut {
    var in_Out_Time: String? = null
    var name: String? = null
    var inBus: Boolean? = null

    constructor(){}

    constructor(name: String, outTime: String?, inBus: Boolean){
        this.name = name
        this.in_Out_Time = outTime
        this.inBus = inBus
    }
}