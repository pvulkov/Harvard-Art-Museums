package com.harvard.art.museums.features.exhibitions.details

import android.content.Intent
import com.harvard.art.museums.features.exhibitions.data.ExhibitionViewItem

sealed class ExhibitionDetailsActionState {
    object LoadingState : ExhibitionDetailsActionState()
    data class ErrorState(val error: Throwable) : ExhibitionDetailsActionState()
    data class DataState(val exhibitionsList: List<ExhibitionViewItem>) : ExhibitionDetailsActionState()
    data class ShareState(val intent: Intent) : ExhibitionDetailsActionState()
    data class OpenLinkState(val intent: Intent) : ExhibitionDetailsActionState()
}