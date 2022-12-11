package com.example.whatever

class StudentInOut {
    var inTime: String? = null
    var outTime: String? = null
    var name: String? = null
    var inBus: Boolean? = null
    var morningAttendance: Boolean? = null
    var eveningAttendance: Boolean? = null

    constructor()

    constructor(
        name: String? = null,
        outTime: String? = null,
        inTime: String? = null,
        inBus: Boolean? = null,
        morningAttendance: Boolean? = null,
        eveningAttendance: Boolean? = null
    ) {
        this.name = name
        this.inTime = inTime
        this.outTime = outTime
        this.inBus = inBus
        this.morningAttendance = morningAttendance
        this.eveningAttendance = eveningAttendance
    }
}