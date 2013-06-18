package com.youngrak;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.youngrak.common.Constant;
import com.youngrak.common.HttpDataHelper;
import com.youngrak.common.dialog.DialogUtils;
import com.youngrak.model.Tomb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;

public class UserSearch extends BaseActivity implements RadioGroup.OnCheckedChangeListener ,OnEditorActionListener {

	static final String TAG = "UserSearch";
	ImageButton searchButton;
	EditText searchText;
	String areaType;
	String searchUserName;
	String mMessage = "";
	ArrayList<Tomb> mListData = null;
	ProgressBar pageLoading = null;
	TombAdapter adapter = null;
	ListView mListView = null;
	
	@Override
    public void onCreate(Bundle icicle) {
    	
        super.onCreate(icicle);
        setContentView(R.layout.user_search);
        
        new TopMenuInitializer(thisContext, (View)findViewById(R.id.top_menubar), "고인검색");
        
        areaType = "2";
        pageLoading = (ProgressBar)findViewById(R.id.pageLoading);
        mListView = (ListView)findViewById(R.id.listview);
        RadioGroup areaTypeGroup = (RadioGroup)findViewById(R.id.areaTypeGroup);
        areaTypeGroup.setOnCheckedChangeListener(this);
        searchText = (EditText)findViewById(R.id.searchText);
        searchText.setOnEditorActionListener(this);
        searchButton = (ImageButton)findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new PageLoadTask().execute();
			}
		});
        
        mListView.setOnItemClickListener(new OnItemClickListener(){
        	@Override
			public void onItemClick(AdapterView<?> args0, View view, int position, long id) {
        		Intent i = new Intent(thisContext, UserInfo.class);
        		Tomb t = mListData.get(position);
        		i.putExtra("tomb", t);
        		startActivity(i);
        	}
        });
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		
		if(checkedId == R.id.areaType1){
			areaType = "1";
		} else if(checkedId == R.id.areaType2){
			areaType = "2";
		}
		
	}
	
	private Runnable showDialog = new Runnable() {
		@Override
		public void run(){
			DialogUtils.alert(thisContext, "", mMessage);
		}
	};
	
	private void requestSearchUser(){
		
		try{
			
			ApplicationEx app = (ApplicationEx)this.getApplication();
			String url = Constant.USER_SEARCH_URL + "?area_type="+areaType+"&dead_name="+searchText.getText().toString();
			String result = HttpDataHelper.getHttpJsonData(app.getHttpClient(), url, "GET", null, -1);
			
			Log.d(TAG, result);
    		
			
    		JSONArray data = new JSONArray(result);
    		
    		mListData = new ArrayList<Tomb>();
    		
    		for(int i=0;i<data.length();i++){
    			JSONObject o = (JSONObject)data.get(i);
    			mListData.add(Tomb.bindJSONObject(o));
    		}
    		
    		
		} catch(JSONException e) {
        	e.printStackTrace();
        	Log.e(TAG, e.getMessage());
        	//DialogUtils.alert(thisContext, "", e.getMessage());
        	mMessage = "JSON 오류가 발생했습니다";
        	thisHandler.post(showDialog);
        } catch(ClientProtocolException e) {
        	e.printStackTrace();
    		Log.e(TAG, e.getMessage());
    		//DialogUtils.alert(thisContext, "", "서버 접속에 실패했습니다");
    		mMessage = "서버 접속에 실패했습니다.";
    		thisHandler.post(showDialog);
    	} catch(IOException e) {
    		e.printStackTrace();
    		Log.e(TAG, e.getMessage());
    		//DialogUtils.alert(thisContext, "", "서버에 연결할 수 없습니다");
    		mMessage = "서버에 연결할 수 없습니다.";
    		thisHandler.post(showDialog);
    	} catch(Exception e) {
    		e.printStackTrace();
    		//Log.e(TAG, e.getMessage());
    		//DialogUtils.alert(MyLecture.this, "", e.getMessage());
    	}
	}
	
	// AsyncTask클래스는 항상 Subclassing 해서 사용 해야 함.
 	// 사용 자료형은
 	// background 작업에 사용할 data의 자료형: String 형
 	// background 작업 진행 표시를 위해 사용할 인자: Integer형
 	// background 작업의 결과를 표현할 자료형: Long
 	private class PageLoadTask extends AsyncTask<String, Integer, Integer> {
 		
 		// 이곳에 포함된 code는 AsyncTask가 execute 되자 마자 UI 스레드에서 실행됨.
 		// 작업 시작을 UI에 표현하거나
 		// background 작업을 위한 ProgressBar를 보여 주는 등의 코드를 작성.
 		protected void onPreExecute(){
 			super.onPreExecute();
 			
 			if(pageLoading != null)
 				pageLoading.setVisibility(View.VISIBLE);
 		}
 		
 		// UI 스레드에서 AsynchTask객체.execute(...) 명령으로 실행되는 callback
 		@Override
 		protected Integer doInBackground(String... args) {

 			requestSearchUser();
 			return 0;
 		}
 		
 		// onInBackground(...)가 완료되면 자동으로 실행되는 callback
 		// 이곳에서 onInBackground가 리턴한 정보를 UI위젯에 표시 하는 등의 작업을 수행함.
 		// (예제에서는 작업에 걸린 총 시간을 UI위젯 중 TextView에 표시함)
 		protected void onPostExecute(Integer result) {
 			if(pageLoading != null)
 				pageLoading.setVisibility(View.GONE);
 			
 			searchText.setText("");
 			
 			if(mListData != null) {
 				Log.d(TAG, "mInterviewData not null length = "+mListData.size());
 	        	adapter = new TombAdapter(thisContext, R.layout.user_row, mListData);
 	        
 	        	mListView.setAdapter(adapter);

 	    	}
    		
 		}
 		
 		
 		// AsyncTask.cancel(boolean) 메소드가 true 인자로
 		// 실행되면 호출되는 콜백.
 		// background 작업이 취소될때 꼭 해야될 작업은  여기에 구현.
 		@Override
 		protected void onCancelled() {
 			// TODO Auto-generated method stub
 			super.onCancelled();
 			
 			if(pageLoading != null)
 				pageLoading.setVisibility(View.GONE);
 		}
 	
 	}
 	
 	private class TombAdapter extends ArrayAdapter<Tomb> {
 		
 		private Context context;
 		private List<Tomb> itemList;
 		private int viewId;
 		
 		public TombAdapter(Context context, int textViewResourceId, List<Tomb> itemList) {
 			super(context, textViewResourceId, itemList);
 			this.itemList = itemList;
 			this.context = context;
 			viewId=textViewResourceId;
 		}
 		
 		@Override
 		public View getView(int position, View convertView, ViewGroup parent) {
 			
 			View v = convertView;
 			
 			if(v == null){
 				LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 				v = vi.inflate(viewId, null);
 			}
 			
 			if(itemList != null){
 				
 				Log.d(TAG, "itemList != null");
 				
 				final Tomb item = itemList.get(position);
 				TextView itemName = (TextView) v.findViewById(R.id.userName);
 				TextView deadDate = (TextView) v.findViewById(R.id.deadDate);
 				TextView deadSex = (TextView) v.findViewById(R.id.deadSex);
 				TextView burySec = (TextView) v.findViewById(R.id.burySec);
 				TextView buryNo = (TextView) v.findViewById(R.id.buryNo);
 				
 				if(itemName != null){
 					itemName.setText(item.getDeadName());
 				}
 				deadDate.setText("사망일자:"+item.getDeadDate());
 				deadSex.setText("성별:"+item.getDeadSex());
 				buryNo.setText(item.getBuryNo());
 				
 			}
 			return v;
 		}
 	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// TODO Auto-generated method stub
		if((actionId == EditorInfo.IME_ACTION_DONE) || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)){
    		
    		Log.d(TAG, Integer.toString(v.getId()));
    		
			new PageLoadTask().execute();
    	}
    	
    	
		return false;
	}
}
