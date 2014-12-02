package vlc.khanhleo.comicmanga;

import vlc.khanhle.comicmanga.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("NewApi")
public class MainActivity extends Activity {

	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mWebView = (WebView) findViewById(R.id.wv_content);
		mWebView.setWebViewClient(new WebViewClient());

		String url = "http://vechai.info/shin-cau-be-but-chi-vol-4/";
		
		// url =
		// "http://game.24h.com.vn/game-hay-nhat/pikachu-phien-ban-moi-c131g597b15.html";
		// url = "file:///android_asset/game/testgame.html";
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setLoadsImagesAutomatically(true);
		mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setDisplayZoomControls(false);
		mWebView.loadUrl(url);
	}
}
