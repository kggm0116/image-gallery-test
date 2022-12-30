package ru.kggm.presentation.utility

import android.content.res.Configuration
import android.view.View
import androidx.fragment.app.Fragment

class UIElementExtensions {

    companion object {
        val View.inLandscape get() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val View.inPortrait get() = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

        val Fragment.inLandscape get() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val Fragment.inPortrait get() = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }
}