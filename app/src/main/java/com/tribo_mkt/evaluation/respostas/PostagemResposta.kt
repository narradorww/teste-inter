package com.tribo_mkt.evaluation.respostas

import com.google.gson.annotations.SerializedName

class PostagemResposta(
    @SerializedName("userId") val usuarioId: String,
    @SerializedName("id") val id: String,
    @SerializedName("title") val titulo: String,
    @SerializedName("body") val conteudo: String,
    var comentarios: Int? = null
)