package com.fbwall;

import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.Session.OpenRequest;
import com.tanzeer.fbimagepost.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	private Session.StatusCallback statusCallback = new SessionStatusCallback();
	private boolean isFbButtonClicked = false;
	Button fblogin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
				Session session = Session.getActiveSession();
		if (session == null) {
			if (savedInstanceState != null) {
				session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
			}
			if (session == null) {
				session = new Session(this);
			}
			Session.setActiveSession(session);
			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
				OpenRequest openRequest = new Session.OpenRequest(MainActivity.this);
				openRequest.setPermissions("publish_actions");
				openRequest.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK);
				session.openForPublish(openRequest.setCallback(statusCallback));
			}
		}
		if(session!=null && session.isOpened())
		{
			Intent intent=new Intent(MainActivity.this,ActivityWallPostActivity.class);
			startActivity(intent);
		}
		fblogin=(Button)findViewById(R.id.button1);
		fblogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.e("msg", "fb button clicked");
				isFbButtonClicked = true;
				Session session = Session.getActiveSession();
				if (session.isOpened()) {
					Log.e("msg", "isopened");
					loginWithFb();
				} else if (!session.isOpened() && !session.isClosed()) {
					Log.e("msg", "not open not close");
					OpenRequest openRequest = new Session.OpenRequest(MainActivity.this);
					openRequest.setPermissions("publish_actions");
					openRequest.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK);
					session.openForPublish(openRequest.setCallback(statusCallback));
				} else {
					Log.e("msg", "open active session");
					Session.openActiveSession(MainActivity.this, true, statusCallback);
				}
			}
		});

	}
	private void loginWithFb() {
		isFbButtonClicked = false;
		Log.e("MSG", "inside loginwith fb");
		Session session=Session.getActiveSession();
		if(session!=null && session.isOpened())
		{
			Intent intent=new Intent(MainActivity.this,ActivityWallPostActivity.class);
			startActivity(intent);
		}

	}

	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			// To do
			Log.e("MSG", "session changed" + session.isOpened());
			if (session.isOpened()) {

				if (isFbButtonClicked) {
					
					loginWithFb();
					isFbButtonClicked = false;
				}

			}
		}
	}
	@Override
	public void onStop() {
		super.onStop();
		Session.getActiveSession().removeCallback(statusCallback);
	}

	@Override
	public void onStart() {
		super.onStart();
		Session.getActiveSession().addCallback(statusCallback);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
			Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
	}
}
