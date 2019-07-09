package com.harvard.art.museums.features.objects.details


sealed class ObjectDetailsActionState {
    object LoadingState : ObjectDetailsActionState()
    data class ErrorState(val error: Throwable) : ObjectDetailsActionState()
    data class DataState(val data: Any) : ObjectDetailsActionState()
}