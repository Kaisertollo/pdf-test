package net.atos.pdfrenderer.pdfviewer.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import net.atos.pdfrenderer.R
import net.atos.pdfrenderer.pdfviewer.utils.PdfPageQuality
import java.io.File

class DefaultPdfPageAdapter(
    file: File,
    quality: PdfPageQuality,
    dispatcher: CoroutineDispatcher,
    private val scope: CoroutineScope,
) : PdfPagesAdapter<DefaultPdfPageViewHolder>(file, quality, dispatcher) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefaultPdfPageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pdf_page, parent, false)

        return DefaultPdfPageViewHolder(view, scope, ::renderPage)
    }

    override fun onBindViewHolder(holder: DefaultPdfPageViewHolder, position: Int) {
        holder.bind(position)
    }
}