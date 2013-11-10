package com.darkly.fagledfood;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class IngredientsActivity extends Activity {

	private ProgressBar mProgress;
	private TextView mStatus;
	private String mCurrentPhotoPath;
	private Ingredient ingredientModel;
	private List<Ingredient> definedIngredients;
	
	private static final String EXTRAS_PATH_VALUE = "com.darkly.foodtranslator.current_path";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);       
		setContentView(R.layout.activity_ingredients);
		
		mProgress = (ProgressBar)findViewById(R.id.progressBar);
		mStatus = (TextView)findViewById(R.id.textView);
		mStatus.setText("Processing");
		
		mCurrentPhotoPath = getIntent().getStringExtra(EXTRAS_PATH_VALUE);
		if(mCurrentPhotoPath != null){
			readImage();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ingredients, menu);
		return true;
	}



	private void readImage(){
		Ocr ocr = new Ocr();
		mStatus.setText("Interpretting Image...");
		String text = ocr.exifHandler(mCurrentPhotoPath);

		mStatus.setText("OCR is completed");
		mProgress.setVisibility(View.INVISIBLE);
		
		ingredientModel = new Ingredient();
		List<String> words = Arrays.asList(text.split("\\s*,\\s*"));
		
		for(int i=0; i<words.size(); i++){
			Ingredient ing = new Ingredient();
			ing.name = words.get(i);
			ingredientModel.ingredients.add(ing);
		}
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				List<Ingredient> definedIngredientsStuff = ingredientModel.processIngredients();
				
				definedIngredients = definedIngredientsStuff;
				
StringBuilder sb = new StringBuilder();
				
				for(int i=0; i<definedIngredients.size(); i++){
					Ingredient ing = definedIngredients.get(i);
					sb.append(ing.name).append(":: ").append(ing.definition).append("/n");
				}
				Log.d("Food", sb.toString());
				Log.d("Food", "got some data with " + definedIngredientsStuff.size() + "items");
				
			}
		}).start();
		// mRecognizedText.setText(text);
		// mImageView.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));

	}
	
	
}
