package vlc.khanhleo.comicmanga;

import vlc.khanhle.comicmanga.R;
import vlc.khanhleo.comicmanga.utils.Consts;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreenActivity extends Activity {

	private boolean mActive = true;
	private final int mSplashTime = 3000; // time to display the splash screen
											// in ms

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash_screen);

		Thread splashTread = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					while (mActive && (waited < mSplashTime)) {
						sleep(100);
						if (mActive) {
							waited += 100;
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {

					if (Consts.getIsFirstUse(getApplication())) {
						Intent intent = new Intent(SplashScreenActivity.this,
								HelpActivity.class);
						startActivity(intent);

					} else {
						Intent intent = new Intent(SplashScreenActivity.this,
								MainActivity.class);
						startActivity(intent);
					}

					finish();
				}
			}
		};
		splashTread.start();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			mActive = false;
		}
		return true;
	}

}
