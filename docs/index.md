# Overview

## Arkitekt KMM

Arkitekt KMM is a Kotlin Multiplatform Mobile library that helps you with
the abstraction of a use cases, known as one of the building blocks of the Clean Architecture.
Its main objective is a separation of concerns and better domain modeling.
It is backed by [`Kotlin Coroutines`](https://kotlinlang.org/docs/coroutines-overview.html).

## Benefits
- Delegate work to a background thread
- Cancel on re-execution (optional)
- Error handling
- Auto-cancellation when is the related coroutine scope terminated

## Content

The module `km-usecases` provides two main components - `UseCase` and `FlowUseCase`:

- `UseCase` is for events that return a single response (e.g. REST API call GET, POST...).
- `FlowUseCase` is for events that return multiple responses (e.g. Location data updates...).

Another module, `km-viewmodel` consists `ArktitektViewModel` which will help with maintaining 
`CoroutineScope` on iOS platform:

- `ArktitektViewModel` creates `CoroutineScope` and manages it

## Installation

Head out to [Installation](docs/Overview/installation.md) section.

## Example

Check out the usage in the [example](https://github.com/RudolfHladik/Template).


