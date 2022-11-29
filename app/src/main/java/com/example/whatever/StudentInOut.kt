package com.example.whatever

class StudentInOut {
    var in_Out_Time: String? = null
    var name: String? = null
    var inBus: Boolean? = null
    var morningAttendance: Boolean? = null
    var eveningAttendance: Boolean? = null

    constructor()

    constructor(
        name: String,
        outTime: String?,
        inBus: Boolean,
        morningAttendance: Boolean? = null,
        eveningAttendance: Boolean? = null
    ) {
        this.name = name
        this.in_Out_Time = outTime
        this.inBus = inBus
        this.morningAttendance = morningAttendance
        this.eveningAttendance = eveningAttendance
    }
}