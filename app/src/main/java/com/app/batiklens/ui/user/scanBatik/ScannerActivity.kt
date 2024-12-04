package com.app.batiklens.ui.user.scanBatik

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.batiklens.R
import com.app.batiklens.databinding.ActivityScannerBinding
import com.app.batiklens.databinding.ScannerInfoViewBinding
import com.app.batiklens.di.Injection.getPath
import com.app.batiklens.di.Injection.messageToast
import com.app.batiklens.helper.ImageClassifierHelper
import com.app.batiklens.ui.user.result.ResultActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File

class ScannerActivity : AppCompatActivity() {

    private lateinit var bind: ActivityScannerBinding
    private lateinit var imageCapture: ImageCapture
    private lateinit var imageClassier: ImageClassifierHelper
    private var imageUri: Uri? = null
    private var imageFile: File? = null
    private var dialog: Dialog? = null
    private val scannerViewModel: ScannerViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bind = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(bind.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bind.apply {

            if (ContextCompat.checkSelfPermission(this@ScannerActivity, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
                // Start Camera
                startCamera()
            } else {
                requestCameraPermission()
            }

            scannerViewModel.modelPredict.observe(this@ScannerActivity) { result ->
                result.onSuccess {
                    val i = Intent(this@ScannerActivity, ResultActivity::class.java).apply {
                        putExtra(ResultActivity.MOTIF_NAME, it)
                    }
                    startActivity(i)
                }.onFailure {
                    messageToast(this@ScannerActivity, it.toString())
                }
            }

            tipsButton.setOnClickListener {
                dialog = Dialog(this@ScannerActivity)
                val dialogBind = ScannerInfoViewBinding.inflate(layoutInflater)
                dialog?.let { data ->
                    data.setContentView(dialogBind.root)
                    data.setCancelable(false)
                    dialogFull(data)

                    dialogBind.masuk.setOnClickListener {
                        data.dismiss()
                    }

                    data.show()
                }
            }

            galleryButton.setOnClickListener {
                resultLauncherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            imageClassier = ImageClassifierHelper(
                context = this@ScannerActivity,
                classifierListener = object : ImageClassifierHelper.ClassifierListener {
                    override fun onErr(err: String) {
                        Log.e("error", err)
                    }

                    override fun onResult(result: List<Classifications>?,  inferenceTime: Double) {
                        result?.let { classifications ->
                            Log.e("data classification : ", classifications.toString())
                            Log.e("waktu : ", inferenceTime.toString())
                            // Iterate through the classifications
                            classifications.forEach { classification ->
                                classification.categories.forEach { category ->
                                    val motifName = category.label
                                    val motif = motifName.substringAfterLast("_")
                                    val score = category.score

                                    val i = Intent(this@ScannerActivity, ResultActivity::class.java).apply {
                                        putExtra(ResultActivity.MOTIF_NAME, motifName)
                                        putExtra(ResultActivity.MOTIF, motif)
                                        putExtra(ResultActivity.SCORE, score)
                                        putExtra(ResultActivity.WAKTU, inferenceTime)
                                    }
                                    startActivity(i)
                                }
                            }
                        }
                    }
                }
            )

            captureButton.setOnClickListener {
                val photoFile = File(externalMediaDirs.firstOrNull(), "${System.currentTimeMillis()}.jpg")
                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                imageCapture.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(this@ScannerActivity),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            imageUri = Uri.fromFile(photoFile)
                            // Classify the image using the URI
//                            imageUri?.let {
//                                imageClassier.classifyStaticImage(it)
//                            }
//                            scannerViewModel.predictModel(photoFile)
                        }

                        override fun onError(exception: ImageCaptureException) {
                            messageToast(this@ScannerActivity,"Image capture failed $exception" )
                        }
                    }
                )
            }

        }
    }

    private fun dialogFull(d: Dialog) {
        d.window?.let {
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    private fun showLoading(isLoading: Boolean) {
        bind.loading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun startCamera() {
        bind.apply {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(this@ScannerActivity)

            cameraProviderFuture.addListener({
                val cameraProvider : ProcessCameraProvider = cameraProviderFuture.get()

                val preview = androidx.camera.core.Preview.Builder()
                    .build()
                    .also {
                        it.surfaceProvider = previewView.surfaceProvider
                    }

                imageCapture = ImageCapture.Builder()
                    .setTargetRotation(bind.previewView.display.rotation)
                    .build()

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()

                    cameraProvider.bindToLifecycle(
                        this@ScannerActivity,
                        cameraSelector,
                        preview,
                        imageCapture
                    )
                } catch (e: Exception){
                    messageToast(this@ScannerActivity,"Error : $e" )
                }

            }, ContextCompat.getMainExecutor(this@ScannerActivity))
        }
    }

    private val resultLauncherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            imageFile = getPath(this@ScannerActivity, it)?.let { it1 -> File(it1) }
            if (imageFile != null){
                scannerViewModel.predictModel(imageFile!!)
            } else {
                messageToast(this@ScannerActivity, "Failed to get the image file.")
            }

        }
    }

    private fun requestCameraPermission() {
        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    startCamera()
                } else {
                    messageToast(this@ScannerActivity,"Camera Permission Denied" )
                }
            }

        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }
}