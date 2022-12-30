package com.example.whatever

class Carer {
    var name: String? = null
    var routeNumber: String? = null
    var code: String? = null
    var access: Boolean? = null

    constructor()

    constructor(name: String, routenumber: String, code: String, access: Boolean){
        this.name = name
        this.routeNumber =routenumber
        this.code = code
        this.access = access
    }
}