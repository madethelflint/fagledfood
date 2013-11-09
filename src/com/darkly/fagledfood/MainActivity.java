package com.darkly.fagledfood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class MainActivity extends Activity {

	private Button mCameraButton;
	private Bitmap mBitmap;
	private ImageView mImageView;
	private String mCurrentPhotoPath;
	private TextView mRecognizedText;

	private String JPEG_FILE_PREFIX;
	private static final String EXTRAS_PATH_VALUE = "com.darkly.foodtranslator.current_path";

	private Intent takePictureIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		JPEG_FILE_PREFIX = "JPG";

		mCameraButton = (Button)findViewById(R.id.camera_button);
		mImageView = (ImageView)findViewById(R.id.captured_image);
		mRecognizedText = (TextView)findViewById(R.id.recognized_text);

		mCameraButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dispatchTakePictureIntent(0);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // Handle action bar item clicks here. The action bar will
	        // automatically handle clicks on the Home/Up button, so long
	        // as you specify a parent activity in AndroidManifest.xml.
	        switch (item.getItemId()) {
	            case R.id.action_settings:
	                return true;
	        }
	        return super.onOptionsItemSelected(item);
	    }

		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data){
			try{
				galleryAddPic();

				Ocr ocr = new Ocr();
				String text = ocr.exifHandler(mCurrentPhotoPath);
				
				mRecognizedText.setText(text);
				mImageView.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));
			}catch (Exception e){
				String x = e.getMessage();
			}
		}


		private void dispatchTakePictureIntent(int actionCode) {
			takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			try{
				File f = createImageFile();
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
				takePictureIntent.putExtra(MainActivity.EXTRAS_PATH_VALUE, mCurrentPhotoPath);
			}catch (IOException exception){
				String x = exception.getMessage();
			}
			startActivityForResult(takePictureIntent, actionCode);
		}

		public static boolean isIntentAvailable(Context context, String action) {
			final PackageManager packageManager = context.getPackageManager();
			final Intent intent = new Intent(action);
			List<ResolveInfo> list =
					packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
			return list.size() > 0;
		}

		private File createImageFile() throws IOException {
			// Create an image file name
			String timeStamp =
					new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			String imageFileName = JPEG_FILE_PREFIX + timeStamp;
			File image = File.createTempFile(
					imageFileName,
					".jpg",
					getAlbumDir()
			);
			mCurrentPhotoPath = image.getAbsolutePath();

			return image;
		}

		private File getAlbumDir(){
			File storageDir = new File(
					Environment.getExternalStoragePublicDirectory(
							Environment.DIRECTORY_PICTURES
					),
					getAlbumName()
			);

			if(!storageDir.isDirectory())
				storageDir.mkdir();
			return storageDir;
		}

		private String getAlbumName(){
			return "FoodTranslator";
		}

		private void galleryAddPic() {
			Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			File f = new File(mCurrentPhotoPath);
			Uri contentUri = Uri.fromFile(f);
			mediaScanIntent.setData(contentUri);
			this.sendBroadcast(mediaScanIntent);
		}


}
