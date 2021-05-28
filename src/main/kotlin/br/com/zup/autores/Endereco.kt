package br.com.zup.autores

import javax.persistence.Embeddable

@Embeddable
class Endereco(
    enderecoResponse: EnderecoResponse
) {
    val cep = enderecoResponse.cep
    val logradouro = enderecoResponse.logradouro
    val bairro = enderecoResponse.bairro
    val localidade = enderecoResponse.localidade
}
