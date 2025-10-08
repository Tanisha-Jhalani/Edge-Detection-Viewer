package com.example.app

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.camera2.*
import android.os.Build
import android.os.Bundle
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.TextureView
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.gl.MyGLSurfaceView
import com.example.jni.NativeBridge

class MainActivity : ComponentActivity() {
    private lateinit var glView: MyGLSurfaceView

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            // Camera initialization will be handled inside GL module or future extension.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        glView = MyGLSurfaceView(this)
        setContentView(glView)

        ensureCameraPermission()
    }

    private fun ensureCameraPermission() {
        val has = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        if (!has) {
            requestPermission.launch(Manifest.permission.CAMERA)
        }
    }
}


