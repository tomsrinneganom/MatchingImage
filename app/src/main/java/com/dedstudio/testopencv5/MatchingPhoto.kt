package com.dedstudio.testopencv5

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.features2d.Features2d
import org.opencv.features2d.FlannBasedMatcher
import org.opencv.features2d.ORB
import org.opencv.imgproc.Imgproc

class MatchingPhoto() {
    public fun matching(context: Context, image1:Bitmap, image2:Bitmap): Bitmap {
        OpenCVLoader.initDebug()
        var bitmapImg1 = image1
        var bitmapImg2 = image2

        bitmapImg1 = Bitmap.createScaledBitmap(bitmapImg1, 500, 500, false)
        bitmapImg2 = Bitmap.createScaledBitmap(bitmapImg2, 500, 500, false)

        val image1 = Mat()
        Utils.bitmapToMat(bitmapImg1, image1, false)
        Imgproc.cvtColor(image1, image1, Imgproc.COLOR_BGR2GRAY)
        val mask1 = Mat()
        val keyPoints1 = MatOfKeyPoint()
        val descriptors1 = Mat()

        val image2 = Mat()
        Utils.bitmapToMat(bitmapImg2, image2, false)
        Imgproc.cvtColor(image2, image2, Imgproc.COLOR_BGR2GRAY)
        val mask2 = Mat()
        val keyPoints2 = MatOfKeyPoint()
        val descriptors2 = Mat()

        val detector: ORB = ORB.create()

        detector.detectAndCompute(image1, mask1, keyPoints1, descriptors1)
        detector.detectAndCompute(image2, mask2, keyPoints2, descriptors2)

        val matches = ArrayList<MatOfDMatch>()
        if (descriptors1.type() != CvType.CV_32F) {
            Log.i("Log_tag", "convert descriptor1")
            descriptors1.convertTo(descriptors1, CvType.CV_32F)
        }
        if (descriptors2.type() != CvType.CV_32F) {
            Log.i("Log_tag", "convert descriptor2")
            descriptors2.convertTo(descriptors2, CvType.CV_32F)
        }
        val flannBasedMatcher = FlannBasedMatcher.create()
        flannBasedMatcher.knnMatch(descriptors1, descriptors2, matches, 2)
        Log.i("Log_tag", "matches size${matches.size}")
        val ratioThresh = 0.7f
        val listOfGoodMatches: MutableList<DMatch> = ArrayList()
        for (i in 0 until matches.size) {
            if (matches[i].rows() > 1) {
                val m: Array<DMatch> = matches[i].toArray()
                if (m[0].distance < ratioThresh * m[1].distance) {
                    listOfGoodMatches.add(m[0])
                }
            }
        }
        val goodMatches = MatOfDMatch()
        goodMatches.fromList(listOfGoodMatches)
        Log.i(
            "Log_tag",
            "goodMatches: ${goodMatches.size()} width: ${goodMatches.width()} height: ${goodMatches.height()}"
        )
        Toast.makeText(context,"goodMatches: ${goodMatches.size()} width: ${goodMatches.width()} height: ${goodMatches.height()}",Toast.LENGTH_LONG).show()
        val imgMatches = Mat()
        Features2d.drawMatches(
            image1,
            keyPoints1,
            image2,
            keyPoints2,
            goodMatches,
            imgMatches,
            Scalar.all(-1.0),
            Scalar.all(-1.0),
            MatOfByte(),
            Features2d.DrawMatchesFlags_NOT_DRAW_SINGLE_POINTS
        )
        var bitmap = Bitmap.createBitmap(imgMatches.width(), imgMatches.height(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(imgMatches, bitmap)
//        image.setImageBitmap(bitmap)

        return bitmap
    }

}