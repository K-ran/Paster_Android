package in.karanpurohit.paster;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class CopyListenerService extends Service {
    BufferedReader bReader=null;
    BufferedWriter bWriter=null;
    Reciever r1;
    static String DEBUG_TAG = "cool Debug ";
    public CopyListenerService() {
    }
    ClipboardManager.OnPrimaryClipChangedListener listener;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            bReader = new BufferedReader(new InputStreamReader(MainActivity.s1.getInputStream()));
            bWriter = new BufferedWriter(new PrintWriter(MainActivity.s1.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        listener = new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                ClipData data = ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).getPrimaryClip();
                Log.d("asdasd asd",data.getItemAt(0).getText().toString());
                try {
                    bWriter.write(data.getItemAt(0).getText().toString()+"\n");
                    bWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Log.d(CopyListenerService.DEBUG_TAG,"Starting Thread");
        r1= new Reciever(bReader,getApplicationContext(),this);
        r1.start();
        Log.d(DEBUG_TAG,"Service started");
        SharedPreferenceManager.setValue(this,MainActivity.KEY,"true");
        ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).addPrimaryClipChangedListener(listener);
        return START_STICKY;
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    @Override
    public void onDestroy() {

        Toast.makeText(CopyListenerService.this, "Service Stoped", Toast.LENGTH_SHORT).show();
        Log.d(DEBUG_TAG,"Service stoped");
        if(r1!=null)
            r1.stopRunning();
        ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).removePrimaryClipChangedListener(listener);
        SharedPreferenceManager.setValue(this,MainActivity.KEY,"false");
    }

    public void setClipBoard(String data){
        Log.d(CopyListenerService.DEBUG_TAG,"Inside Call back "+data);
        ClipboardManager clipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Text", data);
        clipboard.setPrimaryClip(clip);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
