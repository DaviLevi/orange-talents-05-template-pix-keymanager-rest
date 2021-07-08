package br.com.zup.ot5.core.exception_handler

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.JsonInclude

data class Erro(
    val status: Int,
    val titulo: String,
    val descricao: String,
    @JsonInclude(value = Include.NON_NULL) val propriedadesInvalidas: List<PropriedadeInvalida>? = null
){
    data class PropriedadeInvalida(
        val propriedade: String,
        val descricao: String
    )
}