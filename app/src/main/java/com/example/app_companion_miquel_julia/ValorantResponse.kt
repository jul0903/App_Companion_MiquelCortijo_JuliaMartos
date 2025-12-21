package com.example.app_companion_miquel_julia

data class ValorantResponse(
    val characters: List<ValorantCharacter>
)

data class ValorantCharacter(
    val name: String,
    val id: String
)
