package app.futured.arkitekt.kmmexample.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class Coin(
    val id: String,
    val icon: String,
    val name: String,
    val symbol: String,
    val price: Float,
)
