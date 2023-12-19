package net.atos.pdfrenderer.pdfviewer.interfaces

import android.view.View

import kotlinx.coroutines.CoroutineDispatcher
import net.atos.pdfrenderer.pdfviewer.utils.PdfPageQuality
import java.io.File

interface PdfViewController {

    fun getView(): View

    fun setup(file: File)

    fun setZoomEnabled(isZoomEnabled: Boolean)

    fun setMaxZoom(maxZoom: Float)

    fun setQuality(quality: PdfPageQuality)

    fun setOnPageChangedListener(onPageChangedListener: OnPageChangedListener?)

    fun goToPosition(position: Int)

    fun setDispatcher(dispatcher: CoroutineDispatcher)
}