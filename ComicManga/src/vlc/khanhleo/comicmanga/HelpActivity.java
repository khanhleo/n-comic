package vlc.khanhleo.comicmanga;

import vlc.khanhle.comicmanga.R;
import vlc.khanhleo.comicmanga.utils.Consts;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class HelpActivity extends FragmentActivity {
	private static final int PAGE_NUM = 5;

	private int[] drawables = new int[] { R.drawable.screen_shot_1,
			R.drawable.screen_shot_2, R.drawable.screen_shot_3,
			R.drawable.screen_shot_4, R.drawable.screen_shot_5 };

	private ViewPager viewPager;
	private ImageView btnLeft, btnRight;
//	private Button btnSkip;
	private int currentPageIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_help);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		btnLeft = (ImageView) findViewById(R.id.btnLeft);
		btnRight = (ImageView) findViewById(R.id.btnRight);
//		btnSkip = (Button) findViewById(R.id.btnSkip);
		OnBoardingPagerAdapter adapter = new OnBoardingPagerAdapter(this);
		viewPager.setAdapter(adapter);

		btnLeft.setVisibility(View.GONE);

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int pageIndex) {
				if (pageIndex == 0) {
					btnLeft.setVisibility(View.GONE);
				} else {
					btnLeft.setVisibility(View.VISIBLE);
					btnRight.setVisibility(View.VISIBLE);
				}
				currentPageIndex = pageIndex;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

	}

	public void onClickLeft(View v) {
		currentPageIndex--;
		viewPager.setCurrentItem(currentPageIndex);
	}

	public void onClickRight(View v) {
		if (currentPageIndex == drawables.length - 1) {
			if (Consts.getIsFirstUse(getApplication())) {
				Intent i = new Intent(this, MainActivity.class);
				startActivityForResult(i, 1);
			}else{
				finish();
			}
		} else {
			currentPageIndex++;
			viewPager.setCurrentItem(currentPageIndex);
		}
	}

	public void onClickSkip(View v) {
		if (Consts.getIsFirstUse(getApplication())) {
			Intent i = new Intent(this, MainActivity.class);
			startActivity(i);
			finish();
		}else{
			finish();
		}
	}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//
//		setResult(10);
//		finish();
//	}

	private class OnBoardingPagerAdapter extends PagerAdapter {
		private Context mContext;

		public OnBoardingPagerAdapter(Context c) {
			mContext = c;
		}

		@Override
		public int getCount() {
			return PAGE_NUM;
		}

		@Override
		public boolean isViewFromObject(View paramView, Object paramObject) {
			return (paramView == paramObject);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = (ImageView) LayoutInflater.from(mContext)
					.inflate(R.layout.a_help_page, null);
			imageView.setImageResource(drawables[position]);
			container.addView(imageView, 0);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((ImageView) object);
		}

	}
}
