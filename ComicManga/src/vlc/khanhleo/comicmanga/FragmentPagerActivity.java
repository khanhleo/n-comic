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

import vlc.khanhle.comicmanga.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
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
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class FragmentPagerActivity extends FragmentActivity  {
	static final int NUM_ITEMS = 45;

	MyAdapter mAdapter;

	ViewPager mPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.fragment_pager);

		mAdapter = new MyAdapter(getSupportFragmentManager());

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		// Watch for button clicks.
		// Button button = (Button)findViewById(R.id.goto_first);
		// button.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// mPager.setCurrentItem(0);
		// }
		// });
		// button = (Button)findViewById(R.id.goto_last);
		// button.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// mPager.setCurrentItem(NUM_ITEMS-1);
		// }
		// });
	}

	public static class MyAdapter extends FragmentPagerAdapter {
		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return NUM_ITEMS;
		}

		@Override
		public Fragment getItem(int position) {
			return ArrayListFragment.newInstance(position);
		}
	}

	public static class ArrayListFragment extends Fragment implements OnTouchListener  {
		int mNum;

		/**
		 * Create a new instance of CountingFragment, providing "num" as an
		 * argument.
		 */
		static ArrayListFragment newInstance(int num) {
			ArrayListFragment f = new ArrayListFragment();

			// Supply num input as an argument.
			Bundle args = new Bundle();
			args.putInt("num", num);
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

		private String getSdCardPath() {
			return Environment.getExternalStorageDirectory().getPath() + "/";
		}

		private String getPathImage(int mNum2) {
			String pathName = "";
			pathName = getSdCardPath() + "ComicManga/" + "v4_"
					+ String.valueOf(mNum2 + 1) + ".jpg";
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
		    private static final float MIN_ZOOM = 1f,MAX_ZOOM = 1f;

		    // These matrices will be used to scale points of the image
		    Matrix matrix = new Matrix();
		    Matrix savedMatrix = new Matrix();

		    // The 3 states (events) which the user is trying to perform
		    static final int NONE = 0;
		    static final int DRAG = 1;
		    static final int ZOOM = 2;
		    int mode = NONE;

		    // these PointF objects are used to record the point(s) the user is touching
		    PointF start = new PointF();
		    PointF mid = new PointF();
		    float oldDist = 1f;

		    /** Called when the activity is first created. */
//		    @Override
//		    public void onCreate(Bundle savedInstanceState) 
//		    {
//		        super.onCreate(savedInstanceState);
//		        setContentView(R.layout.main);
//		        ImageView view = (ImageView) findViewById(R.id.imageView);
//		        view.setOnTouchListener(this);
//		    }

		    @Override
		    public boolean onTouch(View v, MotionEvent event) 
		    {
		        ImageView view = (ImageView) v;
		        view.setScaleType(ImageView.ScaleType.MATRIX);
		        float scale;

		        dumpEvent(event);
		        // Handle touch events here...

		        switch (event.getAction() & MotionEvent.ACTION_MASK) 
		        {
		            case MotionEvent.ACTION_DOWN:   // first finger down only
		                                                savedMatrix.set(matrix);
		                                                start.set(event.getX(), event.getY());
		                                                Log.d(TAG, "mode=DRAG"); // write to LogCat
		                                                mode = DRAG;
		                                                break;

		            case MotionEvent.ACTION_UP: // first finger lifted

		            case MotionEvent.ACTION_POINTER_UP: // second finger lifted

		                                                mode = NONE;
		                                                Log.d(TAG, "mode=NONE");
		                                                break;

		            case MotionEvent.ACTION_POINTER_DOWN: // first and second finger down

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

		                                                if (mode == DRAG) 
		                                                { 
		                                                    matrix.set(savedMatrix);
		                                                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y); // create the transformation in the matrix  of points
		                                                } 
		                                                else if (mode == ZOOM) 
		                                                { 
		                                                    // pinch zooming
		                                                    float newDist = spacing(event);
		                                                    Log.d(TAG, "newDist=" + newDist);
		                                                    if (newDist > 5f) 
		                                                    {
		                                                        matrix.set(savedMatrix);
		                                                        scale = newDist / oldDist; // setting the scaling of the
		                                                                                    // matrix...if scale > 1 means
		                                                                                    // zoom in...if scale < 1 means
		                                                                                    // zoom out
		                                                        matrix.postScale(scale, scale, mid.x, mid.y);
		                                                    }
		                                                }
		                                                break;
		        }

		        view.setImageMatrix(matrix); // display the transformation on screen

		        return true; // indicate event was handled
		    }

		    /*
		     * --------------------------------------------------------------------------
		     * Method: spacing Parameters: MotionEvent Returns: float Description:
		     * checks the spacing between the two fingers on touch
		     * ----------------------------------------------------
		     */

		    private float spacing(MotionEvent event) 
		    {
		        float x = event.getX(0) - event.getX(1);
		        float y = event.getY(0) - event.getY(1);
		        return FloatMath.sqrt(x * x + y * y);
		    }

		    /*
		     * --------------------------------------------------------------------------
		     * Method: midPoint Parameters: PointF object, MotionEvent Returns: void
		     * Description: calculates the midpoint between the two fingers
		     * ------------------------------------------------------------
		     */

		    private void midPoint(PointF point, MotionEvent event) 
		    {
		        float x = event.getX(0) + event.getX(1);
		        float y = event.getY(0) + event.getY(1);
		        point.set(x / 2, y / 2);
		    }

		    /** Show an event in the LogCat view, for debugging */
		    private void dumpEvent(MotionEvent event) 
		    {
		        String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE","POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
		        StringBuilder sb = new StringBuilder();
		        int action = event.getAction();
		        int actionCode = action & MotionEvent.ACTION_MASK;
		        sb.append("event ACTION_").append(names[actionCode]);

		        if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) 
		        {
		            sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
		            sb.append(")");
		        }

		        sb.append("[");
		        for (int i = 0; i < event.getPointerCount(); i++) 
		        {
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
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				// TODO Auto-generated method stub
//				return false;
//			}

	}
}
