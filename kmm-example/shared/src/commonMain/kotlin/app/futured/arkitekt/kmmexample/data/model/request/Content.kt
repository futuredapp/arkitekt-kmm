package app.futured.arkitekt.kmmexample.data.model.request

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

sealed class Content {
    abstract fun serialize(json: Json): String

    internal object EmptyContent : Content() {
        override fun serialize(json: Json): String = ""
    }
    internal data class QueryContent(val params: Map<String,String>): Content() {
        override fun serialize(json: Json): String = ""
    }
    internal data class JsonContent<BODY : Any>(
        val body: BODY,
        val bodySerializer: KSerializer<BODY>
    ) : Content() {

        override fun serialize(json: Json): String = json.encodeToString(bodySerializer, body)
    }

    internal data class FormUrlEncodedContent(
        val formData: Map<String, String>
    ) : Content() {

        override fun serialize(json: Json): String {
            var serializedData = ""
            formData.forEach {
                if (serializedData.isNotBlank()) {
                    serializedData += "&"
                }
                serializedData += "${it.key}=${it.value}"
            }
            return serializedData
        }
    }
}
