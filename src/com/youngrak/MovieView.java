package com.youngrak;

import com.youngrak.common.Constant;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

public class MovieView extends BaseActivity {
	
	@Override
    public void onCreate(Bundle icicle) {
    	
        super.onCreate(icicle);
        setContentView(R.layout.movie_view);
        
        String movieFile = getIntent().getStringExtra("movieFile");
        
        VideoView video = (VideoView)findViewById(R.id.movieView);
        
        MediaController mediaController = new MediaController(this);
        String movieUrl = Constant.VOD_SERVER + "/" + movieFile;
        Log.d("MovieView", movieUrl);
        Uri uri = Uri.parse(movieUrl);
        video.setVideoURI(uri);
        video.setMediaController(mediaController);
        video.requestFocus();
        video.start();
	}
}
