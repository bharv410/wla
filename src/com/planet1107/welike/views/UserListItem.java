package com.planet1107.welike.views;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.findatrainerapp.welike.R;
import com.planet1107.welike.activities.ProfileActivity;
import com.planet1107.welike.connect.Connect;
import com.planet1107.welike.connect.User;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;

public class UserListItem extends RelativeLayout implements OnClickListener {

	User mUser;
	ImageButton mImageButtonFollowUnfollow;
	Connect sharedConnect;
	
	public UserListItem(Context context) {
		
		super(context);
	}

	public UserListItem(Context context, AttributeSet attrs) {
	
		super(context, attrs);
	}

	public UserListItem(Context context, AttributeSet attrs, int defStyle) {
		
		super(context, attrs, defStyle);
	}

	public void setUser(User user) {
		
		mUser = user;	
		reloadView();
	}

	protected void onFinishInflate () {
		
		mImageButtonFollowUnfollow = (ImageButton) this.findViewById(R.id.imageButtonFollowUnfollow);
		mImageButtonFollowUnfollow.setFocusable(false);
		mImageButtonFollowUnfollow.setOnClickListener(this);
        TextView textViewUsername = (TextView) findViewById(R.id.textViewUserUsername);
        ImageView imageViewUserimage = (ImageView)findViewById(R.id.imageViewUserUserImage);
        textViewUsername.setClickable(true);
        imageViewUserimage.setClickable(true);
        textViewUsername.setOnClickListener(this);
        imageViewUserimage.setOnClickListener(this);
		sharedConnect = Connect.getInstance(getContext());
	}
	
	@Override
	public void onClick(View v) {
		
		if (v == mImageButtonFollowUnfollow) {
			if (mUser.userID == sharedConnect.getCurrentUser().userID) {
				return;
			} else if (mUser.followingUser) {
				new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						
						sharedConnect.unfollowUser(mUser.userID);
						return null;
					}
				}.execute();
				mUser.followingUser = false;
				sharedConnect.getCurrentUser().userFollowingCount--;
			} else {
				new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						
						sharedConnect.followUser(mUser.userID);
						return null;
					}
				}.execute();
				mUser.followingUser = true;
				sharedConnect.getCurrentUser().userFollowingCount++;
			}
			reloadView();
		} else {
			showUser();
		}
		
	}
	
	private void reloadView() {
		
		if (mUser.userID == sharedConnect.getCurrentUser().userID) {
			mImageButtonFollowUnfollow.setVisibility(View.INVISIBLE);
		} else if (mUser.followingUser) {
			mImageButtonFollowUnfollow.setVisibility(View.VISIBLE);
			mImageButtonFollowUnfollow.setImageResource(R.drawable.btn_unfollow);
		} else {
			mImageButtonFollowUnfollow.setVisibility(View.VISIBLE);
			mImageButtonFollowUnfollow.setImageResource(R.drawable.btn_follow);
		}
        UrlImageViewHelper.setUrlDrawable(((ImageView)findViewById(R.id.imageViewUserUserImage)), mUser.userAvatarPath);
        ((TextView)findViewById(R.id.textViewUserUsername)).setText(mUser.userFullName);
	}
	
	public void showUser() {
		
		Intent intentUser = new Intent(getContext(), ProfileActivity.class);
		intentUser.putExtra("userID", mUser.userID);
		getContext().startActivity(intentUser);
	}
}
