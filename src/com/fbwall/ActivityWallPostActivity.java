package com.fbwall;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.tanzeer.fbimagepost.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ActivityWallPostActivity  extends Activity{
	
	Button btnUpload;
	int PICK_IMAGE=100;
	ImageView iv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_image);
		btnUpload=(Button)findViewById(R.id.button1);
		iv=(ImageView)findViewById(R.id.imageView1);
		btnUpload.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);	
				
			}
		});
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode==PICK_IMAGE)
		{
			 Uri _uri = data.getData();
			Cursor cursor = getContentResolver().query(_uri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
	        cursor.moveToFirst();

	        //Link to the image
	        final String imageFilePath = cursor.getString(0);
	        Log.e("msg",imageFilePath.toString());
	        cursor.close();
	        iv.setImageBitmap(BitmapFactory.decodeFile(imageFilePath.toString()));
	        postPic(imageFilePath.toString());
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	private class PostPic extends AsyncTask<Void,Void,Void>
	{

		@Override
		protected Void doInBackground(Void... params) {
			
			return null;
		}
		
	}
	private void postPic(String url)
	{
		Bundle params = new Bundle();
		Bitmap bitmap=BitmapFactory.decodeFile(url);
		params.putParcelable("source", bitmap);
		params.putString("message","this is a test photo for posting an image and text");
		Session session=Session.getActiveSession();
		if(session==null)
			Log.e("response","session null");
		else
			Log.e("ac",session.getAccessToken().toString());
		Request request=new Request(Session.getActiveSession(),"/me/photos",params,HttpMethod.POST,new Request.Callback() {
			
			@Override
			public void onCompleted(Response response) {
				Log.e("response",response.toString());
				Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
				
			}
		});
		request.executeAsync();
	}

}
