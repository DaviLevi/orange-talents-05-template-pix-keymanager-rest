package br.com.zup.ot5.pix.consulta_pix

import br.com.zup.ot5.ConsultaChavePixResponse


class ConsultaPixResponse(
    grpcResponse: ConsultaChavePixResponse
){

    val pixId = grpcResponse.pixId

    val idTitular = grpcResponse.idTitular

    val tipoChave = grpcResponse.tipoChave.name

    val chave = grpcResponse.valorChave

    val titular = mapOf(
        Pair("nome", grpcResponse.nomeTitular),
        Pair("cpf", grpcResponse.cpfTitular),
    )

    val conta = mapOf(
        Pair("instituicao", grpcResponse.instituicaoConta),
        Pair("ispb", grpcResponse.ispb),
        Pair("agencia", grpcResponse.agencia),
        Pair("numero", grpcResponse.numero),
        Pair("tipoConta", grpcResponse.tipoConta.name),
    )

    val criadaEm = grpcResponse.criadaEm
}
