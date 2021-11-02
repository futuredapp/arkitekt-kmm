## Define UseCase

UseCase is a abstract class with one abstract suspend function. It has two generic parameters. The first is to define 
Argument type, the second is to define Return type. If you don't need any of these, just put in z `Unit`
The suspend function will be executed on non-ui thread. Don't forget to `freeze()` the UseCase in `init` block. 
Any properties has to be initialized/injected above the init block because of freezing.

```kotlin
// shared/src/commonMain/../domain/
class GetCoinsListUseCase : UseCase<Unit, List<Coin>>() {
    private val coinStore: CoinStore = CoinStore(RestApiManager, DatabaseManager)

    init {
        freeze()
    }

    override suspend fun build(arg: Unit): List<Coin> {
        val list = coinStore.fetchCoins().coins.map {
            Coin(it.id, it.name, it.icon, it.symbol, it.price)
        }
        return list
    }
}
```

## Execute UseCase

To be able to execute the UseCase you have to be in the scope of `CoroutineScopeOwner`. On the Android side
you can take advantage of `viewModelScope` from Android Architecture Components (AAC). On the iOS side
you can use `ArkitektViewModel`from `km-viewmodel`. If you need to execute the UseCase outside of ViewModel, 
e.g. in a Service, you need to implement `CoroutineScopeOwner` and provide `CoroutineScope` by yourself.

### Usage on Android

Define a BaseViewModel

```kotlin
// androidApp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.futured.arkitekt.kmusecases.scope.CoroutineScopeOwner

abstract class BaseViewModel : ViewModel() , CoroutineScopeOwner {

    override val coroutineScope = viewModelScope
}
```

Execute the UseCase

```kotlin
class CoinsViewModel : BaseViewModel() {
    private val getCoinsUseCase = GetCoinsListUseCase()
    
    var coins by mutableStateOf(emptyList<Coin>())
    var isLoading by mutableStateOf(false)

    fun fetchCoins() {
        getCoinsUseCase.execute(Unit) {
            onStart { isLoading = true }
            onSuccess { 
                coins = it
                isLoading = false
            }
            onError {
                isLoading = false    
                println(it.message)
            }
        }
    }
}
```
### Usage on iOS

```swift
import shared

class BaseViewModel : Km_viewmodelArkitektViewModel {
    
    deinit {
        onDestroy()
    }
}
```

Execute the UseCase

```swift
import shared

class CoinsViewModel : BaseViewModel, ObservableObject {
    @Published var coins = [Coin]()

    private let getCoinsUseCase = GetCoinsListUseCase()

    func getCoins() {
        getCoinsUseCase.execute(self, args: KotlinUnit()) {
            $0.onSuccess { list in
                self.coins = list as [Coin]
            }
            $0.onError { error in
                print(error)
            }
        }
    }
}
```
