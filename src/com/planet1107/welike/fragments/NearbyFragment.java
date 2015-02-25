package com.planet1107.welike.fragments;

import java.util.ArrayList;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.findatrainerapp.welike.R;
import com.planet1107.welike.connect.Connect;
import com.planet1107.welike.connect.User;

public class NearbyFragment extends Fragment{
	Location lastLocation;
	//private GoogleMap mMap;
	private static View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		if (view != null) {
	        ViewGroup parent = (ViewGroup) view.getParent();
	        if (parent != null)
	            parent.removeView(view);
	    }
	    try {
	        view = inflater.inflate(R.layout.fragment_nearby, container, false);
	    } catch (InflateException e) {
	        /* map is already there, just return view as it is */
	    }
	    return view;
	}
	
	@Override
	public void onActivityCreated (Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);	
//		
//		setHasOptionsMenu(true);
//		mMap = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map)).getMap();
//		locationClient = new LocationClient(getActivity(), new GooglePlayServicesClient.ConnectionCallbacks() {
//			
//			@Override
//			public void onDisconnected() {
//				
//			}
//			
//			@Override
//			public void onConnected(Bundle connectionHint) {
//				LocationRequest locationRequest = LocationRequest.create();
//				locationClient.requestLocationUpdates(locationRequest, NearbyFragment.this);
//			}
//		}, this);
//		locationClient.connect();
//		try{
//		mMap.setMyLocationEnabled(true);
//		}catch(NullPointerException e){
//			e.printStackTrace();
//		}
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		inflater.inflate(R.menu.nearby, menu);
	}
	
	
	
	private class PrintAllUsers extends AsyncTask<String, Void, ArrayList<User>> {
		 
        @Override 
        protected ArrayList<User> doInBackground(String... params) {
        	Connect sharedConnect = Connect.getInstance(getActivity());
	    	Location lastLocation = NearbyFragment.this.lastLocation;
	       ArrayList<User> users = sharedConnect.getUsersAround(lastLocation.getLatitude(), lastLocation.getLongitude(), 10000, 1, 20);
	       return users; 
        } 
 
        @Override 
        protected void onPostExecute(ArrayList<User> users) {
        	Toast.makeText(getActivity(), ""+users.size(), Toast.LENGTH_SHORT).show();
        	for( User user: users){
		    	   Toast.makeText(getActivity(), user.userFullName, Toast.LENGTH_SHORT).show();
		       }
        	Toast.makeText(getActivity(), "coords = " + lastLocation.getLatitude()+", "+ lastLocation.getLongitude(), Toast.LENGTH_LONG).show();
        } 
    } 
	
}