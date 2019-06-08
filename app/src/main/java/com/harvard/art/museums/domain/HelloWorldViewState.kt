package com.harvard.art.museums.domain

sealed class HelloWorldViewState {
    object LoadingState : HelloWorldViewState()
    data class DataState(val greeting: String) : HelloWorldViewState()
    data class ErrorState(val error: Throwable) : HelloWorldViewState()
}

