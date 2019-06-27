package com.harvard.art.museums.features.objects

import android.content.Intent
import com.harvard.art.museums.features.exhibitions.data.ExhibitionViewItem

sealed class ObjectsActionState {
    object LoadingState : ObjectsActionState()
    data class ErrorState(val error: Throwable) : ObjectsActionState()
    data class DataState(val  viewItems: List<ObjectViewItem>) : ObjectsActionState()
    data class ShareState(val intent: Intent) : ObjectsActionState()
    data class OpenLinkState(val intent: Intent) : ObjectsActionState()
}