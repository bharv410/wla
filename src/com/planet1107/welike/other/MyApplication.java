package com.planet1107.welike.other;

import java.util.UUID;

import com.layer.sdk.LayerClient;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application
{
	public static Context ctx;
  @Override
  public void onCreate()
  {
    super.onCreate();
     ctx=getApplicationContext();
	// Create a LayerClient object
	UUID appID = UUID.fromString("ee8f6f06-921b-11e4-b982-75bc6005303e");
	LayerClient layerClient = LayerClient.newInstance(this, appID, "GCM ID");
	layerClient.registerConnectionListener(new MyConnectionListener());
	layerClient.registerAuthenticationListener(new MyAuthenticationListener());
	layerClient.connect();
  }
}
