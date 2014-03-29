package com.band.activities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.IntentSender;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi.MetadataBufferResult;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFile.DownloadProgressListener;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.drive.DriveApi.ContentsResult;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.api.services.drive.DriveScopes;

public class MainActivity extends Activity implements ConnectionCallbacks, OnConnectionFailedListener, ResultCallback<MetadataBufferResult> {
	
	GoogleApiClient mGoogleApiClient;
	
	protected String mAccountName;
    protected static final String EXTRA_ACCOUNT_NAME = "account_name";
    protected static final int REQUEST_CODE_RESOLUTION = 1;
    protected static final int NEXT_AVAILABLE_REQUEST_CODE = 2;
    protected static final int REQUEST_CODE_CREATOR = 3;
    protected static final int REQUEST_CODE_OPENER = 4;
    
    private static Query shared;
    private static Query title;
    private static Query notShared;
    private static Query token;
    private boolean hasFiles;
    private String mNextPageToken;
	
    /**
     * Progress bar to show the current download progress of the file.
     */
    private ProgressBar mProgressBar;

    /**
     * File that is selected with the open file activity.
     */
    private DriveId mSelectedFileDriveId;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d("Testing", "In the onCreate");
	    super.onCreate( savedInstanceState );
/*
/*	    
	    shared = new Query.Builder().addFilter(Filters.sharedWithMe()).build();
	    title = new Query.Builder().addFilter(Filters.eq(SearchableField.TITLE, "Carbon Scoop")).build();
	    notShared = new Query.Builder().addFilter(Filters.eq(SearchableField.TITLE, "*")).build();
	    
	    token = new Query.Builder().setPageToken(mNextPageToken).build();
	    
	    hasFiles = true;
	    
	    mGoogleApiClient = new GoogleApiClient.Builder( this )
	            .addApi( Drive.API )
	            .addScope(Drive.SCOPE_FILE)
//	            .addScope(new Scope(DriveScopes.DRIVE))
	            .addConnectionCallbacks( this )
	            .addOnConnectionFailedListener( this )
	            .build();
*/	    
	    if (savedInstanceState != null) {
            mAccountName = savedInstanceState.getString(EXTRA_ACCOUNT_NAME);
        }
        if (mAccountName == null) {
            mAccountName = getIntent().getStringExtra(EXTRA_ACCOUNT_NAME);
        }

        if (mAccountName == null) {
            Account[] accounts = AccountManager.get(this).getAccountsByType("com.google");
            if (accounts.length == 0) {
                Log.d("testing", "Must have a Google account installed");
                return;
            }
            mAccountName = accounts[0].name;
        }
        
        readMasterDotFile = false;
        
