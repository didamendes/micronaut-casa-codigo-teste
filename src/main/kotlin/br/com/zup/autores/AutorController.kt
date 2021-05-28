package br.com.zup.autores

import io.micronaut.data.model.Pageable
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.http.uri.UriBuilder
import io.micronaut.validation.Validated
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Controller("/autores")
class AutorController(
    val autorRepository: AutorRepository,
    val enderecoClient: EnderecoClient
) {

    @Get
    fun lista(@QueryValue(defaultValue = "") email: String): HttpResponse<Any> {
        if (email.isBlank()) {
            val autores = autorRepository.findAll()

            val resposta = autores.map { autor -> DetalhesDoAutorResponse(autor) }

            return HttpResponse.ok(resposta)
        }

        val autorOptional = autorRepository.findByEmail(email)

        when {
            autorOptional.isPresent -> return HttpResponse.ok(DetalhesDoAutorResponse(autorOptional.get()))
            else -> return HttpResponse.notFound()
        }
    }

    @Post
    @Transactional
    fun cadastrar(@Body @Valid request: AutorRequest): HttpResponse<Any> {
        val enderecoResponse = enderecoClient.consulta(request.cep)

        val autor: Autor = request.paraAutor(enderecoResponse.body()!!)

        autorRepository.save(autor)
        val uri = UriBuilder.of("/autores/{id}").expand(mutableMapOf(Pair("id", autor.id)))

        return HttpResponse.created(DetalhesDoAutorResponse(autor), uri)
    }

    @Put(uri = "/{id}")
    @Transactional
    fun alterar(@PathVariable id: Long, descricao: String): HttpResponse<Any> {
        val autorOptional = autorRepository.findById(id)

        if (autorOptional.isEmpty) {
            return HttpResponse.notFound()
        }

        autorOptional.get().apply { this.descricao = descricao }

        return HttpResponse.ok(DetalhesDoAutorResponse(autorOptional.get()))
    }

    @Delete(uri = "/{id}")
    @Transactional
    fun deletar(@PathVariable id: Long): HttpResponse<Any> {
        val autorOptional = autorRepository.findById(id)

        if (autorOptional.isEmpty) {
            return HttpResponse.notFound()
        }

        autorRepository.delete(autorOptional.get())

        return HttpResponse.noContent()
    }

}