package com.tribo_mkt.evaluation.respostas

import com.google.gson.annotations.SerializedName

class UsuarioResposta(
    @SerializedName("id") val id: String,
    @SerializedName("name") val nome: String,
    @SerializedName("username") val usuarioNome: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val telefone: String
)