package com.harvard.art.museums.features.exhibitions.list

import android.content.Intent
import com.harvard.art.museums.features.exhibitions.data.ExhibitionViewItem

sealed class ExhibitionsActionState {
    object LoadingState : ExhibitionsActionState()
    data class ErrorState(val error: Throwable) : ExhibitionsActionState()
    data class DataState(val exhibitionsList: List<ExhibitionViewItem>) : ExhibitionsActionState()
    data class ShareState(val intent: Intent) : ExhibitionsActionState()
    data class OpenLinkState(val intent: Intent) : ExhibitionsActionState()
}