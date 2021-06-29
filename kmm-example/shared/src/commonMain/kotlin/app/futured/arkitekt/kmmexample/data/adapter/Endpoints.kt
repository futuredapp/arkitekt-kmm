package app.futured.arkitekt.kmmexample.data.adapter

import app.futured.arkitekt.kmmexample.data.model.request.Content
import app.futured.arkitekt.kmmexample.data.model.request.GetCoinsListParams
import app.futured.arkitekt.kmmexample.data.model.response.CoinsListResponse
import io.ktor.http.HttpMethod
import kotlinx.serialization.KSerializer

sealed class Endpoint<T>(
    internal val path: String,
    internal val method: HttpMethod,
    internal val content: Content,
    internal val responseSerializer: KSerializer<T>? = null
) {
    class GetCoinsList(currency: String, limit: String) : Endpoint<CoinsListResponse>(
        path = "public/v1/coins",
        method = HttpMethod.Get,
        content = Content.QueryContent(
            mapOf(
                "currency" to currency,
                "limit" to limit
            )
        ),
        responseSerializer = CoinsListResponse.serializer()
    )
}
