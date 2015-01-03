package com.planet1107.welike.other;

import com.layer.sdk.LayerClient;
import com.layer.sdk.exceptions.LayerException;
import com.layer.sdk.listeners.LayerConnectionListener;

public class MyConnectionListener implements LayerConnectionListener {

    @Override
    public void onConnectionConnected(LayerClient client) {
        client.authenticate();
    }

    @Override
    public void onConnectionDisconnected(LayerClient arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onConnectionError(LayerClient arg0, LayerException e) {
        // TODO Auto-generated method stub
    }

}
