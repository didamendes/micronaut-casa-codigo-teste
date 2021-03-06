package br.com.zup.autores

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client

@Client("https://viacep.com.br/ws/")
interface EnderecoClient {

    @Get(uri = "{cep}/json/")
    fun consulta(cep: String): HttpResponse<EnderecoResponse>

}