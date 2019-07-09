package com.harvard.art.museums.features.objects.list

import android.content.Intent

sealed class ObjectsActionState {
    object LoadingState : ObjectsActionState()
    data class ErrorState(val error: Throwable) : ObjectsActionState()
    data class DataState(val  viewItems: List<ObjectViewItem>) : ObjectsActionState()
    data class ShareState(val intent: Intent) : ObjectsActionState()
    data class OpenLinkState(val intent: Intent) : ObjectsActionState()
}