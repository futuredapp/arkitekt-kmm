package app.futured.arkitekt.kmmexample

import app.futured.arkitekt.kmmexample.data.adapter.Endpoint
import app.futured.arkitekt.kmmexample.data.adapter.RestApiAdapter
import app.futured.arkitekt.kmmexample.data.model.response.Coin
import app.futured.arkitekt.kmusecases.freeze
import app.futured.arkitekt.kmusecases.usecase.UseCase

class TestUseCase : UseCase<String, ListWrapper<Coin>>() {

    init {
        freeze()
    }

    override suspend fun build(arg: String): ListWrapper<Coin> {
        val apiAdapter = RestApiAdapter()
        val coinsList = apiAdapter.execute(Endpoint.GetCoinsList(arg, "5"))
        return ListWrapper(coinsList.coins)
    }
}

data class ListWrapper<T : Any>(val list: List<T>)
