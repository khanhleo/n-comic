/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package vlc.khanhleo.comicmanga;

import java.io.File;
import java.util.ArrayList;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import vlc.khanhle.comicmanga.R;
import vlc.khanhleo.comicmanga.menu.SystemUiHider;
import vlc.khanhleo.comicmanga.utils.Consts;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.SettingNotFoundException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.FloatMath;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

@SuppressLint("NewApi")
public class FragmentPagerActivity extends FragmentActivity implements
		OnClickListener {
	private int mNumberItem = 70;
	// private String mVolchap ;
	private String mChap;
	private String mVol;
	ArrayList<String> mListDataResult, mListFilePath;

	private View mBtnNext, mBtnPrevious, mBtnSetting, mSearch;

	MyAdapter mAdapter;

	ViewPager mPager;
	private int mNextPageIndex = 1, mPreviousPageIndex;
	private static View controlsView;
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	public static SystemUiHider mSystemUiHider;

	private static boolean mVisible = false;
	
	/** The view to show the ad. */
	private AdView adView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mListDataResult = new ArrayList<String>();
		mListFilePath = new ArrayList<String>();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			// mVol = extras.getString(Consts.VOL);
			// mChap = extras.getString(Consts.CHAP);
			// mNumberItem = extras.getInt(Consts.NUMBER_ITEM);
			mListDataResult = extras.getStringArrayList(Consts.NUMBER_ITEM);
			mVol = mListDataResult.get(0);
			mChap = mListDataResult.get(1);
			mNumberItem = Integer.parseInt(mListDataResult.get(2));
			mListFilePath = getPathIamgeByList(mVol, mChap);
		}

		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.fragment_pager);

		mAdapter = new MyAdapter(getSupportFragmentManager());

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		// menu
		controlsView = findViewById(R.id.fullscreen_content_controls);
		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, mPager, HIDER_FLAGS);
		mSystemUiHider.setup();

		mBtnNext = findViewById(R.id.btn_next);
		mBtnPrevious = findViewById(R.id.btn_previous);
		mBtnSetting = findViewById(R.id.btn_setting);
		mSearch = findViewById(R.id.btn_search);
		// mTxtGoto = (EditText) findViewById(R.id.txt_go_to);

		mBtnNext.setOnClickListener(this);
		mBtnPrevious.setOnClickListener(this);
		mBtnSetting.setOnClickListener(this);
		mSearch.setOnClickListener(this);

		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				if (arg0 == mNumberItem) {
					mBtnNext.setEnabled(false);
				} else if (arg0 == 0) {
					mBtnPrevious.setEnabled(false);
				} else {
					mBtnNext.setEnabled(true);
					mBtnPrevious.setEnabled(true);
				}
				mNextPageIndex = arg0 + 1;
				mPreviousPageIndex = arg0 - 1;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		
		setupAdmob();
	}
	
	private void setupAdmob(){
		if (Consts.isNetworkOnline(getApplicationContext())) {

			// Create an ad.
			adView = new AdView(this);
			adView.setAdSize(AdSize.BANNER);
			adView.setAdUnitId(Consts.AD_UNIT_ID);

			// Add the AdView to the view hierarchy. The view will have no size
			// until the ad is loaded.
			LinearLayout layout = (LinearLayout) findViewById(R.id.llAdmobs);
			layout.addView(adView);

			// Create an ad request. Check logcat output for the hashed device
			// ID to
			// get test ads on a physical device.
			AdRequest adRequest = new AdRequest.Builder().build();

			// Start loading the ad in the background.
			adView.loadAd(adRequest);
		} else {
			LinearLayout layout = (LinearLayout) findViewById(R.id.llBottom);
			layout.setVisibility(View.GONE);
		}
	}

	// get list file path
	private ArrayList<String> getPathIamgeByList(String mVol, String mChap){
		File dir = new File(Consts.getSdCardPath() + mVol + "/"
				+ mChap);
		ArrayList<String> list = new ArrayList<String>();
		if (dir.exists() != false) {
			if (dir.listFiles().length > 2) {
				for (File itemFile : dir.listFiles()) {
					list.add(itemFile.getAbsolutePath());
				}
			}
		}
		
		return list;
	}
	
	// button on setting panel click
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_next:
			mPager.setCurrentItem(mNextPageIndex);
			break;
		case R.id.btn_previous:
			mPager.setCurrentItem(mPreviousPageIndex);
			break;
		case R.id.btn_setting:
			showBrightnessDialog(this);
			break;
		case R.id.btn_search:
			showGoToDialog(this);
			break;
		default:
			break;
		}
		delayedHide(AUTO_HIDE_DELAY_MILLIS);
	}

	// go to dialog
	public void showGoToDialog(final Activity activity) {
		final Dialog dialog = new Dialog(activity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent); 
		dialog.setContentView(R.layout.dialog_go_to);
		final EditText txtInput = (EditText) dialog
				.findViewById(R.id.txt_input);
		Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String strCheck = txtInput.getText().toString();
				if (!strCheck.equals("")) {
					int number = Integer
							.parseInt(txtInput.getText().toString());
					getNumberIndex(number);
					dialog.dismiss();
				}else{
					Toast.makeText(getApplicationContext(), activity.getString(R.string.warning_string_empty),
							Toast.LENGTH_LONG).show();
				}
			}
		});

		dialog.setCancelable(true);
		dialog.show();

	}

	// brightness dialog
	public void showBrightnessDialog(final Activity activity) {

		final Dialog dialog = new Dialog(activity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent); 
		dialog.setContentView(R.layout.dialog_brightness);
		final SeekBar sbBrightness = (SeekBar) dialog
				.findViewById(R.id.sb_brightness);
		sbBrightness.setMax(255);

		   float curBrightnessValue = 0;
		  try {
		   curBrightnessValue = android.provider.Settings.System.getInt(
		     getContentResolver(),
		     android.provider.Settings.System.SCREEN_BRIGHTNESS);
		  } catch (SettingNotFoundException e) {
		   e.printStackTrace();
		  }

		   int screen_brightness = (int) curBrightnessValue;
		   sbBrightness.setProgress(screen_brightness);
		  sbBrightness.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
		   int progress = 0;

		    @Override
		   public void onProgressChanged(SeekBar seekBar, int progresValue,
		     boolean fromUser) {
		    progress = progresValue;
		   }

		    @Override
		   public void onStartTrackingTouch(SeekBar seekBar) {
		    // Do something here,
		    // if you want to do anything at the start of
		    // touching the seekbar
		   }

		    @Override
		   public void onStopTrackingTouch(SeekBar seekBar) {
		    android.provider.Settings.System.putInt(getContentResolver(),
		      android.provider.Settings.System.SCREEN_BRIGHTNESS,
		      progress);
		   }
		  });
		Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.setCancelable(true);
		dialog.show();

	}
	
	// result data
	public void getNumberIndex(int number) {
//		Toast.makeText(getApplicationContext(), String.valueOf(number),
//				Toast.LENGTH_SHORT).show();
		 mPager.setCurrentItem(number);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	static Handler mHideHandler = new Handler();
	static Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			// mSystemUiHider.hide();

			hidenShowMenu();
			// controlsView.setVisibility(View.GONE);
		}
	};

	private static void hidenShowMenu(){
		int mControlsHeight = 0;
		int mShortAnimTime = 200;
		if (mControlsHeight == 0) {
			mControlsHeight = controlsView.getHeight();
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			// If the ViewPropertyAnimator API is available
			// (Honeycomb MR2 and later), use it to animate the
			// in-layout UI controls at the bottom of the
			// screen.

			controlsView.animate()
					.translationY(mVisible ? 0 : mControlsHeight)
					.setDuration(mShortAnimTime);
			// controlsView.setVisibility(visible ? View.VISIBLE
			// : View.GONE);
		} else {
			// If the ViewPropertyAnimator APIs aren't
			// available, simply show or hide the in-layout UI
			// controls.
			controlsView.setVisibility(mVisible ? View.VISIBLE : View.GONE);
		}
		mVisible = !mVisible;
	}
	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	public static void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}

	public class MyAdapter extends FragmentPagerAdapter {
		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return mNumberItem;
		}

		@Override
		public Fragment getItem(int position) {
			return ArrayListFragment.newInstance(position, mVol, mChap, mListFilePath);
		}
	}

	public static class ArrayListFragment extends Fragment implements
			OnTouchListener {
		private int mNum;
		private String mVol;
		private String mChap;
		private ArrayList<String> mFilePath;

		/**
		 * Create a new instance of CountingFragment, providing "num" as an
		 * argument.
		 */
		static ArrayListFragment newInstance(int mNum, String mVol, String mChap, ArrayList<String> mListFilePath) {
			ArrayListFragment f = new ArrayListFragment();

			// Supply num input as an argument.
			Bundle args = new Bundle();
			args.putInt("num", mNum);
			args.putString("vol", mVol);
			args.putString("chap", mChap);
			args.putStringArrayList("file_path", mListFilePath);
			f.setArguments(args);

			return f;
		}

		/**
		 * When creating, retrieve this instance's number from its arguments.
		 */
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mNum = getArguments() != null ? getArguments().getInt("num") : 1;
			mVol = getArguments() != null ? getArguments().getString("vol")
					: "vol01";
			mChap = getArguments() != null ? getArguments().getString("chap")
					: "chap1";
			mFilePath = getArguments()!=null? getArguments().getStringArrayList("file_path"):null;
		}

		/**
		 * The Fragment's UI is just a simple text view showing its instance
		 * number.
		 */
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_pager_list, container,
					false);
			View tv = v.findViewById(R.id.ivContent);
