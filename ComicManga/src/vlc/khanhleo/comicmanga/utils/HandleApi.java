package vlc.khanhleo.comicmanga.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import vlc.khanhleo.comicmanga.ChapActivity;
import vlc.khanhleo.comicmanga.object.DownloadItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class HandleApi extends AsyncTask<String, Void, DownloadItem> {
//	private ProgressDialog dialog;
	private ChapActivity caller;

	private ProgressBar mPr;
	private TextView mTv;
	private LinearLayout mDownload;
	private int mPosition;
	private ProgressBar mPrCheckApi;
	
	public HandleApi(ChapActivity caller, ProgressBar mPr, TextView mTv, int mPosition, LinearLayout mDowLayout, ProgressBar mPrCheckApi) {
//		dialog = new ProgressDialog(caller);
		this.caller = caller;
		this.mPr = mPr;
		this.mTv = mTv;
		this.mPosition = mPosition;
		this.mDownload = mDowLayout;
		this.mPrCheckApi = mPrCheckApi;
	}

	@Override
	protected void onPreExecute() {
//		dialog.show();
		mPrCheckApi.setVisibility(View.VISIBLE);
	}

	@Override
	protected DownloadItem doInBackground(String... url) {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpPost = new HttpGet(url[0]);
		DownloadItem di = new DownloadItem();
		try {
			HttpResponse response = client.execute(httpPost);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				String strJson = builder.toString();
				;
				if (strJson != null) {
					try {
						JSONObject json = new JSONObject(strJson);
						JSONObject data = json.getJSONObject("data");
//						di.setmFileName(data.getString("name"));
						di.setmUrl(Consts.URL +data.getString("content"));
						di.setmFileName(data.getString("file_name"));
						return di;
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return null;
					}
				} else {
					return null;
				}
			} else {
				Log.e("http response:", "Failed to download file");
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return di;
	}

	@Override
	protected void onPostExecute(DownloadItem result) {
//		if (dialog.isShowing())
//			dialog.dismiss();
		if(mPrCheckApi.getVisibility()==View.VISIBLE)
			mPrCheckApi.setVisibility(View.INVISIBLE);
		caller.resultData(result,mPosition,this.mPr,this.mTv,this.mDownload);
		super.onPostExecute(result);
	}

}
