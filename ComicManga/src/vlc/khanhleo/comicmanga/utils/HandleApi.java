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

import vlc.khanhleo.comicmanga.object.DownloadItem;

import android.os.AsyncTask;
import android.util.Log;

public class HandleApi  extends AsyncTask<String, Void, DownloadItem> {
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
					String strJson = builder.toString();;
					if (strJson != null) {
						try {
							JSONObject json = new JSONObject(strJson);
							JSONObject data = json.getJSONObject("data");
							di.setmFileName(data.getString("name"));
							di.setmFolderName(data.getString("wp_content"));
							di.setmUrl(data.getString("content"));
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
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}


//	public static String getApiData(String url) {
//		StringBuilder builder = new StringBuilder();
//		HttpClient client = new DefaultHttpClient();
//		HttpPost httpPost = new HttpPost(url);
//		try {
//			HttpResponse response = client.execute(httpPost);
//			StatusLine statusLine = response.getStatusLine();
//			int statusCode = statusLine.getStatusCode();
//			if (statusCode == 200) {
//				HttpEntity entity = response.getEntity();
//				InputStream content = entity.getContent();
//				BufferedReader reader = new BufferedReader(
//						new InputStreamReader(content));
//				String line;
//				while ((line = reader.readLine()) != null) {
//					builder.append(line);
//				}
//			} else {
//				Log.e("http response:", "Failed to download file");
//			}
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return builder.toString();
//	}

//	public static DownloadItem handleApiData(String url) {
//		try {
//			TheTask tt = null;
//			String strJson = tt.execute(url).get();
//			if (strJson != null) {
//				DownloadItem di = new DownloadItem();
//				try {
//					JSONObject json = new JSONObject(strJson);
//					JSONObject data = json.getJSONObject("data");
//					di.setmFileName(data.getString("name"));
//					di.setmFolderName(data.getString("wp_content"));
//					di.setmUrl(data.getString("content"));
//					return di;
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					return null;
//				}
//			} else {
//				return null;
//			}
//		} catch (InterruptedException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (ExecutionException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		// String strJson = getApiData(url);
//		return null;
//
//	}
}
