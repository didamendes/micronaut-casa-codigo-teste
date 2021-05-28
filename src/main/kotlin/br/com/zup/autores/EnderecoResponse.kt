package br.com.zup.autores

data class EnderecoResponse(
    val cep: String,
    val logradouro: String,
    val bairro: String,
    val localidade: String
)
