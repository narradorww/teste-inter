package com.tribo_mkt.evaluation.respostas

import com.google.gson.annotations.SerializedName

class FotoResposta(
    @SerializedName("albumId") val albumId: String,
    @SerializedName("id") val id: String,
    @SerializedName("title") val titulo: String,
    @SerializedName("url") val url: String,
    @SerializedName("thumbnailUrl") val thumbnailUrl: String
)