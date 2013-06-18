package com.youngrak;

import java.util.ArrayList;

import com.polites.android.GestureImageView;
import com.youngrak.common.DisplayUtil;
import com.youngrak.common.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class MapInfo extends BaseActivity implements OnTouchListener {
	
	private static String TAG = "MapInfo";
	
    
    //GestureImageView mapView;
	FrameLayout frameLayout;
	ImageView mapView;
    Button button1, button2, button3, button4, button5, button6, button7, button8, button9, button10;
    String mapInfo = "";
    int paddingTop = 0;
    int paddingLeft = 0;
    LinearLayout popup1;
    FullScrollView mapScrollView;
    ArrayList<LinearLayout> infoImageList;
	
	@Override
    public void onCreate(Bundle icicle) {
    	
        super.onCreate(icicle);
        setContentView(R.layout.map_info);
        
        new TopMenuInitializer(thisContext, (View)findViewById(R.id.top_menubar), "영락공원지도");
        
        
        frameLayout = (FrameLayout)findViewById(R.id.frameLayout);
        LayoutParams params = frameLayout.getLayoutParams();
        params.width = DisplayUtil.DPFromPixel(1653);
        params.height = DisplayUtil.DPFromPixel(1122);
        frameLayout.setLayoutParams(params);
        
        mapInfo = getIntent().getStringExtra("mapinfo");
        Log.d(TAG, "mapInfo = "+mapInfo);
        //mapView = (GestureImageView)findViewById(R.id.map1);
        mapView = (ImageView)findViewById(R.id.map1);
        //mapView.setOnTouchListener(this);
        mapView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hideInfoImage();
			}
		});
        
        mapScrollView = (FullScrollView)findViewById(R.id.mapScrollView);
        
        infoImageList = new ArrayList<LinearLayout>();
        infoImageList.add((LinearLayout)findViewById(R.id.popup1));
        infoImageList.add((LinearLayout)findViewById(R.id.popup2));
        infoImageList.add((LinearLayout)findViewById(R.id.popup3));
        infoImageList.add((LinearLayout)findViewById(R.id.popup4));
        infoImageList.add((LinearLayout)findViewById(R.id.popup5));
        infoImageList.add((LinearLayout)findViewById(R.id.popup6));
        infoImageList.add((LinearLayout)findViewById(R.id.popup7));
        infoImageList.add((LinearLayout)findViewById(R.id.popup8));
        infoImageList.add((LinearLayout)findViewById(R.id.popup9));
        infoImageList.add((LinearLayout)findViewById(R.id.popup10));
        
        
        button1 = (Button)findViewById(R.id.point1Button);
        button2 = (Button)findViewById(R.id.point2Button);
        button3 = (Button)findViewById(R.id.point3Button);
        button4 = (Button)findViewById(R.id.point4Button);
        button5 = (Button)findViewById(R.id.point5Button);
        button6 = (Button)findViewById(R.id.point6Button);
        button7 = (Button)findViewById(R.id.point7Button);
        button8 = (Button)findViewById(R.id.point8Button);
        button9 = (Button)findViewById(R.id.point9Button);
        button10 = (Button)findViewById(R.id.point10Button);
        
        button1.setOnClickListener(buttonListener);
        button2.setOnClickListener(buttonListener);
        button3.setOnClickListener(buttonListener);
        button4.setOnClickListener(buttonListener);
        button5.setOnClickListener(buttonListener);
        button6.setOnClickListener(buttonListener);
        button7.setOnClickListener(buttonListener);
        button8.setOnClickListener(buttonListener);
        button9.setOnClickListener(buttonListener);
        button10.setOnClickListener(buttonListener);
        
        savedMatrix.set(mapView.getImageMatrix());
        
        
	}
	
	View.OnClickListener buttonListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(v.getId() == button1.getId()){
				popupInfoImage(0);
			} else if(v.getId() == button2.getId()){
				popupInfoImage(1);
			} else if(v.getId() == button3.getId()){
				popupInfoImage(2);
			} else if(v.getId() == button4.getId()){
				popupInfoImage(3);
				/*
				//mapView.moveBy(1000, 1000);
				Log.d(TAG, "buttonListener button4 Click");
				//mapView.scrollTo(0, 0);
				
				//Matrix m = mapView.getImageMatrix();
				Matrix m = new Matrix();
				m.set(savedMatrix);
				m.postTranslate(-100, -100);
				mapView.setImageMatrix(m);
				*/
			} else if(v.getId() == button5.getId()){
				popupInfoImage(4);
			} else if(v.getId() == button6.getId()){
				popupInfoImage(5);
			} else if(v.getId() == button7.getId()){
				popupInfoImage(6);
			} else if(v.getId() == button8.getId()){
				popupInfoImage(7);
			} else if(v.getId() == button9.getId()){
				popupInfoImage(8);
			} else if(v.getId() == button10.getId()){
				popupInfoImage(9);
			}
			
		}
	};
	
	@Override
	protected void onResume(){
		super.onResume();
		
		
		if(!StringUtils.isEmptyOrBlank(mapInfo)){
			Log.d(TAG, "mapInfo Not null");
        	if(mapInfo.equals("http://www.cyberyoungrak.or.kr/map1")){//철쭉
        		Log.d(TAG, "mapInfo1");
        		/*
        		Matrix m = new Matrix();
        		m.set(savedMatrix);
        		m.postTranslate(-300, -400);
        		mapView.setImageMatrix(m);
        		*/
        		mapScrollView.post(new Runnable() { 
	        	        public void run() { 
	        	             mapScrollView.scrollTo(100, 120);
	        	        } 
	        	});
        		popupInfoImage(0);
        	} else if(mapInfo.equals("http://www.cyberyoungrak.or.kr/map2")){//관리사무소
        		Log.d(TAG, "mapInfo2");
        		
        		mapScrollView.post(new Runnable() { 
	        	        public void run() { 
	        	             mapScrollView.scrollTo(210, 0);
	        	        } 
	        	});
        		popupInfoImage(1);
        	} else if(mapInfo.equals("http://www.cyberyoungrak.or.kr/map3")){//6위용가족묘
        		Log.d(TAG, "mapInfo3");
        		mapScrollView.post(new Runnable() { 
	        	        public void run() { 
	        	             mapScrollView.scrollTo(340, 0);
	        	        } 
	        	});
        		popupInfoImage(2);
        	} else if(mapInfo.equals("http://www.cyberyoungrak.or.kr/map4")){//12위용가족묘
        		Log.d(TAG, "mapInfo4");
        		
        		mapScrollView.post(new Runnable() { 
	        	        public void run() { 
	        	             mapScrollView.scrollTo(670, 0);
	        	        } 
	        	});
        		popupInfoImage(3);
        	} else if(mapInfo.equals("http://www.cyberyoungrak.or.kr/map5")){//평장
        		Log.d(TAG, "mapInfo5");
        		
        		mapScrollView.post(new Runnable() { 
	        	        public void run() { 
	        	             mapScrollView.scrollTo(1000, 300);
	        	        } 
	        	});
        		popupInfoImage(4);
        	} else if(mapInfo.equals("http://www.cyberyoungrak.or.kr/map6")){//개나리
        		Log.d(TAG, "mapInfo6");
        		
        		mapScrollView.post(new Runnable() { 
	        	        public void run() { 
	        	             mapScrollView.scrollTo(1150, 400);
	        	        }
	        	});
        		popupInfoImage(5);
        	} else if(mapInfo.equals("http://www.cyberyoungrak.or.kr/map7")){//자연장 청마루
        		Log.d(TAG, "mapInfo7");
        		mapScrollView.post(new Runnable() { 
	        	        public void run() { 
	        	             mapScrollView.scrollTo(370, 0);
	        	        } 
	        	});
        		popupInfoImage(6);
        	} else if(mapInfo.equals("http://www.cyberyoungrak.or.kr/map8")){//승화원
        		Log.d(TAG, "mapInfo8");
        		mapScrollView.post(new Runnable() { 
	        	        public void run() { 
	        	             mapScrollView.scrollTo(100, 0);
	        	        } 
	        	});
        		popupInfoImage(7);
        	} else if(mapInfo.equals("http://www.cyberyoungrak.or.kr/map9")){//제1추모관(봉안당)
        		Log.d(TAG, "mapInfo9");
        		mapScrollView.post(new Runnable() { 
	        	        public void run() { 
	        	             mapScrollView.scrollTo(210, 0);
	        	        } 
	        	});
        		popupInfoImage(8);
        	} else if(mapInfo.equals("http://www.cyberyoungrak.or.kr/map10")){//제2추모관
        		Log.d(TAG, "mapInfo10");
        		mapScrollView.post(new Runnable() { 
	        	        public void run() { 
	        	             mapScrollView.scrollTo(400, 0);
	        	        } 
	        	});
        		popupInfoImage(9);
        	}
        }
	}
	
	
	
	private void popupInfoImage(int imageId){
		for(int i=0;i<infoImageList.size();i++){
			if(i==imageId){
				((LinearLayout)infoImageList.get(i)).setVisibility(View.VISIBLE);
			} else {
				((LinearLayout)infoImageList.get(i)).setVisibility(View.GONE);
			}
		}
	}
	
	private void hideInfoImage(){
		for(int i=0;i<infoImageList.size();i++){
			((LinearLayout)infoImageList.get(i)).setVisibility(View.GONE);
		}
	}
	
	private void showPopupImage(Context mcon){
		LayoutInflater inflater = (LayoutInflater) mcon.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View popup = inflater.inflate(R.layout.map_popup,null);
        Animation anim = AnimationUtils.loadAnimation(mcon, R.anim.popup_show);
        
        frameLayout.addView(popup);
        //popup.startAnimation(anim);
	}
	
	private PopupWindow showOptions(Context mcon){
	    try{ 
	        LayoutInflater inflater = (LayoutInflater) mcon.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	        View layout = inflater.inflate(R.layout.map_popup,null);
	        //layout.setAnimation(AnimationUtils.loadAnimation(this, R.anim.myanim));
	        PopupWindow optionspu = new PopupWindow(layout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

	        optionspu.setFocusable(true);
	        optionspu.showAtLocation(layout, Gravity.TOP, 0, 0);
	        optionspu.update(0, 0, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	        optionspu.setAnimationStyle(R.style.PopupAnimation);
	        return optionspu;
	    }
	    catch (Exception e){e.printStackTrace();
	    return null;}
	}
	
	
	//public boolean onTouch(View v, MotionEvent rawEvent) {
	//	return false;
	//}

	Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    
 // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
	public boolean onTouch(View v, MotionEvent rawEvent) {
		WrapMotionEvent event = WrapMotionEvent.wrap(rawEvent);
		// ...
		ImageView view = (ImageView) v;

		// Dump touch event to log
		dumpEvent(event);
		

		// Handle touch events here...
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			//savedMatrix.set(matrix);
			
			savedMatrix.set(mapView.getImageMatrix());
			start.set(event.getX(), event.getY());
			Log.d(TAG, "mode=DRAG");
			mode = DRAG;
			
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			
			oldDist = spacing(event);
			Log.d(TAG, "oldDist=" + oldDist);
			if (oldDist > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
				Log.d(TAG, "mode=ZOOM");
			}
			
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			Log.d(TAG, "mode=NONE");
			/*
			//left, top, right, bottom
			int moveLeft = (int)(event.getX() - start.x);
			int moveTop = (int)(event.getY() - start.y);
			//int left = (int)start.x;
			//int top = (int)start.y;
			paddingTop = buttonLayout.getPaddingTop() + moveTop;
			paddingLeft = buttonLayout.getPaddingLeft() + moveLeft;
							
			Log.d(TAG, "moveTop="+moveTop+" moveLeft="+moveLeft+", paddingTop="+paddingTop+", paddingLeft="+paddingLeft);
			buttonLayout.setPadding(paddingLeft, paddingTop, 0, 0);
			
			
			FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)popup1.getLayoutParams();
			paddingTop = params.topMargin + moveTop;
			paddingLeft = params.leftMargin + moveLeft;
			
			params.topMargin = paddingTop;
			params.leftMargin = paddingLeft;
			popup1.setLayoutParams(params);
			*/
			break;
		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) {
				
				//...
				matrix.set(savedMatrix);
				matrix.postTranslate(event.getX() - start.x,
                event.getY() - start.y);
				
				
				
			}
			else if (mode == ZOOM) {
				/*
				float newDist = spacing(event);
				Log.d(TAG, "newDist=" + newDist);
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					float scale = newDist / oldDist;
					matrix.postScale(scale, scale, mid.x, mid.y);
				}
				*/
			}
			break;
		}

		view.setImageMatrix(matrix);
		return true; // indicate event was handled
	}

	// Show an event in the LogCat view, for debugging
	private void dumpEvent(WrapMotionEvent event) {
	      // ...
		String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
	            "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
		StringBuilder sb = new StringBuilder();
		int action = event.getAction();
	    int actionCode = action & MotionEvent.ACTION_MASK;
	    sb.append("event ACTION_").append(names[actionCode]);
	    if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) {
	         sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
	         sb.append(")");
	    }
	    sb.append("[");
	    for (int i = 0; i < event.getPointerCount(); i++) {
	    	sb.append("#").append(i);
	        sb.append("(pid ").append(event.getPointerId(i));
	        sb.append(")=").append((int) event.getX(i));
	        sb.append(",").append((int) event.getY(i));
	        if (i + 1 < event.getPointerCount())
	        	sb.append(";");
	    }
	    sb.append("]");
	    Log.d(TAG, sb.toString());
	}

	// Determine the space between the first two fingers
	private float spacing(WrapMotionEvent event) {
		// ...
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	// Calculate the mid point of the first two fingers
	private void midPoint(PointF point, WrapMotionEvent event) {
		// ...
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}
}
