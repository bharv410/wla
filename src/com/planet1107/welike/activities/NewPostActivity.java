package com.planet1107.welike.activities;

import com.findatrainerapp.welike.R;
import com.planet1107.welike.connect.Connect;
import com.planet1107.welike.other.ChoosePictureUtility;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class NewPostActivity extends Activity {

	private static final int SELECT_PHOTO = 100;
	private static final int AVIARY_PHOTO = 101;
	
	String post;
	ImageView mImageViewNewPostImage;
	TextView mTextViewSelectPhoto;
	EditText mEditTextPost;
	Bitmap image;
	SendPostTask mSendPostTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_post);
		setTitle("New Post");

		mImageViewNewPostImage = (ImageView) findViewById(R.id.imageViewNewPostImage);
		mTextViewSelectPhoto = (TextView) findViewById(R.id.textViewSelectPhoto);
		mEditTextPost = (EditText) findViewById(R.id.editTextPost);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.new_post, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int itemId = item.getItemId();
		if (itemId == R.id.action_cancel) {
			menuItemCancelOnClick();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void menuItemCancelOnClick() {
		
		finish();
	}
	
	public void buttonSendOnClick(View v) {
		
		attemptSendPost();
	}
	
	public void attemptSendPost() {
		
		if (mSendPostTask == null) {
			
			mEditTextPost.setError(null);
			mTextViewSelectPhoto.setError(null);
			
			post = mEditTextPost.getText().toString();

			boolean cancel = true;
			View focusView = null;

			if (TextUtils.isEmpty(post)) {
				mEditTextPost.setError(getString(R.string.error_field_required));
				focusView = mEditTextPost;
			} else if (image == null) {
				mTextViewSelectPhoto.setError(getString(R.string.error_field_required));
				focusView = mTextViewSelectPhoto;
			} else {
				cancel = false;
				mSendPostTask = new SendPostTask();
				mSendPostTask.execute();
			}

			if (cancel) {
				focusView.requestFocus();
			}
		}
	}
	
	private class SendPostTask extends AsyncTask<Void, Void, Void> {
	     
		private ProgressDialog mLoadingDialog = new ProgressDialog(NewPostActivity.this);

		@Override
	    protected void onPreExecute() {
	    	
	    	mLoadingDialog.setMessage("Posting...");
	    	mLoadingDialog.show();
	    }
		
		@Override
		protected Void doInBackground(Void... arg) {
	         
			Connect sharedConnect = Connect.getInstance(getBaseContext());
			sharedConnect.sendPost(mEditTextPost.getText().toString(), image);
			return null;
	     }

		@Override
	    protected void onPostExecute(Void arg) {
	         
			mLoadingDialog.dismiss();
			finish();
	    }
		
		@Override
		protected void onCancelled() {
			
			mSendPostTask = null;
			mLoadingDialog.dismiss();

		}
	}
	
	public void imageViewPostImageOnClick(View v) {
		
		ChoosePictureUtility.getInstance().choosePicture(this);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		    
		super.onActivityResult(requestCode, resultCode, data); 
		switch (requestCode) { 
			case ChoosePictureUtility.SELECT_PHOTO:
				ChoosePictureUtility.getInstance().onActivityResult(requestCode, resultCode, data);
				Uri bitmapUri = ChoosePictureUtility.getInstance().getPickedImageUri();
				if (bitmapUri != null) {
		            image = BitmapFactory.decodeFile(getRealPathFromURI( this, bitmapUri));
		            mImageViewNewPostImage.setImageBitmap(image);
				}
				break;		    
	    }
	}
	public String getRealPathFromURI(Context context, Uri contentUri) {
		  Cursor cursor = null;
		  try { 
		    String[] proj = { MediaStore.Images.Media.DATA };
		    cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
		    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		    cursor.moveToFirst();
		    return cursor.getString(column_index);
		  } finally {
		    if (cursor != null) {
		      cursor.close();
		    }
		  }
		}
}

