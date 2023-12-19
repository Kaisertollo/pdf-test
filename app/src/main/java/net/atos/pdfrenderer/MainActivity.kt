package net.atos.pdfrenderer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.atos.pdfrenderer.pdfviewer.interfaces.OnErrorListener
import net.atos.pdfrenderer.pdfviewer.interfaces.OnPageChangedListener
import net.atos.pdfrenderer.pdfviewer.pdfviewer.PdfViewer
import net.atos.pdfrenderer.pdfviewer.utils.PdfPageQuality
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity(), OnPageChangedListener, OnErrorListener {
    private val STORAGE_PERMISSION_REQUEST_CODE = 1
    private val test:MutableLiveData<Boolean> = MutableLiveData(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Check if the permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                STORAGE_PERMISSION_REQUEST_CODE
            )
        } else {
            GlobalScope.launch {
                downloadFile()
            }
            test.observe(this as LifecycleOwner){
                if(it){
                    val internalFileDir: File? = this.getExternalFilesDir("") // get internal file dir
                    createDirectory(internalFileDir, "/test")
                    val destinationPath = "$internalFileDir/test"
                    val file = File("$destinationPath/test.pdf")
                    PdfViewer.Builder(findViewById<ViewGroup>(R.id.rootView), lifecycleScope)
                        .setMaxZoom(3f)
                        .setZoomEnabled(true)
                        .quality(PdfPageQuality.QUALITY_1080)
                        .setOnErrorListener(this)
                        .setOnPageChangedListener(this)
                        .setRenderDispatcher(Dispatchers.Default)

                        .build()
                        .load(file)
                }
            }
        }

    }
    fun createDirectory(dir: File?, folder_main: String?) {
        val f = File(dir, folder_main)
        if (!f.exists()) {
            f.mkdirs()
        }
    }
    private fun downloadFile() {
        try {
            val internalFileDir: File? = this.getExternalFilesDir("") // get internal file dir
            createDirectory(internalFileDir, "/test")
            val destinationPath = "$internalFileDir/test"
            val file = File("$destinationPath/test.pdf")
            val url = URL("https://drive.google.com/u/0/uc?id=1XKmXATt2RtxylBXeRgQnMmYdHNJh-g2Q&export=download")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.doOutput = true
            connection.connect()
            val inputStream = connection.inputStream
            val fileOutputStream = FileOutputStream(file)
            val buffer = ByteArray(4096)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                fileOutputStream.write(buffer, 0, bytesRead)
            }
            fileOutputStream.close()
            inputStream.close()
            test.postValue(true)

        } catch (e: Exception) {
            e.printStackTrace()
            println("vlad ${e.message}")

        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, proceed with accessing the storage
                // Your code to access storage goes here
            } else {
                // Permission is denied, handle accordingly (e.g., show an error message or disable functionality)
            }
        }
    }

    override fun onFileLoadError(e: java.lang.Exception) {
        println("vlad1")
    }

    override fun onAttachViewError(e: java.lang.Exception) {
        println("vlad2")
    }

    override fun onPdfRendererError(e: IOException) {
        println("vlad3")
    }

    override fun onPageChanged(page: Int, total: Int) {
        println("vlad3")
    }
}