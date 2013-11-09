package com.darkly.fagledfood;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

import com.googlecode.tesseract.android.TessBaseAPI;

/**
 * Created by rkendall on 11/9/13.
 */
public class Ocr {

	public Bitmap mBitmap;

	public Ocr(){

	}

	public String exifHandler(String _path){

		mBitmap = BitmapFactory.decodeFile(_path);

		ExifInterface exif = null;

		try{
			exif = new ExifInterface(_path);
		}catch (IOException exception){
			String x = exception.getMessage();
		}

		int exifOrientation = exif.getAttributeInt(
				ExifInterface.TAG_ORIENTATION,
				ExifInterface.ORIENTATION_NORMAL);

		int rotate = 0;

		switch (exifOrientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
		}

		if (rotate != 0) {
			int w = mBitmap.getWidth();
			int h = mBitmap.getHeight();

			// Setting pre rotate
			Matrix mtx = new Matrix();
			mtx.preRotate(rotate);

			// Rotating Bitmap & convert to ARGB_8888, required by tess
			mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, w, h, mtx, false);
		}
		mBitmap = mBitmap.copy(Bitmap.Config.ARGB_8888, true);

		TessBaseAPI baseApi = new TessBaseAPI();
		String trainPath = trainingData();
		baseApi.init(trainPath, "eng");
		baseApi.setImage(mBitmap);
		String recognizedText = baseApi.getUTF8Text();
		baseApi.end();

		return recognizedText;
		//return "";
	}

	public String trainingData(){
		String trainingData = "/mnt/sdcard/" + Environment.DIRECTORY_DOWNLOADS;// + "eng.traineddata";
		return trainingData;
	}

	public void saveTrainingData(){

	}
}
