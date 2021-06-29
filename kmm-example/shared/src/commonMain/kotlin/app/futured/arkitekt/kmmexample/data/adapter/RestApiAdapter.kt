package app.futured.arkitekt.kmmexample.data.adapter

import app.futured.arkitekt.kmmexample.data.model.request.Content
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readText
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters
import io.ktor.http.Url
import io.ktor.network.sockets.SocketTimeoutException
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

class RestApiAdapter {
    private val httpClient = HttpClient()

    private val baseUrl = "https://api.coinstats.app/"

    private val json = Json(Json) {
        isLenient = true
        ignoreUnknownKeys = true
    }

    suspend fun <T : Any> execute(ep: Endpoint<T>) =
        executeRequest(
            url = Url(baseUrl + ep.path),
            method = ep.method,
            bodyContent = ep.content,
            responseSerializer = ep.responseSerializer,
        )

    private suspend fun <T : Any> executeRequest(
        url: Url,
        headers: Map<String, String> = mapOf(),
        method: HttpMethod,
        bodyContent: Content,
        responseSerializer: KSerializer<T>? = null
    ): T {
        val response: HttpResponse = try {
            httpClient.request<HttpResponse> {
                url(url)
                header("Accept", "application/json")
                headers.forEach {
                    header(it.key, it.value)
                }
                this.method = method
                if (method != HttpMethod.Get) {
                    this.body = bodyContent.parseBody()
                } else {
                    if (bodyContent is Content.QueryContent) {
                        bodyContent.params.forEach {
                            parameter(it.key, it.value)
                        }
                    }
                }
            }
        } catch (error: IOException) {
            when (error) {
                is SocketTimeoutException -> error("Timeout exception")
//                is UnknownHostException -> ApiException(
//                    HttpURLConnection.HTTP_UNAVAILABLE,
//                    resources.getString(R.string.error_connection_error)
//                )
                else -> throw error
            }
        }

        return if (response.isSuccessful()) {
            val body = response.readText()
            if (responseSerializer == null) {
                @Suppress("UNCHECKED_CAST")
                Unit as T
            } else {
                json.decodeFromString(responseSerializer, body)
            }
        } else {
            error("Api error $url ${response.status.value} ")
        }
    }

    private fun Content.parseBody(): Any = when (this) {
        is Content.FormUrlEncodedContent ->
            FormDataContent(
                Parameters.build {
                    this@parseBody.formData.forEach {
                        append(it.key, it.value)
                    }
                }
            )
        else -> this.serialize(json)
    }

    private fun HttpResponse.isSuccessful(): Boolean = status.value in 200..299
}
