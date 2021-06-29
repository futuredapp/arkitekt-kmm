package app.futured.arkitekt.kmmexample.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class GetCoinsListParams(val currency: String, val limit: Int)
