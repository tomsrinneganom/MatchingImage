package com.dedstudio.testopencv5

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.camera.camera2.internal.annotation.CameraExecutor
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.camera.view.preview.transform.PreviewTransform
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.fragment.findNavController
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.android.synthetic.main.camera_x_fragment.*
import kotlinx.android.synthetic.main.camera_x_fragment.view.*
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

var f = true

class CameraXFragment : Fragment() {

    companion object {
        fun newInstance() = CameraXFragment()
    }

    private lateinit var viewModel: CameraXViewModel
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraPreviewView: PreviewView
    private lateinit var imageCapture: ImageCapture
    private lateinit var imageView1: ImageView
    private lateinit var cameraProvider: ProcessCameraProvider
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.camera_x_fragment, container, false)
        cameraPreviewView = view.findViewById(R.id.camera_preview_view)
        imageView1 = view.findViewById(R.id.imageView3)
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(Runnable {
            cameraProvider = cameraProviderFuture.get()
            bindPreview()
        }, ContextCompat.getMainExecutor(requireContext()))
        view.button3.setOnClickListener {
            f = false
        }
        view.button4.setOnClickListener {
            f = true
        }
        val cameraExecutor = Executors.newSingleThreadExecutor()
        imageView1.isClickable = true

        imageView1.setOnClickListener {
            Log.i("Log_tag", "1")
            var file = File("f")
            file = File(file.absolutePath + "/img1.jpg")
            var file2 = requireContext().filesDir
            Log.i("Log_tag", file2.absolutePath)
            Log.i("Log_tag", file.absolutePath)
            var outPutFileOptions: ImageCapture.OutputFileOptions
            outPutFileOptions = if (f) {
                val f = file2.absolutePath + "/image2.jpg"
                Log.i("Log_tag", "F : $f")
                ImageCapture.OutputFileOptions.Builder(File(f))
                    .build()
            } else {
                ImageCapture.OutputFileOptions.Builder(File(file2.absolutePath + "/image1.jpg"))
                    .build()
            }
            Log.i("Log_tag", "2")
            imageCapture.takePicture(outPutFileOptions, cameraExecutor,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        Log.i("Log_tag", "Saved${outputFileResults.savedUri?.path}")
                        f = !f
                        val navDirections =
                            CameraXFragmentDirections.actionCameraXFragmentToCropImageFragment(if (!f) {
                                "/data/user/0/com.dedstudio.testopencv5/files/image2.jpg"
                            } else {
                                "/data/user/0/com.dedstudio.testopencv5/files/image1.jpg"
                            })
                        findNavController().navigate(navDirections)
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Log.i("Log_tag", "Error: ${exception.message}")
                    }

                })
            Log.i("Log_tag", "3")
        }
        val button = view.findViewById<Button>(R.id.button)
        button.setOnClickListener {
            findNavController().navigate(R.id.matchingFragment)
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CameraXViewModel::class.java)
    }

    private fun bindPreview() {
        val preview = Preview.Builder().build()
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        imageCapture = ImageCapture.Builder()
            .build()

        preview.setSurfaceProvider(cameraPreviewView.createSurfaceProvider())

        val camera = cameraProvider.bindToLifecycle(
            this as LifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )

    }

    override fun onStop() {
        super.onStop()
        cameraProvider.unbindAll()

    }
}