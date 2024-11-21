package com.app.batiklens.ui.user.scanBatik

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.batiklens.R
import com.app.batiklens.databinding.ActivityScannerBinding

class ScannerActivity : AppCompatActivity() {

    private lateinit var bind: ActivityScannerBinding

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

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()

                    cameraProvider.bindToLifecycle(
                        this@ScannerActivity,
                        cameraSelector,
                        preview
                    )
                } catch (e: Exception){
                    Log.e("CameraX", "Use case binding failed", e)
                }

            }, ContextCompat.getMainExecutor(this@ScannerActivity))
        }
    }

    private fun requestCameraPermission() {
        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    startCamera() // Start camera if permission is granted
                } else {
                    Log.e("Permission", "Camera permission denied")
                }
            }

        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }
}