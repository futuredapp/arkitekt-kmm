package app.futured.arkitekt.kmmexample.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class CoinsListResponse(val coins: List<Coin>)
