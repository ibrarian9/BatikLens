package com.app.batiklens.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.SystemClock
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

class ImageClassifierHelper(
    private val threshold: Float = 0.1f,
    private val maxResult: Int = 1,
    private val modulName: String = "BatikLens_Xception_Model_Metadata.tflite",
    private val context: Context,
    private val classifierListener: ClassifierListener?
) {
    private var imageClassifier: ImageClassifier? = null
    private var tensorImage: TensorImage? = null

    interface ClassifierListener {
        fun onErr(err: String)
        fun onResult(
            result: List<Classifications>?,
            inferenceTime: Double
        )
    }

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        val optBuilder = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(threshold)
            .setMaxResults(maxResult)
        val baseOptBuilder = BaseOptions.builder()
            .setNumThreads(4)
        optBuilder.setBaseOptions(baseOptBuilder.build())

        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context,
                modulName,
                optBuilder.build()
            )
        } catch (e: IllegalStateException) {
            classifierListener?.onErr(e.toString())
        }
    }

    fun classifyStaticImage(imageUri: Uri) {
        if (imageClassifier == null){
            setupImageClassifier()
        }

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .add(CastOp(DataType.UINT8))
            .build()
        val source = ImageDecoder.createSource(context.contentResolver, imageUri)
        ImageDecoder.decodeBitmap(source)
            .copy(Bitmap.Config.ARGB_8888, true)?.let { bitmap ->
            tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))
        }

        val imageProcessOpt = ImageProcessingOptions.builder()
            .build()

        var inferenceTime = SystemClock.uptimeMillis()
        val results = imageClassifier?.classify(tensorImage, imageProcessOpt)
        inferenceTime = SystemClock.uptimeMillis() - inferenceTime
        val inferenceTimeInSecond = inferenceTime / 1000.0
        classifierListener?.onResult(
            result = results,
            inferenceTime = inferenceTimeInSecond
        )
    }
}