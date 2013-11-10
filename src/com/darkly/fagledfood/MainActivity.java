package com.darkly.fagledfood;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private Button mScanButton;
	private ImageView mIntroImage;
	private String mCurrentPhotoPath;
	private Intent takePictureIntent;
	
	private static final String JPEG_FILE_PREFIX = "aLabel";
	private static final String EXTRAS_PATH_VALUE = "com.darkly.foodtranslator.current_path";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mScanButton = (Button)findViewById(R.id.scan_button);
		mIntroImage = (ImageView)findViewById(R.id.intro_image);
		mScanButton.setOnClickListener(new View.OnClickListener() {
			
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			//galleryAddPic();
			
			Intent i = new Intent(MainActivity.this, IngredientsActivity.class);
			i.putExtra(EXTRAS_PATH_VALUE, mCurrentPhotoPath);
			startActivity(i);

		} catch (Exception e) {
			String x = e.getMessage();
			new AlertDialog.Builder(this)
					.setTitle("Exception In ActivityResult")
					.setMessage(x)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

								}

								public void dismiss() {
									// TODO Auto-generated method stub

								}

							}).show();
		}
	}

	private void dispatchTakePictureIntent(int actionCode) {
		takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		try {
			File f = createImageFile();
			takePictureIntent
					.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			takePictureIntent.putExtra(MainActivity.EXTRAS_PATH_VALUE,
					mCurrentPhotoPath);
		} catch (IOException exception) {
			String x = exception.getMessage();
			new AlertDialog.Builder(this)
					.setTitle("Exception In Dispatch")
					.setMessage(x)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

								}

								public void dismiss() {
									// TODO Auto-generated method stub

								}

							}).show();
		}
		startActivityForResult(takePictureIntent, actionCode);
	}

	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = JPEG_FILE_PREFIX + timeStamp;
		File image = File.createTempFile(imageFileName, ".jpg", getAlbumDir());
		mCurrentPhotoPath = image.getAbsolutePath();

		return image;
	}

	private File getAlbumDir() {
		File storageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				getAlbumName());

		if (!storageDir.isDirectory())
			storageDir.mkdir();
		return storageDir;
	}

	private String getAlbumName() {
		return "FoodTranslator";
	}

//	private void galleryAddPic() {
//		Intent mediaScanIntent = new Intent(
//				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//		File f = new File(mCurrentPhotoPath);
//		Uri contentUri = Uri.fromFile(f);
//		mediaScanIntent.setData(contentUri);
//		this.sendBroadcast(mediaScanIntent);
//	}

}
