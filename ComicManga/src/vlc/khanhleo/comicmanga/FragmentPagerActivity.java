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

import java.util.ArrayList;

import vlc.khanhle.comicmanga.R;
import vlc.khanhleo.comicmanga.menu.SystemUiHider;
import vlc.khanhleo.comicmanga.utils.Consts;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
import android.widget.ImageView;

@SuppressLint("NewApi")
public class FragmentPagerActivity extends FragmentActivity {
	private int mNumberItem = 70;
	// private String mVolchap ;
	private String mChap;
	private String mVol;
	ArrayList<String> mListDataResult;

	MyAdapter mAdapter;

	ViewPager mPager;
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
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	public static SystemUiHider mSystemUiHider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mListDataResult = new ArrayList<String>();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			// mVol = extras.getString(Consts.VOL);
			// mChap = extras.getString(Consts.CHAP);
			// mNumberItem = extras.getInt(Consts.NUMBER_ITEM);
			mListDataResult = extras.getStringArrayList(Consts.NUMBER_ITEM);
			mVol = mListDataResult.get(0);
			mChap = mListDataResult.get(1);
			mNumberItem = Integer.parseInt(mListDataResult.get(2));
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
		// mSystemUiHider
		// .setOnVisibilityChangeListener(new
		// SystemUiHider.OnVisibilityChangeListener() {
		// // Cached values.
		// int mControlsHeight;
		// int mShortAnimTime;
		//
		// @Override
		// @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
		// public void onVisibilityChange(boolean visible) {
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
		// // If the ViewPropertyAnimator API is available
		// // (Honeycomb MR2 and later), use it to animate the
		// // in-layout UI controls at the bottom of the
		// // screen.
		// if (mControlsHeight == 0) {
		// mControlsHeight = controlsView.getHeight();
		// }
		// if (mShortAnimTime == 0) {
		// mShortAnimTime = getResources().getInteger(
		// android.R.integer.config_shortAnimTime);
		// }
		// controlsView
		// .animate()
		// .translationY(visible ? 0 : mControlsHeight)
		// .setDuration(mShortAnimTime);
		// } else {
		// // If the ViewPropertyAnimator APIs aren't
		// // available, simply show or hide the in-layout UI
		// // controls.
		// controlsView.setVisibility(visible ? View.VISIBLE
		// : View.GONE);
		// }
		//
		// if (visible && AUTO_HIDE) {
		// // Schedule a hide().
		// delayedHide(AUTO_HIDE_DELAY_MILLIS);
		// }
		// }
		// });
		// mPager click
		// Set up the user interaction to manually show or hide the system UI.
		// mPager.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View view) {
		// if (TOGGLE_ON_CLICK) {
		// mSystemUiHider.toggle();
		// } else {
		// mSystemUiHider.show();
		// }
		// }
		// });

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
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
			return ArrayListFragment.newInstance(position, mVol, mChap);
		}
	}

	public static class ArrayListFragment extends Fragment implements
			OnTouchListener {
		private int mNum;
		private String mVol;
		private String mChap;

		/**
		 * Create a new instance of CountingFragment, providing "num" as an
		 * argument.
		 */
		static ArrayListFragment newInstance(int mNum, String mVol, String mChap) {
			ArrayListFragment f = new ArrayListFragment();

			// Supply num input as an argument.
			Bundle args = new Bundle();
			args.putInt("num", mNum);
			args.putString("vol", mVol);
			args.putString("chap", mChap);
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
			Bitmap bmp = BitmapFactory.decodeFile(getPathImage(mNum));
			((ImageView) tv).setImageBitmap(bmp);
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
				Log.d(TAG, "mode=DRAG"); // write to LogCat
				mode = DRAG;

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
						int mControlsHeight = 0;
						int mShortAnimTime = 0;
						if (mControlsHeight == 0) {
							mControlsHeight = controlsView.getHeight();
						}
						if (mShortAnimTime == 0) {
							mShortAnimTime = getResources().getInteger(
									android.R.integer.config_shortAnimTime);
						}
						boolean visible = false;
						if (controlsView.getVisibility() == View.VISIBLE)
							visible = false;
						else
							visible = true;
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							// If the ViewPropertyAnimator API is available
							// (Honeycomb MR2 and later), use it to animate the
							// in-layout UI controls at the bottom of the
							// screen.

							controlsView
									.animate()
									.translationY(visible ? 0 : mControlsHeight)
									.setDuration(mShortAnimTime);
							controlsView.setVisibility(visible ? View.VISIBLE
									: View.GONE);
						} else {
							// If the ViewPropertyAnimator APIs aren't
							// available, simply show or hide the in-layout UI
							// controls.
							controlsView.setVisibility(visible ? View.VISIBLE
									: View.GONE);
						}

						if (visible && AUTO_HIDE) {
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

		Handler mHideHandler = new Handler();
		Runnable mHideRunnable = new Runnable() {
			@Override
			public void run() {
				mSystemUiHider.hide();
			}
		};

		/**
		 * Schedules a call to hide() in [delay] milliseconds, canceling any
		 * previously scheduled calls.
		 */
		private void delayedHide(int delayMillis) {
			mHideHandler.removeCallbacks(mHideRunnable);
			mHideHandler.postDelayed(mHideRunnable, delayMillis);
		}

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
}
