package com.dedstudio.testopencv5

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.matching_fragment.*
import kotlinx.android.synthetic.main.matching_fragment.view.*
import org.opencv.android.OpenCVLoader
import org.opencv.core.MatOfDMatch
import java.io.File

class MatchingFragment : Fragment() {

    companion object {
        fun newInstance() = MatchingFragment()
    }

    private lateinit var viewModel: MatchingViewModel
    private lateinit var imageView: ImageView
    private lateinit var imageView2: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.matching_fragment, container, false)
        imageView = view.findViewById(R.id.imageView)
        imageView2 = view.findViewById(R.id.imageView2)
        var fg = 0
        var s: String = ""
        val keyPoint1 = MutableLiveData<Int>()
        val keyPoint2 = MutableLiveData<Int>()
        val keyPoint3 = MutableLiveData<Int>()
        val keyPoint4 = MutableLiveData<Int>()
        keyPoint1.observeForever {
            Log.i("Log_tag", "$it")
            s += "keypoint1: $it"
            fg += 1
        }
        keyPoint2.observeForever {
            Log.i("Log_tag", "$it")
            s += "keypoint2: $it"
            fg += 1
        }
        keyPoint3.observeForever {
            Log.i("Log_tag", "$it")
            s += "keypoint3: $it"
            fg += 1
        }
        keyPoint4.observeForever {
            s += "keypoint4: $it"
            Log.i("Log_tag", "$it")
        }

        val f =
            MatchingPhoto2().run(requireContext(), keyPoint1, keyPoint2) + MatchingPhoto2().run2(
                requireContext(),
                keyPoint3,
                keyPoint4)
        view.textView.text = f
        var bitmap1 =
            BitmapFactory.decodeFile("/data/user/0/com.dedstudio.testopencv5/files/image1.jpg")
        var bitmap2 =
            BitmapFactory.decodeFile("/data/user/0/com.dedstudio.testopencv5/files/image2.jpg")
        bitmap1 =
            Bitmap.createScaledBitmap(bitmap1, bitmap1.width / 10, bitmap1.height / 10, false)
        bitmap2 =
            Bitmap.createScaledBitmap(bitmap2, bitmap2.width / 10, bitmap2.height / 10, false)
        imageView.setImageBitmap(bitmap1)
        imageView2.setImageBitmap(bitmap2)
//        var bitmap1: Bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888)
//        var bitmap2: Bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888)
//
//        var file = File("/data/user/0/com.dedstudio.testopencv5/files/image1.jpg")
//        if (file.exists()) {
//            bitmap1 =
//                BitmapFactory.decodeFile(file.absolutePath)
//        } else {
//            Log.i("Log_tag", "!exists")
//        }
//        file = File("/data/user/0/com.dedstudio.testopencv5/files/image2.jpg")
//        if (file.exists()) {
//            bitmap2 =
//                BitmapFactory.decodeFile(file.absolutePath)
//        } else {
//            Log.i("Log_tag", "!exists")
//        }
//        imageView.setImageBitmap(MatchingPhoto().matching(requireContext(), bitmap1, bitmap2))
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MatchingViewModel::class.java)
    }

}