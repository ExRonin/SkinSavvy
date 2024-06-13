package com.dicoding.skinSavvy.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.dicoding.skinSavvy.ml.CancerClassification
import org.tensorflow.lite.support.image.TensorImage
import java.io.IOException

@Suppress("DEPRECATION")
class ImageClassifierHelper(private val context: Context) {

    private var imageClassifier: CancerClassification? = null

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        imageClassifier = CancerClassification.newInstance(context)
    }

    fun classifyStaticImage(imageUri: Uri): Pair<String, Float>? {
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                val source = ImageDecoder.createSource(context.contentResolver, imageUri)
                ImageDecoder.decodeBitmap(source)
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        } else {
            try {
                MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }?.copy(Bitmap.Config.ARGB_8888, true)

        bitmap?.let { it ->
            val tensorImage = TensorImage.fromBitmap(it)
            val outputs = imageClassifier?.process(tensorImage)
            val probability = outputs?.probabilityAsCategoryList
            val result = probability?.maxByOrNull { it.score }
            result?.let {
                return Pair(it.label, it.score)
            }
        }
        return null
    }


    fun closeClassifier() {
        imageClassifier?.close()
    }
}
