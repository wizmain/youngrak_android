package com.youngrak;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.youngrak.common.Constant;
import com.youngrak.common.HttpMultipartClient;
import com.youngrak.common.HttpMultipartClient.OnProgressListener;
import com.youngrak.common.ImageUtil;
import com.youngrak.common.StringUtils;
import com.youngrak.common.dialog.DialogUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ChangeImage extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
	
	final static String TAG = "ChangeImage";
	ImageView currentImage;
	Uri selectedUri = null;
	String cm1ID = "";
	String selectedImageType = "1";
	String currImageUrl = "";
	ImageUtil imageUtil = new ImageUtil();
	
	@Override
    public void onCreate(Bundle icicle) {
    	
        super.onCreate(icicle);
        setContentView(R.layout.change_image);
        
        new TopMenuInitializer(this, (View)findViewById(R.id.top_menubar), "사진변경");
        
        cm1ID = getIntent().getStringExtra("cm1ID");
        currImageUrl = getIntent().getStringExtra("currImageUrl");
        currentImage = (ImageView)findViewById(R.id.selectedImage);
        
        RadioGroup imageTypeGroup = (RadioGroup)findViewById(R.id.imageType);
        imageTypeGroup.setOnCheckedChangeListener(this);
        
        
        ImageButton imageSelectButton = (ImageButton)findViewById(R.id.imageSelect);
        imageSelectButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, "사진선택"),1);
			}
		});
        ImageButton imageUploadButton = (ImageButton)findViewById(R.id.imageUpload);
        imageUploadButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(selectedUri == null){
					DialogUtils.alert(thisContext, "", "사진을 선택해 주세요");
				} else {
					String filePath = getRealPathFromURI(selectedUri);
					new FileUploadTask().execute(filePath, cm1ID);
				}
				
			}
		});
        
        if(!StringUtils.isEmptyOrBlank(currImageUrl)){
        	Log.d(TAG, currImageUrl);
        	//new ImageLoadTask().execute(currImageUrl);
        	imageUtil.getImageLoad(currentImage, currImageUrl);
        	
        }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("ChangeImage", "ResultCode="+resultCode);
		super.onActivityResult(requestCode, resultCode, data);
		
		
	    if (resultCode == RESULT_OK) {
	        if (requestCode == 1) {
	        	Log.d("ChangeImage", "requestCode="+requestCode);
	        // currImageURI is the global variable I’m using to hold the content:
	            Uri currImageURI = data.getData();
	            String path = getRealPathFromURI(currImageURI);
	            Bitmap currImageBitmap = null;
	            Options opts = new Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(path, opts); 
                Log.e("optwidth",opts.outWidth+""); 
                opts.inJustDecodeBounds = false; 
                if(opts.outWidth>500){ 
                        opts.inSampleSize = 4; 
                        currImageBitmap = BitmapFactory.decodeFile(path, opts); 
                } else { 
                	currImageBitmap = BitmapFactory.decodeFile(path, opts); 
                }
	            
	            //currentImage.setImageURI(currImageURI);
                currentImage.setImageBitmap(currImageBitmap);
	            selectedUri = currImageURI;
	        }
	    }
	    
	}
	
	private String getRealPathFromURI(Uri contentURI) {
	    Cursor cursor = getContentResolver()
	               .query(contentURI, null, null, null, null); 
	    cursor.moveToFirst(); 
	    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
	    return cursor.getString(idx); 
	}
	
	
	private class FileUploadTask extends AsyncTask<String, Integer, Void> implements OnProgressListener {
 		
 		private ProgressDialog dialog = null;
 		private String fileName=null;
 		
 		// 이곳에 포함된 code는 AsyncTask가 execute 되자 마자 UI 스레드에서 실행됨.
 		// 작업 시작을 UI에 표현하거나
 		// background 작업을 위한 ProgressBar를 보여 주는 등의 코드를 작성.
 		protected void onPreExecute(){
 			super.onPreExecute();
 			
 			/*
 			dialog = new ProgressDialog(thisContext);
            dialog.setMessage("Uploading...");
            dialog.setIndeterminate(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setProgress(0);
            dialog.show();
            */
            
            dialog = ProgressDialog.show(thisContext, "", "전송중입니다...");
 		}
 		
 		// UI 스레드에서 AsynchTask객체.execute(...) 명령으로 실행되는 callback
 		@Override
 		protected Void doInBackground(String... args) {
 			String filePath = args[0];
			File uploadFile = new File(filePath);
			fileName = StringUtils.getFileNameWithoutExt(filePath);
			if(!uploadFile.exists()){
				Toast.makeText(thisContext, "file not exist", Toast.LENGTH_SHORT);
				return null;
			}
			
			HttpClient httpClient = appEx.getHttpClient();
			HttpPost httpPost = new HttpPost(Constant.SERVER_ADDR+Constant.CHERISH_IMAGE_UPLOAD_URL);
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("file", new FileBody(uploadFile));
			try {
				reqEntity.addPart("cm1_id", new StringBody(cm1ID,Charset.forName("UTF-8")));
				reqEntity.addPart("img_select", new StringBody(selectedImageType,Charset.forName("UTF-8")));
				
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			httpPost.setEntity(reqEntity);
			
			try {
				HttpResponse response = httpClient.execute(httpPost);
				Log.d("ChangeImage", response.toString());
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*
 			try {
		        
 				
		        //HttpMultipartClient httpMultipartClient = new HttpMultipartClient("www.smartinterview.co.kr", "/m.upload_test.php", 80);
 				HttpMultipartClient httpMultipartClient = new HttpMultipartClient(Constant.SERVER, Constant.CHERISH_IMAGE_UPLOAD_URL, Constant.SERVER_PORT);
		        httpMultipartClient.setOnProgressListener(this);
		        Log.d(TAG, "filePath = "+filePath);
		        try {
					FileInputStream fis = new FileInputStream(filePath);
					//httpMultipartClient.addFile(fileName, fis, fis.available());
					httpMultipartClient.addFile(fileName, fis, fis.available());
					httpMultipartClient.setRequestMethod("POST");
					
					httpMultipartClient.addField("cm1_id", cm1ID);
					httpMultipartClient.addField("img_select", selectedImageType);
					httpMultipartClient.send();
					
					Log.e("HttpMultiparClient ResponseBody",httpMultipartClient.getResponseBody());
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
					//Toast.makeText(thisContext, "file not exist", Toast.LENGTH_SHORT);
					handlerShowToastMessage("file not exist", Toast.LENGTH_SHORT);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					//Toast.makeText(thisContext, "io exception", Toast.LENGTH_SHORT);
					handlerShowToastMessage("IO Exception 업로드하지 못했습니다.", Toast.LENGTH_SHORT);
				}
 				
 			} catch (Exception e){
 				e.printStackTrace();
 				handlerShowToastMessage("업로드하지 못했습니다.", Toast.LENGTH_SHORT);
 			}
 			*/
			return null;
 		}
 		
 		// onInBackground(...)가 완료되면 자동으로 실행되는 callback
 		// 이곳에서 onInBackground가 리턴한 정보를 UI위젯에 표시 하는 등의 작업을 수행함.
 		// (예제에서는 작업에 걸린 총 시간을 UI위젯 중 TextView에 표시함)
 		protected void onPostExecute(Void result) {
 			if(dialog != null){
 				dialog.dismiss();
 				dialog = null;
 			}
 			
 			
 			Toast.makeText(thisContext, "전송이 완료되었습니다.", Toast.LENGTH_SHORT);
 			//onBackPressed();
 			Intent intent = new Intent();
 			//Bundle bundle = new Bundle();
 			//bundle.putString("filename", fileName);
 			intent.putExtra("fileName", fileName);
 			((Activity)thisContext).setResult(RESULT_OK, intent);
 			finish();
 		}
 		
 		
 		// AsyncTask.cancel(boolean) 메소드가 true 인자로
 		// 실행되면 호출되는 콜백.
 		// background 작업이 취소될때 꼭 해야될 작업은  여기에 구현.
 		@Override
 		protected void onCancelled() {
 			// TODO Auto-generated method stub
 			super.onCancelled();
 			
 			if(dialog != null){
 				dialog.dismiss();
 				dialog = null;
 			}
 		}

		@Override
		public void onFileProgressChanged(int progress) {
			
			//publishProgress(progress);
			dialog.setProgress(progress);
			if(progress == 100)
				thisHandler.post(updateProgressDialogMessage);
			Log.d("UploadTest", "onFileProgressChanged progress="+progress);
		}
		
		private Runnable updateProgressDialogMessage = new Runnable(){
			@Override
			public void run(){
				dialog.setMessage("저장중입니다...잠시만기다려주세요");
			}
		};
		 	
 	}


	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if(checkedId == R.id.imagePortrait) {
			selectedImageType = "1";
		} else if(checkedId == R.id.imageTomb) {
			selectedImageType = "2";
		}
	}
	
	
	private class ImageLoadTask extends AsyncTask<String, Integer, Bitmap> {
	 		
 		protected void onPreExecute(){
 			super.onPreExecute();
 		}
 		
 		@Override
 		protected Bitmap doInBackground(String... args) {
 			URL url;
 			Bitmap bmp = null;
			try {
				url = new URL(Constant.SERVER_ADDR + args[0]);
				
				/*
				 * Options opts = new Options(); 
        opts.inJustDecodeBounds = true; 
        BitmapFactory.decodeFile(path, opts); 
        Log.e("optwidth",opts.outWidth+""); 
        opts.inJustDecodeBounds = false; 
        if(opts.outWidth>500){ 
                opts.inSampleSize = 4; 
                mBitmap = BitmapFactory.decodeFile(path, opts); 
        } 
        else mBitmap = BitmapFactory.decodeFile(path, opts); 
				 */
				
				Options opts = new Options(); 
                //opts.inJustDecodeBounds = true;
				opts.inSampleSize = 8;
                
				bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, opts);
				
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
 			return bmp;
 		}
 		
 		protected void onPostExecute(Bitmap result) {
 			currentImage.setImageBitmap(result);
 		}
 		
 		@Override
 		protected void onCancelled() {
 			// TODO Auto-generated method stub
 			super.onCancelled();
 		}
 	
 	}
}
