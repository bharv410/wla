package com.planet1107.welike.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.findatrainerapp.welike.R;
import com.planet1107.welike.connect.Connect;

public class UploadVideoActivity extends Activity {
	Button promoVideoButton;
	AmazonS3Client s3Client;
	Intent videoReceiveIntent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_video);
		promoVideoButton=(Button)findViewById(R.id.promoVideoButton);
		
		
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				s3Client = new AmazonS3Client( new BasicAWSCredentials( "AKIAI3SJ7KNK6QV27OEA", "h41TVvT2nyl+N1/sl1abiaWdswhFn3zXuVizV0XG" ) );
				return"done";
			}
		}.execute();
	}
	
	public void uploadVideo(View v){
		Intent getVideo = new Intent(Intent.ACTION_GET_CONTENT);
		getVideo.setType("video/*");
		startActivityForResult(getVideo,1);
	}
	@Override 
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == 1) {
	        if (resultCode == RESULT_OK) {
	        	videoReceiveIntent=data;
	        	new AsyncTask<Void, Void, String>() {
	    			@Override
	    			protected String doInBackground(Void... params) {
	    				try{
	    				PutObjectRequest por = new PutObjectRequest( "fitovatevideoss",Connect.getInstance(getApplicationContext()).getCurrentUser().userUsername,  new java.io.File( getFilePathFromContentUri(videoReceiveIntent.getData(),getContentResolver())) );  
	    	            por.setCannedAcl(CannedAccessControlList.PublicRead);
	    				s3Client.putObject( por );
	    				}catch(Exception e){
	    					return "exception";
	    				}
	    				return"done";
	    			}
	    			@Override
	    			protected void onPostExecute(String uploaded) {
	    				if(uploaded.contains("exception"))
	    					Toast.makeText(getApplicationContext(), "choose different video", Toast.LENGTH_SHORT).show();
	    				else{
	    					Intent main = new Intent(UploadVideoActivity.this, MainActivity.class);
	    				startActivity(main);
	    				}
	    			}
	    		}.execute();
	        	
	            
	            
	        } 
	    } 
	} 
	private String getFilePathFromContentUri(Uri selectedVideoUri,
	        ContentResolver contentResolver) {
	    String filePath;
	    String[] filePathColumn = {MediaColumns.DATA};
	 
	    Cursor cursor = contentResolver.query(selectedVideoUri, filePathColumn, null, null, null);
	    cursor.moveToFirst();
	 
	    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	    filePath = cursor.getString(columnIndex);
	    cursor.close();
	    return filePath;
	} 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.upload_video, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
