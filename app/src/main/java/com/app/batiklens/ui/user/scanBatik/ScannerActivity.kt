package com.app.batiklens.ui.user.scanBatik

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.batiklens.BuildConfig
import com.app.batiklens.R
import com.app.batiklens.databinding.ActivityScannerBinding
import com.app.batiklens.databinding.ScannerInfoViewBinding
import com.app.batiklens.di.Injection.messageToast
import com.app.batiklens.di.database.History
import com.app.batiklens.ui.user.MainActivity
import com.app.batiklens.ui.user.detailMotif.DetailMotifActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class ScannerActivity : AppCompatActivity() {

    private lateinit var bind: ActivityScannerBinding
    private lateinit var imageCapture: ImageCapture
    private var imageUri: Uri? = null
    private var dialog: Dialog? = null
    private var isFlashOn = false
    private val scannerViewModel: ScannerViewModel by viewModel()
    private val maxSize = 5 * 1024 * 1024

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

            backBtn.setOnClickListener {
                startActivity(Intent(this@ScannerActivity, MainActivity::class.java))
                finish()
            }

            if (ContextCompat.checkSelfPermission(this@ScannerActivity, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
                // Start Camera
                startCamera()
            } else {
                requestCameraPermission()
            }

            scannerViewModel.loading.observe(this@ScannerActivity) { isLoading ->
                loading.visibility = if (isLoading) View.VISIBLE else View.GONE
            }

            scannerViewModel.modelPredict.observe(this@ScannerActivity) { result ->
                result?.let {

                    val history = History(
                        namaBatik = it.predictedLabel,
                        imageUri = imageUri.toString(),
                        confidence = it.confidence,
                        idProvinsi = it.idProvinsi,
                        idMotif = it.idMotif
                    )

                    val maxConfidence = 0.6
                    if (result.confidence < maxConfidence) {
                        messageToast(this@ScannerActivity, "Ini Bukan Batik!")
                    } else {
                        scannerViewModel.insertHistory(history)
                        val i = Intent(this@ScannerActivity, DetailMotifActivity::class.java).apply {
                            putExtra(DetailMotifActivity.DETAIL_ID, it.idProvinsi)
                            putExtra(DetailMotifActivity.DETAIL_MOTIF_ID, it.idMotif)
                        }
                        startActivity(i)
                    }
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
                            if (photoFile.length() > maxSize) {
                                messageToast(this@ScannerActivity, "File Gambar Melebihi 5 Mb")
                            } else {
                                scannerViewModel.predictModel(photoFile)
                                imageUri = FileProvider.getUriForFile(
                                    this@ScannerActivity,
                                    "${BuildConfig.APPLICATION_ID}.fileprovider",
                                    photoFile
                                )
                            }
                        }

                        override fun onError(exception: ImageCaptureException) {
                            messageToast(this@ScannerActivity,"Gambar Gagal diambil : $exception" )
                        }
                    }
                )
            }
        }
    }

    private fun toggleFlash(cameraControl: CameraControl) {
        isFlashOn = !isFlashOn
        bind.flashImage.setImageResource(
            if (isFlashOn) R.drawable.baseline_flash_off_24 else R.drawable.outline_flash_on_24
        )

        cameraControl.enableTorch(isFlashOn)
    }

    private fun dialogFull(d: Dialog) {
        d.window?.let { window ->
            val layoutParams = window.attributes
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            window.attributes = layoutParams
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val marginHorizontal = resources.getDimensionPixelSize(R.dimen.dialog_margin)
            d.findViewById<View>(android.R.id.content)?.setPadding(marginHorizontal, 0, marginHorizontal, 0)
        }
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

                    val camera = cameraProvider.bindToLifecycle(
                        this@ScannerActivity,
                        cameraSelector,
                        preview,
                        imageCapture
                    )

                    val cameraControl = camera.cameraControl

                    flash.setOnClickListener {
                        toggleFlash(cameraControl)
                    }

                } catch (e: Exception){
                    messageToast(this@ScannerActivity,"Error : $e" )
                }

            }, ContextCompat.getMainExecutor(this@ScannerActivity))
        }
    }

    private val resultLauncherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { selectedUri ->
            val photoFile = File(cacheDir, "${System.currentTimeMillis()}.jpg")

            try {
                contentResolver.openInputStream(selectedUri)?.use { inputStream ->
                    photoFile.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }

                if (photoFile.length() > maxSize) {
                    messageToast(this@ScannerActivity, "File Gambar Melebihi 5 Mb")
                } else {
                    scannerViewModel.predictModel(photoFile)
                    imageUri = FileProvider.getUriForFile(
                        this@ScannerActivity,
                        "${BuildConfig.APPLICATION_ID}.fileprovider",
                        photoFile
                    )
                }
            } catch (e: Exception) {
                messageToast(this@ScannerActivity, "Gagal menyimpan file: ${e.message}")
            }
        }
    }

    private fun requestCameraPermission() {
        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    startCamera()
                } else {
                    messageToast(this@ScannerActivity,"Izin Kamera Ditolak" )
                }
            }

        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }
}