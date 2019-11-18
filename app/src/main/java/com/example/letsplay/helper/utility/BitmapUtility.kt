package com.example.letsplay.helper.utility

import android.graphics.Bitmap
import android.os.Environment
import java.nio.file.Files.exists
import android.os.Environment.getExternalStorageDirectory
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


object BitmapUtility {

    fun saveBitmapToFile(bmp: Bitmap, filePath: String) {
        val fos = FileOutputStream(filePath)
        bmp.compress(Bitmap.CompressFormat.JPEG, 80, fos)
        fos.flush()
        fos.close()
        bmp.recycle()
    }
}