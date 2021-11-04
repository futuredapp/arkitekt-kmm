# Overview

## Arkitekt kmm

Is a Kotlin multiplatform mobile library that helps you with
abstraction of Usecase as a component from clean architecture.
Its main objective is separation of concerns and better domain modeling.
It is backed by `Kotlinx Coroutines`

## Benefits
- delegate work to background thread
- cancel on re-execution (optional)
- error handling
- auto-cancel of coroutine context

## Content

Library contains two main parts `UseCase` and `FlowUseCase`

- `UseCase` is for events that return single response. (e.g. REST API call GET, POST...)
- `FlowUseCase` is for events that return multiple responses. (e.g. Location data updates...)

Another module consist `ArktitektViewModel` which will help with maintaining `CoroutineScope` on iOS platform 

- `ArktitektViewModel` creates `CoroutineScope` and manages it

## Installation

Head out to [Installation](docs/Overview/installation.md) section.

## Example

Check out the usage in the [example](https://github.com/RudolfHladik/Template)


