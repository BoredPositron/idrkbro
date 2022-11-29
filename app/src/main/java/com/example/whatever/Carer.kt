package com.example.whatever

class Carer {
    var name: String? = null
    var RouteNumber: String? = null
    var Code: String? = null
    var Access: Boolean? = null

    constructor()

    constructor(name: String, RouteNumber: String, Code: String, access: Boolean){
        this.name = name
        this.RouteNumber = RouteNumber
        this.Code = Code
        this.Access = access
    }
}