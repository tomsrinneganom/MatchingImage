package com.dedstudio.testopencv5

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.applyCanvas
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_crop_image.view.*
import java.io.File
import java.io.FileOutputStream


class CropImageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val args: CropImageFragmentArgs by navArgs()
        val view = inflater.inflate(R.layout.fragment_crop_image, container, false)
        val cropImageView: CropImageView = view.cropImageView
        cropImageView.setImageUriAsync(Uri.fromFile(File(args.pathToImage)))
        view.button2.setOnClickListener {

           val result = cropImageView.croppedImage
            val fileOutputStream = FileOutputStream(File(args.pathToImage))
            result.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
            findNavController().navigate(R.id.matchingFragment)
        }
        return view
    }

}