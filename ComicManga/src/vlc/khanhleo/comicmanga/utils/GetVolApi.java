package vlc.khanhleo.comicmanga.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import vlc.khanhle.comicmanga.R;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

public class GetVolApi extends AsyncTask<String, Void, String> {
	private ProgressDialog dialog;
	private Context mContext;

	@Override
	protected void onPreExecute() {
		dialog.show();
	}

	public GetVolApi(Context mContext) {
		super();
		dialog = new ProgressDialog(mContext);
		this.mContext = mContext;
	}

	@Override
	protected String doInBackground(String... url) {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpPost = new HttpGet(url[0]);
//		DownloadItem di = new DownloadItem();
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
						String result = json.getString("count");
//						JSONObject data = json.getJSONObject("data");
//						di.setmFileName(data.getString("name"));
//						di.setmUrl(Consts.URL +data.getString("content"));
//						di.setmFileName(data.getString("file_name"));
						return result;
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return null;
					}
				} else {
					return null;
				}
			} else {
				Log.e("http response:", "Failed to get data");
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		if (dialog.isShowing())
			dialog.dismiss();
		if (result!=null && !result.equals("")){
			String text = mContext.getString(R.string.vol_count, result,result);
			new AlertDialog.Builder(mContext)
			.setMessage(text)
			.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int id) {
							// if this button is
							// clicked, close
							dialog.cancel();
						}
					}).show();
		}
//		caller.resultData(result,mPosition,this.mPr,this.mTv,this.mDownload);
		super.onPostExecute(result);
	}

}