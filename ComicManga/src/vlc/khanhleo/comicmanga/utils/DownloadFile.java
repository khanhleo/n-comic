package vlc.khanhleo.comicmanga.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import vlc.khanhleo.comicmanga.ChapActivity;
import vlc.khanhleo.comicmanga.object.DownloadItem;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DownloadFile extends AsyncTask<DownloadItem, String, DownloadItem> {

	private ProgressBar mPr;
	private TextView mTv;
	private ChapActivity caller;
	private int mPosition;
	private LinearLayout mDownload;

	private String mFileToDel;

	public DownloadFile(ProgressBar mPr, TextView mTv, ChapActivity caller,
			int mPosition, LinearLayout mDownload) {
		super();
		this.mPr = mPr;
		this.mTv = mTv;
		this.caller = caller;
		this.mPosition = mPosition;
		this.mDownload = mDownload;
	}

	public LinearLayout getmDownload() {
		return mDownload;
	}

	public void setmDownload(LinearLayout mDownload) {
		this.mDownload = mDownload;
	}

	public int getmPosition() {
		return mPosition;
	}

	public void setmPosition(int mPosition) {
		this.mPosition = mPosition;
	}

	public ChapActivity getCaller() {
		return caller;
	}

	public void setCaller(ChapActivity caller) {
		this.caller = caller;
	}

	public ProgressBar getmPr() {
		return mPr;
	}

	public void setmPr(ProgressBar mPr) {
		this.mPr = mPr;
	}

	public TextView getmTv() {
		return mTv;
	}

	public void setmTv(TextView mTv) {
		this.mTv = mTv;
	}

	/**
	 * Before starting background thread Show Progress Bar Dialog
	 * */
	@Override
	protected void onPreExecute() {
		mTv.setText("0%");
		super.onPreExecute();
		// showDialog(progress_bar_type);
	}

	/**
	 * Downloading file in background thread
	 * */
	@Override
	protected DownloadItem doInBackground(DownloadItem... item) {
		int count;
		DownloadItem results;
		try {

			// File root = android.os.Environment.getExternalStorageDirectory();

			File dir = new File(Consts.getSdCardPath()
					+ item[0].getmFolderName());
			if (dir.exists() == false) {
				dir.mkdirs();
			}
			// URL url = new URL(DownloadUrl); // you can write here any
			// link
			File file = new File(dir, item[0].getmFileName());

			URL url = new URL(item[0].getmUrl());
			URLConnection conection = url.openConnection();
			conection.connect();
			// getting file length
			int lenghtOfFile = conection.getContentLength();

			// input stream to read file - with 8k buffer
			InputStream input = new BufferedInputStream(url.openStream(), 8192);

			// Output stream to write file
			OutputStream output = new FileOutputStream(file);

			if (!isCancelled()) {
				byte data[] = new byte[1024];

				long total = 0;

				while ((count = input.read(data)) != -1 && !isCancelled()) {
					total += count;
					// publishing the progress....
					// After this onProgressUpdate will be called
					publishProgress("" + (int) ((total * 100) / lenghtOfFile));
					Log.d("process", "current: "
							+ (int) ((total * 100) / lenghtOfFile));

					// writing data to file
					output.write(data, 0, count);
				}
			}

			// flushing output
			output.flush();

			// closing streams
			output.close();
			input.close();
			results = item[0];

		} catch (Exception e) {
			Log.e("Error: ", e.getMessage());
			results = null;
		}

		return results;
	}

	/**
	 * Updating progress bar
	 * */
	protected void onProgressUpdate(String... progress) {
		// setting progress percentage
		// pDialog.setProgress(Integer.parseInt(progress[0]));
		mPr.setProgress(Integer.parseInt(progress[0]));
		mTv.setText(Integer.parseInt(progress[0]) + "%");
	}

	/**
	 * After completing background task Dismiss the progress dialog
	 * **/
	@Override
	protected void onPostExecute(DownloadItem results) {
		// dismiss the dialog after the file was downloaded
		// dismissDialog(progress_bar_type);

		// caller.downloadFinish(mPosition);
		if (results != null) {
			// unzip
			// String pathName = "";
			// pathName = getSdCardPath() + "ComicManga/Vol1/";
			// String zipName = "vol1_chap1.zip";
			Log.d("Unzip", Consts.getSdCardPath() + results.getmFolderName()
					+ "/" + results.getmFileName());
			mFileToDel = Consts.getSdCardPath() + results.getmFolderName()
					+ "/" + results.getmFileName();
			Thread t1 = new Thread(new UnzipThread(Consts.getSdCardPath()
					+ results.getmFolderName() + "/", results.getmFileName(),
					caller));
			t1.start();
		} else {
			Toast.makeText(caller, "fail", Toast.LENGTH_SHORT).show();
			caller.downloadFinish(mPosition, mDownload,false);
		}
	}

	// show toast
	public void callBackHandler(final boolean isGood, final Context context) {
		((Activity) context).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (isGood) {
					caller.downloadFinish(mPosition, mDownload,true);
					// if has unzip success, delete file .zip
					File file = new File(mFileToDel);
					file.delete();
					Toast.makeText(context, "done", Toast.LENGTH_LONG).show();
				} else{
					caller.downloadFinish(mPosition, mDownload,false);
					Toast.makeText(context, "unZip fail!", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
	}

	private class UnzipThread extends Thread {
		private final String pathName;
		private final String zipName;
		private final ChapActivity c;

		public UnzipThread(String pathName, String zipName, ChapActivity c) {
			super();
			this.pathName = pathName;
			this.zipName = zipName;
			this.c = c;
		}

		@Override
		public void run() {
			boolean isGood = Unzip.unpackZip(pathName, zipName);
			callBackHandler(isGood, c);
		}
	}

}