        Log.d("testing", mAccountName);
        Log.d("testing", "Out of onCreate");
	}
	
	/**
     * Saves the activity state.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_ACCOUNT_NAME, mAccountName);
    }
    
    /**
     * Called when activity gets visible. A connection to Drive services need to
     * be initiated as soon as the activity is visible. Registers
     * {@code ConnectionCallbacks} and {@code OnConnectionFailedListener} on the
     * activities itself.
     */    
	@Override
	protected void onResume() {
		Log.d("Testing", "onResume()");
        super.onResume();

        if (mAccountName == null) {
            return;
        }
        
        Log.d("Testing", "onResume() - 2");
        
        if (mGoogleApiClient == null) {
            // permissions to query available accounts.
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
        }

        if( readMasterDotFile == true )
        {
        	return;
        }
        
        try
        {
        	InputStream in = openFileInput("bdu_master_dot_fileid");
        	
        	if( in != null )
        	{
        		InputStreamReader tmp = new InputStreamReader(in);
        		BufferedReader reader = new BufferedReader(tmp);
        		String str;
        		StringBuilder buf = new StringBuilder();
        		
        		while( (str = reader.readLine()) != null )
        		{
        			buf.append(str+"\n");
        		}
        		
        		in.close();
        	
        		if( buf.length() > 0 )
        		{
        		    mSelectedFileDriveId = DriveId.decodeFromString( buf.toString() );
        		
        		    Log.i("testing", "Using previously selected file: " + mSelectedFileDriveId.encodeToString() );
        		}
        	}
        	
        }
        catch( java.io.FileNotFoundException e ){
        	// The file hasn't been created yet.  This is not an error and will just cause the 
        	// file selection dialog to pop up later.
        }
        catch( Throwable t)
        {
        	Toast.makeText( this, "Exception: " + t.toString(), Toast.LENGTH_SHORT ).show();
    	    Log.i("testing","fileid-load", t);
        }
        
        mGoogleApiClient.connect();
    }
	
    /**
     * Called when activity gets invisible. Connection to Drive service needs to
     * be disconnected as soon as an activity is invisible.
     */	
	@Override
    protected void onPause() {
		Log.d("Testing", "onPause()");
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onPause();
        
        Log.d("Testing", "onPause(): " + mSelectedFileDriveId );
        
        if( mSelectedFileDriveId != null )
        {
            try
            {
        	    OutputStreamWriter out = new OutputStreamWriter( openFileOutput("bdu_master_dot_fileid", 0 ) );
        	
        	    out.write( mSelectedFileDriveId.encodeToString() + "\n" );
        	    out.close();
        	    
        	    Log.i("testing","fileid-save: " + mSelectedFileDriveId.encodeToString() );
            }
            catch( Throwable t )
            {
        	    Toast.makeText( this, "Exception: " + t.toString(), Toast.LENGTH_SHORT ).show();
        	    Log.i("testing","fileid-save-exception", t);
            }
        }
    }
	
	public void search() {
		Log.d("Testing", "search()");
		/*
        //Drive.DriveApi.query(mGoogleApiClient, shared).setResultCallback(this);
		//Drive.DriveApi.query(mGoogleApiClient, all).setResultCallback(this);
        Drive.DriveApi.query(mGoogleApiClient, notShared).setResultCallback(this);
        Log.d("Testing", "done with the search()");
		*/
		
		//if (!hasFiles) {
        //    return;
        //}
        // retrieve the results for the next page.
        //Query query = new Query.Builder().build();
        //Drive.DriveApi.query(mGoogleApiClient, query).setResultCallback(this);
        
        Log.d( "testing", "mGoogleApiClient.connected:" + mGoogleApiClient.isConnected() );

        DriveFolder rootFolder = Drive.DriveApi.getRootFolder(mGoogleApiClient);

        rootFolder.listChildren( mGoogleApiClient ).setResultCallback( rootFolderCallback );
	}
	
	final private ResultCallback<MetadataBufferResult> rootFolderCallback =
	        new ResultCallback<MetadataBufferResult>() {
	            @Override
	            public void onResult(MetadataBufferResult metadataBufferResult) {
	                Log.d("testing", "got root folder");
	                MetadataBuffer buffer = metadataBufferResult.getMetadataBuffer();
	                Log.d("testing", "Buffer count  " + buffer.getCount());
	                for(Metadata m : buffer){
	                    Log.d("testing", "Metadata name  " + m.getTitle() + "(" + (m.isFolder() ? "folder" : "file") + ")");
	                    /*
	                    if (m.isFolder() && m.getTitle().equals("Neewie"))
	                        Drive.DriveApi.getFolder(mApiClient, m.getDriveId())
	                                .listChildren(mApiClient)
	                                .setResultCallback(fileCallback);
	                    */
	                }
	            }
	};

	private boolean readMasterDotFile;
	
	/*
	@Override
	protected void onStart() {
		Log.d("Testing", "onStart()");
		Log.d("Testing", mGoogleApiClient.toString());
	    super.onStart();
	    mGoogleApiClient.connect();
	}
	*/
	/*
	@Override
	protected void onStop() {
		Log.d("Testing", "onStop()");
		Log.d("Testing", mGoogleApiClient.toString());
	    super.onStart();
	    mGoogleApiClient.disconnect();
	}
    */

	@Override
	public void onConnected( Bundle connectionHint ) {
		Log.d("testing", "Connected YAYAYAYAYAY!");
		//saveFileToDrive();
		//search();
        // If there is a selected file, open its contents.
        if( mSelectedFileDriveId != null ) 
        {
            open();
            return;
        }
		
	    IntentSender intentSender = Drive.DriveApi
		            .newOpenFileActivityBuilder()
		            .setMimeType(new String[] { "txt/plain", "text/plain", "text/html", "application/xml" })
		            .build(mGoogleApiClient);
		try {
		    startIntentSenderForResult( intentSender, REQUEST_CODE_OPENER, null, 0, 0, 0 );
		} catch (SendIntentException e) {
		    Log.w("testing", "Unable to send intent", e);
		}
	}

	@Override
	public void onConnectionSuspended( int cause ) {
		Log.d("testing", "Connection suspended");
	}

	@Override
	public void onConnectionFailed( ConnectionResult result ) {
		Log.d("Testing", "onConnectionFailed()");
		if (!result.hasResolution()) {
			Log.d("Testing", "onConnectionFailed() - no resolution");
			Log.d("Testing", result.toString());
			Log.d("Testing", GooglePlayServicesUtil.getErrorString(result.getErrorCode()));
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
            return;
        }
        try {
            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        } catch (SendIntentException e) {
            Log.d("testing", "Exception while starting resolution activity", e);
        }	
	}
	
	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		Log.d("Testing", "onActivityResult()");
		super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_RESOLUTION && resultCode == RESULT_OK) {
            mGoogleApiClient.connect();
        }
        else if( requestCode == REQUEST_CODE_CREATOR )
        {
            // Called after a file is saved to Drive.
            if (resultCode == RESULT_OK) 
            {
                Log.i("testing", "File created.");
                //search();
            }
        }
        else if( requestCode == REQUEST_CODE_OPENER )
        {
            Log.i("testing", "File opener: " + resultCode);
            // Called after a file is saved to Drive.
            if (resultCode == RESULT_OK) 
            {
                Log.i("testing", "File opener.");
                mSelectedFileDriveId = (DriveId) data.getParcelableExtra( OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID );
                Log.i("testing", "selectedFileID: " + mSelectedFileDriveId.encodeToString());
                
                
                //search();
            }
        }        
	}
	

	
	 private void saveFileToDrive() {
	        // Start by creating a new contents, and setting a callback.
	        Log.d("testing", "Creating new contents.");
	        Drive.DriveApi.newContents(mGoogleApiClient).setResultCallback(new ResultCallback<ContentsResult>() {

	            @Override
	            public void onResult(ContentsResult result) {
	                // If the operation was not successful, we cannot do anything
	                // and must
	                // fail.
	                if (!result.getStatus().isSuccess()) {
	                    Log.i("testing", "Failed to create new contents.");
	                    return;
	                }
	                // Otherwise, we can write our data to the new contents.
	                Log.d("testing", "New contents created.");
	                // Get an output stream for the contents.
	                OutputStream outputStream = result.getContents().getOutputStream();
	                // Write the bitmap data from it.
	                try {
	                    outputStream.write( "<test-xml><child>bob</child><child>ted</child></test-xml>".getBytes() );
	                } catch (IOException e1) {
	                    Log.i("testing", "Unable to write file contents.");
	                }
	                // Create the initial metadata - MIME type and title.
	                // Note that the user will be able to change the title later.
	                MetadataChangeSet metadataChangeSet = new MetadataChangeSet.Builder().setMimeType("application/xml").setTitle("test.xml").build();
	                // Create an intent for the file chooser, and start it.
	                IntentSender intentSender = Drive.DriveApi
	                        .newCreateFileActivityBuilder()
	                        .setInitialMetadata(metadataChangeSet)
	                        .setInitialContents(result.getContents())
	                        .build(mGoogleApiClient);
	                try {
	                    startIntentSenderForResult( intentSender, REQUEST_CODE_CREATOR, null, 0, 0, 0 );
	                } catch (SendIntentException e) {
	                    Log.i("testing", "Failed to launch file chooser.");
	                }
	            }
	        });
	 }
	 

	
	@Override
    public void onResult(MetadataBufferResult result) {
		if(!result.getStatus().isSuccess()){
			Toast.makeText(this, "Didn't work", Toast.LENGTH_SHORT).show();
			return;
		}
		Log.d( "testing", "MetaDataBuffer" + result.getMetadataBuffer() );
		Log.d( "testing", "Page Token: " + result.getMetadataBuffer().getNextPageToken());
		//mNextPageToken = result.getMetadataBuffer().getNextPageToken();
        //hasFiles = mNextPageToken != null;
		
		Log.d("testing", "Contents: " + result.getMetadataBuffer().describeContents());
        Log.d("testing", "Retrieved file count: " + result.getMetadataBuffer().getCount());
        Log.d("testing", "Retrieved is sucess: " + result.getStatus() );
        Log.d("testing", "Retrieved is sucess: " + result.getStatus().getStatusCode() );        
        Log.d("testing", "Tings " + result.getMetadataBuffer().getMetadata() );
        Log.d("testing", "PageToken " + result.getMetadataBuffer().getNextPageToken() );
    	
    }
   
    private void open() {
        // Reset progress dialog back to zero as we're
        // initiating an opening request.
        //mProgressBar.setProgress(0);
        DownloadProgressListener listener = new DownloadProgressListener() {
            @Override
            public void onProgress(long bytesDownloaded, long bytesExpected) {
                // Update progress dialog with the latest progress.
                int progress = (int)(bytesDownloaded*100/bytesExpected);
                Log.d("testing", String.format("Loading progress: %d percent", progress));
                //mProgressBar.setProgress(progress);
            }
        };
        Drive.DriveApi.getFile( mGoogleApiClient, mSelectedFileDriveId )
            .openContents( mGoogleApiClient, DriveFile.MODE_READ_ONLY, listener)
            .setResultCallback(contentsCallback);
        
        readMasterDotFile = true;
    }

    private ResultCallback<ContentsResult> contentsCallback = new ResultCallback<ContentsResult>() {
        @Override
        public void onResult(ContentsResult result) {
            if (!result.getStatus().isSuccess()) {
                Log.i("testing", "Error while opening the file contents");
                return;
            }
            Log.i("testing", "File contents opened");
            
            try{
                InputStream inStream = result.getContents().getInputStream();
            
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
                StringBuilder out = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    out.append(line);
                }
                reader.close();
            
                Log.i("testing", "Content: " + out.toString() );
            
            }
            catch( IOException ioe )
            {
                Log.i("testing", "IOException: " + ioe.getMessage() );
            }
        }
    };
   
}