//			Bitmap bmp = BitmapFactory.decodeFile(getPathImage(mNum));
			Bitmap bmp = BitmapFactory.decodeFile(mFilePath.get(mNum));
			((ImageView) tv).setImageBitmap(bmp);
			mMatrixInit = ((ImageView) tv).getImageMatrix();
			tv.setOnTouchListener((OnTouchListener) this);
			return v;
		}

		private String getPathImage(int mNum2) {
			String pathName = "";
			String fileName = "";
			if (mNum2 < 9)
				fileName = mVol + "_" + mChap + "_0"
						+ String.valueOf(mNum2 + 1) + ".jpg";
			else
				fileName = mVol + "_" + mChap + "_" + String.valueOf(mNum2 + 1)
						+ ".jpg";
			pathName = Consts.getSdCardPath() + "/" + mVol + "/" + mChap + "/"
					+ fileName;
			return pathName;
		}
		
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			// setListAdapter(new ArrayAdapter<String>(getActivity(),
			// android.R.layout.simple_list_item_1, Cheeses.sCheeseStrings));
		}

		// zoom

		private static final String TAG = "Touch";
		@SuppressWarnings("unused")
		private static final float MIN_ZOOM = 1f, MAX_ZOOM = 1f;

		private static Matrix mMatrixInit;
		// These matrices will be used to scale points of the image
		Matrix matrix = new Matrix();
		Matrix savedMatrix = new Matrix();

		// The 3 states (events) which the user is trying to perform
		static final int NONE = 0;
		static final int DRAG = 1;
		static final int ZOOM = 2;
		int mode = NONE;

		// these PointF objects are used to record the point(s) the user is
		// touching
		PointF start = new PointF();
		PointF mid = new PointF();
		float oldDist = 1f;
		boolean firstTouch = false;
		int clickCount = 0;
		/* variable for storing the time of first click */
		long startTime;
		/* variable for calculating the total time */
		long duration;
		static int ONE_SECOND = 300;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			ImageView view = (ImageView) v;
			view.setScaleType(ImageView.ScaleType.MATRIX);
			float scale;

			dumpEvent(event);
			// Handle touch events here...

			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN: // first finger down only
				matrix.set(view.getImageMatrix());
				savedMatrix.set(matrix);
				start.set(event.getX(), event.getY());
				if (!matrix.equals(mMatrixInit)) {
					Log.d(TAG, "mode=DRAG"); // write to LogCat
					mode = DRAG;
				} else
					mode = NONE;

				// double tap to show menu
				// if (firstTouch
				// && (System.currentTimeMillis() - time) <= 300) {
				// // do stuff here for double tap
				// Log.e("** DOUBLE TAP**", " second tap ");
				// if (TOGGLE_ON_CLICK) {
				// mSystemUiHider.toggle();
				// } else {
				// mSystemUiHider.show();
				// }
				// firstTouch = false;
				//
				// } else {
				// firstTouch = true;
				// time = System.currentTimeMillis();
				// Log.e("** SINGLE  TAP**", " First Tap time  " + time);
				// return false;
				// }
				break;

			case MotionEvent.ACTION_UP: // first finger lifted
				clickCount++;

				if (clickCount == 1) {
					startTime = System.currentTimeMillis();
				}

				else if (clickCount == 2) {
					long duration = System.currentTimeMillis() - startTime;
					if (duration <= ONE_SECOND) {
//						int mControlsHeight = 0;
//						int mShortAnimTime = 0;
//						if (mControlsHeight == 0) {
//							mControlsHeight = controlsView.getHeight();
//						}
//						if (mShortAnimTime == 0) {
//							mShortAnimTime = getResources().getInteger(
//									android.R.integer.config_shortAnimTime);
//						}
//						// boolean visible = false;
//						// if (controlsView.getVisibility() == View.VISIBLE)
//						// visible = false;
//						// else
//						// visible = true;
//						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//							// If the ViewPropertyAnimator API is available
//							// (Honeycomb MR2 and later), use it to animate the
//							// in-layout UI controls at the bottom of the
//							// screen.
//
//							controlsView
//									.animate()
//									.translationY(
//											mVisible ? 0 : mControlsHeight)
//									.setDuration(mShortAnimTime);
//							// controlsView.setVisibility(visible ? View.VISIBLE
//							// : View.GONE);
//							mVisible = !mVisible;
//						} else {
//							// If the ViewPropertyAnimator APIs aren't
//							// available, simply show or hide the in-layout UI
//							// controls.
//							controlsView.setVisibility(mVisible ? View.VISIBLE
//									: View.GONE);
//							mVisible = !mVisible;
//						}
						hidenShowMenu();
						if (!mVisible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
						clickCount = 0;
						duration = 0;

					} else {
						clickCount = 1;
						startTime = System.currentTimeMillis();
					}
				}
				Log.d("Action Up:", "up");
			case MotionEvent.ACTION_POINTER_UP: // second finger lifted

				mode = NONE;
				Log.d(TAG, "mode=NONE");
				break;

			case MotionEvent.ACTION_POINTER_DOWN: // first and second finger
													// down

				oldDist = spacing(event);
				Log.d(TAG, "oldDist=" + oldDist);
				if (oldDist > 5f) {
					savedMatrix.set(matrix);
					midPoint(mid, event);
					mode = ZOOM;
					Log.d(TAG, "mode=ZOOM");
				}
				break;

			case MotionEvent.ACTION_MOVE:
				if (mode == DRAG) {
					matrix.set(savedMatrix);
					matrix.postTranslate(event.getX() - start.x, event.getY()
							- start.y); // create the transformation in the
										// matrix of points
				} else if (mode == ZOOM) {
					// pinch zooming
					float newDist = spacing(event);
					Log.d(TAG, "newDist=" + newDist);
					if (newDist > 5f) {
						matrix.set(savedMatrix);
						scale = newDist / oldDist; // setting the scaling of the
													// matrix...if scale > 1
													// means
													// zoom in...if scale < 1
													// means
													// zoom out
						matrix.postScale(scale, scale, mid.x, mid.y);
					}
				}
				break;
			}

			view.setImageMatrix(matrix); // display the transformation on screen

			return true; // indicate event was handled
		}

		// Handler mHideHandler = new Handler();
		// Runnable mHideRunnable = new Runnable() {
		// @Override
		// public void run() {
		// mSystemUiHider.hide();
		// }
		// };
		//
		// /**
		// * Schedules a call to hide() in [delay] milliseconds, canceling any
		// * previously scheduled calls.
		// */
		// private void delayedHide(int delayMillis) {
		// mHideHandler.removeCallbacks(mHideRunnable);
		// mHideHandler.postDelayed(mHideRunnable, delayMillis);
		// }

		/*
		 * ----------------------------------------------------------------------
		 * ---- Method: spacing Parameters: MotionEvent Returns: float
		 * Description: checks the spacing between the two fingers on touch
		 * ----------------------------------------------------
		 */

		private float spacing(MotionEvent event) {
			float x = event.getX(0) - event.getX(1);
			float y = event.getY(0) - event.getY(1);
			return FloatMath.sqrt(x * x + y * y);
		}

		/*
		 * ----------------------------------------------------------------------
		 * ---- Method: midPoint Parameters: PointF object, MotionEvent Returns:
		 * void Description: calculates the midpoint between the two fingers
		 * ------------------------------------------------------------
		 */

		private void midPoint(PointF point, MotionEvent event) {
			float x = event.getX(0) + event.getX(1);
			float y = event.getY(0) + event.getY(1);
			point.set(x / 2, y / 2);
		}

		/** Show an event in the LogCat view, for debugging */
		private void dumpEvent(MotionEvent event) {
			String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
					"POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
			StringBuilder sb = new StringBuilder();
			int action = event.getAction();
			int actionCode = action & MotionEvent.ACTION_MASK;
			sb.append("event ACTION_").append(names[actionCode]);

			if (actionCode == MotionEvent.ACTION_POINTER_DOWN
					|| actionCode == MotionEvent.ACTION_POINTER_UP) {
				sb.append("(pid ").append(
						action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
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
			Log.d("Touch Events ---------", sb.toString());
		}

	}
	@Override
	public void onBackPressed() {
		if (mVisible && AUTO_HIDE) {
			mVisible= !mVisible;
		}
		finish();
	}
}
