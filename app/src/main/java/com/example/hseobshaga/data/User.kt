package com.example.hseobshaga.data

data class User(
    val firstName : String,
    val secondName : String,
    val mail : String,
    val room : String,
    var description : String,
    val photoUri : String) {
    constructor(): this(
        "",
        "",
        "",
        "",
        "",
        ""
    )
}