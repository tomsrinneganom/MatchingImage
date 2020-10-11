package com.dedstudio.testopencv5

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.calib3d.Calib3d
import org.opencv.core.*
import org.opencv.features2d.*
import org.opencv.imgcodecs.Imgcodecs


class MatchingPhoto2 {

    fun run(
        context: Context,
        keyPoint1: MutableLiveData<Int>,
        keyPoint2: MutableLiveData<Int>
    ): String {
        OpenCVLoader.initDebug()
        val filenameObject = "/data/user/0/com.dedstudio.testopencv5/files/image2.jpg"
        val filenameScene = "/data/user/0/com.dedstudio.testopencv5/files/image1.jpg"
        val image1 = BitmapFactory.decodeFile(filenameObject)
        val image2 = BitmapFactory.decodeFile(filenameScene)
        Bitmap.createScaledBitmap(image1, 120, 80, false)
        Bitmap.createScaledBitmap(image2, 120, 80, false)
//        val imgObject = Imgcodecs.imread(filenameObject, Imgcodecs.IMREAD_GRAYSCALE)
//        val imgScene = Imgcodecs.imread(filenameScene, Imgcodecs.IMREAD_GRAYSCALE)
        val imgObject = Mat()
        val imgScene = Mat()
        Utils.bitmapToMat(image1, imgObject)
        Utils.bitmapToMat(image2, imgScene)

        if (imgObject.empty() || imgScene.empty()) {
            Log.i("Log_tag", "image empty")
            return "f"
        }
        //-- Step 1: Detect the keypoints using SURF Detector, compute the descriptors
        val hessianThreshold = 400.0
        val nOctaves = 4
        val nOctaveLayers = 3
        val extended = false
        val upright = false
        val detector = ORB.create()
        val detectors2 = BRISK.create()
        val keypointsObject = MatOfKeyPoint()
        val keypointsScene = MatOfKeyPoint()
        val descriptorsObject = Mat()
        val descriptorsScene = Mat()
        detector.detectAndCompute(imgObject, Mat(), keypointsObject, descriptorsObject)
        detector.detectAndCompute(imgScene, Mat(), keypointsScene, descriptorsScene)
        //-- Step 2: Matching descriptor vectors with a FLANN based matcher
        if (descriptorsScene.type() != CvType.CV_32F) {
            Log.i("Log_tag", "convert descriptor1")
            descriptorsScene.convertTo(descriptorsScene, CvType.CV_32F)
        }
        if (descriptorsObject.type() != CvType.CV_32F) {
            Log.i("Log_tag", "convert descriptor2")
            descriptorsObject.convertTo(descriptorsObject, CvType.CV_32F)
        }
        // Since SURF is a floating-point descriptor NORM_L2 is used
//        val matcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED)
        val matcher = FlannBasedMatcher.create()
        val knnMatches: List<MatOfDMatch> = ArrayList()
        matcher.knnMatch(descriptorsObject, descriptorsScene, knnMatches, 2)
        //-- Filter matches using the Lowe's ratio test
        val ratioThresh = 0.75f
        val listOfGoodMatches: MutableList<DMatch> = ArrayList()
        for (i in knnMatches.indices) {
            if (knnMatches[i].rows() > 1) {
                val matches = knnMatches[i].toArray()
                if (matches[0].distance < ratioThresh * matches[1].distance) {
                    listOfGoodMatches.add(matches[0])
                }
            }
        }
        val goodMatches = MatOfDMatch()
        goodMatches.fromList(listOfGoodMatches)
        //-- Draw matches
        val imgMatches = Mat()
        Features2d.drawMatches(
            imgObject,
            keypointsObject,
            imgScene,
            keypointsScene,
            goodMatches,
            imgMatches,
            Scalar.all(-1.0),
            Scalar.all(-1.0),
            MatOfByte(),
            Features2d.DrawMatchesFlags_DEFAULT)
        Log.i("Log_tag", "matches:${goodMatches.size()}")
        Log.i("Log_tag", "matches size :${imgMatches.width()} ${imgMatches.height()}")
        Toast.makeText(context, "matches:${goodMatches.size()}", Toast.LENGTH_LONG).show()
        keyPoint1.value = keypointsScene.height()
        keyPoint2.value = keypointsObject.height()
        var bitmap =
            Bitmap.createBitmap(imgMatches.width(), imgMatches.height(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(imgMatches, bitmap)
//        return Bitmap.createScaledBitmap(bitmap, 1200, 800, false)
            return "keypoint1 height: ${keypointsObject.height()} keypoint2 height: ${keypointsScene.height()} good matches:${goodMatches.size()}"
    }

    fun run2(context: Context,
             keyPoint1: MutableLiveData<Int>,
             keyPoint2: MutableLiveData<Int>): String {
        OpenCVLoader.initDebug()
        val filenameObject = "/data/user/0/com.dedstudio.testopencv5/files/image2.jpg"
        val filenameScene = "/data/user/0/com.dedstudio.testopencv5/files/image1.jpg"
        val image1 = BitmapFactory.decodeFile(filenameObject)
        val image2 = BitmapFactory.decodeFile(filenameScene)
        Bitmap.createScaledBitmap(image1, 120, 80, false)
        Bitmap.createScaledBitmap(image2, 120, 80, false)
//        val imgObject = Imgcodecs.imread(filenameObject, Imgcodecs.IMREAD_GRAYSCALE)
//        val imgScene = Imgcodecs.imread(filenameScene, Imgcodecs.IMREAD_GRAYSCALE)
        val imgObject = Mat()
        val imgScene = Mat()
        Utils.bitmapToMat(image1, imgObject)
        Utils.bitmapToMat(image2, imgScene)

        if (imgObject.empty() || imgScene.empty()) {
            Log.i("Log_tag", "image empty")
            return "f"
        }
        //-- Step 1: Detect the keypoints using SURF Detector, compute the descriptors
        val hessianThreshold = 400.0
        val nOctaves = 4
        val nOctaveLayers = 3
        val extended = false
        val upright = false
        val detector = BRISK.create()
        val keypointsObject = MatOfKeyPoint()
        val keypointsScene = MatOfKeyPoint()
        val descriptorsObject = Mat()
        val descriptorsScene = Mat()
        detector.detectAndCompute(imgObject, Mat(), keypointsObject, descriptorsObject)
        detector.detectAndCompute(imgScene, Mat(), keypointsScene, descriptorsScene)
        //-- Step 2: Matching descriptor vectors with a FLANN based matcher
        if (descriptorsScene.type() != CvType.CV_32F) {
            Log.i("Log_tag", "convert descriptor1")
            descriptorsScene.convertTo(descriptorsScene, CvType.CV_32F)
        }
        if (descriptorsObject.type() != CvType.CV_32F) {
            Log.i("Log_tag", "convert descriptor2")
            descriptorsObject.convertTo(descriptorsObject, CvType.CV_32F)
        }
        // Since SURF is a floating-point descriptor NORM_L2 is used
//        val matcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED)
        val matcher = FlannBasedMatcher.create()
        val knnMatches: List<MatOfDMatch> = ArrayList()
        matcher.knnMatch(descriptorsObject, descriptorsScene, knnMatches, 2)
        //-- Filter matches using the Lowe's ratio test
        val ratioThresh = 0.75f
        val listOfGoodMatches: MutableList<DMatch> = ArrayList()
        for (i in knnMatches.indices) {
            if (knnMatches[i].rows() > 1) {
                val matches = knnMatches[i].toArray()
                if (matches[0].distance < ratioThresh * matches[1].distance) {
                    listOfGoodMatches.add(matches[0])
                }
            }
        }
        val goodMatches = MatOfDMatch()
        goodMatches.fromList(listOfGoodMatches)
        //-- Draw matches
        val imgMatches = Mat()
        Features2d.drawMatches(
            imgObject,
            keypointsObject,
            imgScene,
            keypointsScene,
            goodMatches,
            imgMatches,
            Scalar.all(-1.0),
            Scalar.all(-1.0),
            MatOfByte(),
            Features2d.DrawMatchesFlags_DEFAULT)
        Log.i("Log_tag", "matches:${goodMatches.size()}")
        Log.i("Log_tag", "matches size :${imgMatches.width()} ${imgMatches.height()}")
        Toast.makeText(context, "matches:${goodMatches.size()}", Toast.LENGTH_LONG).show()
        keyPoint1.value = keypointsScene.height()
        keyPoint2.value = keypointsObject.height()
        var bitmap =
            Bitmap.createBitmap(imgMatches.width(), imgMatches.height(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(imgMatches, bitmap)
//        return Bitmap.createScaledBitmap(bitmap, 1200, 800, false)
        return "keypoint1 height: ${keypointsObject.height()} keypoint2 height: ${keypointsScene.height()} good matches:${goodMatches.size()}"
    }
}