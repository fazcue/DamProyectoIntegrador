package com.example.damproyectointegrador.entities

class EMember(
    firstname: String,
    lastname: String,
    dni: String,
    dueFeeDate: String,
    val nMember: Int
) : EClient(firstname, lastname, dni, dueFeeDate)
