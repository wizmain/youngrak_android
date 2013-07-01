package com.youngrak;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.youngrak.common.Constant;
import com.youngrak.common.MediaStoreThumbnail;
import com.youngrak.common.StringUtils;
import com.youngrak.common.HttpMultipartClient.OnProgressListener;
import com.youngrak.common.dialog.DialogUtils;

public class TributeMovieUpload extends BaseActivity {
	final static String TAG = "TributeAlbumUpload";
	ImageView currentImage;
	Uri selectedUri = null;
	String cm1ID = "";
	String selectedImageType = "1";
	String currImageUrl = "";
	private MediaStoreThumbnail mediaStoreThumbnail = new MediaStoreThumbnail();
	
	@Override
    public void onCreate(Bundle icicle) {
    	
        super.onCreate(icicle);
        setContentView(R.layout.movie_upload);
        
        new TopMenuInitializer(this, (View)findViewById(R.id.top_menubar), "동영상및육성파일등록");
        
        cm1ID = getIntent().getStringExtra("cm1ID");
        
        currentImage = (ImageView)findViewById(R.id.selectedImage);
        ImageButton imageSelectButton = (ImageButton)findViewById(R.id.imageSelect);
        imageSelectButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent();
				intent.setType("video/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, "동영상및육성선택"),1);
			}
		});
        ImageButton imageUploadButton = (ImageButton)findViewById(R.id.imageUpload);
        imageUploadButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(selectedUri == null){
					DialogUtils.alert(thisContext, "", "동영상을 선택해 주세요");
				} else {
					String filePath = getRealPathFromURI(selectedUri);
					File f = new File(filePath);
					Log.d(TAG, "f.length()="+f.length());
					if(f.length() > 55439782){
						DialogUtils.alert(thisContext, "", "동영상 사이즈가 너무 큽니다.");
					} else {
						new FileUploadTask().execute(filePath, cm1ID);
					}
				}
				
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("MovieUpload", "ResultCode="+resultCode);
		super.onActivityResult(requestCode, resultCode, data);
		
		
	    if (resultCode == RESULT_OK) {
	        if (requestCode == 1) {
	        	Log.d("AlbumUpload", "requestCode="+requestCode);
	        // currImageURI is the global variable I’m using to hold the content:
	            Uri currImageURI = data.getData();
	            String path = getRealPathFromURI(currImageURI);
				
				long id = ContentUris.parseId(currImageURI);
				//ContentResolver crThumb = thisContext.getContentResolver();
				//BitmapFactory.Options options=new BitmapFactory.Options();
				//options.inSampleSize = 1;
				//Bitmap curThumb = MediaStore.Video.Thumbnails.getThumbnail(crThumb, id, MediaStore.Video.Thumbnails.MICRO_KIND, options);
				//iv.setImageBitmap(ImageUtil.getRoundedCornerBitmap(curThumb, 12));
				//iv.setImageBitmap(curThumb);
	            
	            mediaStoreThumbnail.getThumbnail((int)id, currentImage, thisContext);
	            //currentImage.setImageURI(currImageURI);
	            
	            //currentImage.setImageURI(currImageURI);
                //currentImage.setImageBitmap(currImageBitmap);
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
 		
 		
 		@Override
 		protected Void doInBackground(String... args) {
 			
 			String filePath = args[0];
 			Log.d(TAG, "filePath="+filePath+" cm1ID="+cm1ID);
			File uploadFile = new File(filePath);
			fileName = StringUtils.getFileNameWithoutExt(filePath);
			if(!uploadFile.exists()){
				Toast.makeText(thisContext, "file not exist", Toast.LENGTH_SHORT);
				return null;
			}
			
			HttpClient httpClient = appEx.getHttpClient();
			HttpPost httpPost = new HttpPost(Constant.SERVER_ADDR+Constant.TRIBUTE_MOVIE_UPLOAD_URL);
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("cm1_movie", new FileBody(uploadFile));
			try {
				reqEntity.addPart("cm1_id", new StringBody(cm1ID,Charset.forName("UTF-8")));
				
				
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			httpPost.setEntity(reqEntity);
			
			try {
				HttpResponse response = httpClient.execute(httpPost);
				Log.d("Album Upload", response.toString());
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
 		
 		protected void onPostExecute(Void result) {
 			if(dialog != null){
 				dialog.dismiss();
 				dialog = null;
 			}
 			
 			DialogUtils.alert(thisContext, "", "전송이 완료되었습니다.");
 			//Intent intent = new Intent();
 			//intent.putExtra("fileName", fileName);
 			//((Activity)thisContext).setResult(RESULT_OK, intent);
 			//finish();
 			
 		}
 		
 		
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
	
}
