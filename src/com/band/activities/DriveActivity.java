package com.band.activities;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.drive.Drive;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;



public class DriveActivity extends BaseAct implements ConnectionCallbacks, OnConnectionFailedListener {
	
	GoogleApiClient apiClient;
	String mAccountName;
	protected static final String EXTRA_ACCOUNT_NAME = "account_name";

	
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		
		if (savedInstanceState != null) {
            mAccountName = savedInstanceState.getString( EXTRA_ACCOUNT_NAME );
            Log.d("driveTest", "mAccoutName InstanceState: " + savedInstanceState.getString(EXTRA_ACCOUNT_NAME));
        }
        if (mAccountName == null) {
            mAccountName = getIntent().getStringExtra( EXTRA_ACCOUNT_NAME );
            Log.d("driveTest", "mAccoutName Intent : " + getIntent().getStringExtra(EXTRA_ACCOUNT_NAME));
        }

        if (mAccountName == null) {
            Account[] accounts = AccountManager.get(this).getAccountsByType( "com.google" );
            if (accounts.length == 0) {
                Toast.makeText( this, "Must have a Google account installed", Toast.LENGTH_SHORT );
                return;
            }
            mAccountName = accounts[0].name;
            Log.d("driveTest", "mAccountName assigned: " + mAccountName );
        }
        
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.d("driveTest", "OnResume reached");
		
		if (apiClient == null) {
            // permissions to query available accounts.
            apiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
        }
		apiClient.connect();
		
	}
	
	@Override
	public void onPause() {
		/*
		if( apiClient != null ){
			apiClient.disconnect();
		}
		*/
		super.onPause();
	}
	
	@Override
    protected void onSaveInstanceState( Bundle outState ) {
        super.onSaveInstanceState( outState );
        outState.putString( EXTRA_ACCOUNT_NAME, mAccountName );
    }

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		Log.d("driveTest", "Connected");
		
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		Log.d("driveTest", "Connection Suspended");
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		Log.d("driveTest", "Connection Failed");
		
	}
	
}
