package com.example.extractapp

import android.content.ContentUris
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val PDF_code : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        select_file.setOnClickListener {
            val intent = Intent()
            intent.setType("application/pdf")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select PDF"), PDF_code)
        }








    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == RESULT_OK) {
            if (requestCode == PDF_code) {
                val file__path = data?.data
                val uriPathHelper = URIPathHelper()
                val filePath = uriPathHelper.getPath(this, file__path!!)
//                val ddd = getPDFPath(file__path!!)

//
                val result = getPythonHelloWorld(filePath)//"/sdcard/Download/kk.pdf"
                textView1.text = result

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
//    /sdcard/Download/About us - ICIT ACCOUNT.pdf
    fun getPDFPath(uri: Uri?): String? {
        val id = DocumentsContract.getDocumentId(uri)
        val contentUri = ContentUris.withAppendedId(
            Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
        )
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = contentResolver.query(contentUri, projection, null, null, null)
        val column_index: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    fun getPythonHelloWorld(path: String?): String {
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }
        val python = Python.getInstance()
        val pythonFile = python.getModule("hello")
        return pythonFile.callAttr("find_snnid_", path).toString()
    }

    private fun getRealPathFromURI(contentURI: Uri): String? {
        val result: String?
        val cursor = contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }

}
