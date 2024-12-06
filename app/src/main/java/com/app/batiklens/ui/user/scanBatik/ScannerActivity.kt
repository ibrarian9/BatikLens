package com.app.batiklens.ui.user.scanBatik

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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
import com.app.batiklens.ui.user.detailMotif.DetailMotifActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class ScannerActivity : AppCompatActivity() {

    private lateinit var bind: ActivityScannerBinding
    private lateinit var imageCapture: ImageCapture
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
                result?.let {
                    val i = Intent(this@ScannerActivity, DetailMotifActivity::class.java).apply {
                        putExtra(DetailMotifActivity.DETAIL_ID, it.idProvinsi)
                        putExtra(DetailMotifActivity.DETAIL_MOTIF_ID, it.idMotif)
                    }
                    startActivity(i)
                } ?: run {
                    messageToast(this@ScannerActivity, "Scanner Gagal!!")
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

            captureButton.setOnClickListener {
                val photoFile = File(externalMediaDirs.firstOrNull(), "${System.currentTimeMillis()}.jpg")
                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                imageCapture.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(this@ScannerActivity),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            scannerViewModel.predictModel(photoFile)
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