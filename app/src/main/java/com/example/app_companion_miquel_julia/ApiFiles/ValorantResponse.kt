package com.example.app_companion_miquel_julia.ApiFiles

data class ValorantResponse(
    val characters: List<ValorantCharacter>,
    val acts: List<ValorantAct>
)

data class ValorantCharacter(
    val name: String,
    val id: String,
    val assetName:String
)

data class ValorantAct(
    val id: String,
    val name:String,
    val type:String,
    val isActive:Boolean
)
