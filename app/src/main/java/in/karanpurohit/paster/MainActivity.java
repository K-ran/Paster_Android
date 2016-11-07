package in.karanpurohit.paster;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import in.karanpurohit.paster.CopyListenerService;

public class MainActivity extends AppCompatActivity {

    String started="";
    public static String KEY= "runningState";
    static Socket s1;
    EditText edittext;
    Button button ;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edittext=((EditText)findViewById(R.id.etIp));
        button=((Button)findViewById(R.id.btnConnect));
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Connecting to your device");
        progressDialog.setCanceledOnTouchOutside(false);
        started=SharedPreferenceManager.getValue(this,KEY);
        if(started==null){
            SharedPreferenceManager.setValue(this,KEY,"false");
        }
        Log.d("Started onCreate ",SharedPreferenceManager.getValue(this,KEY));
    }

    public void connect(View view) throws IOException {
        if(SharedPreferenceManager.getValue(this,KEY).equals("false")){
            pcConnecter pcConnecter = new pcConnecter();
            pcConnecter.execute(edittext.getText().toString());
        }
        else {
            if(s1!=null){
                s1.close();
            }
            button.setText("Connect");
            stopService(new Intent(this, CopyListenerService.class));
            SharedPreferenceManager.setValue(this,KEY,"false");
            Log.d(CopyListenerService.DEBUG_TAG,"disconnectiong");
        }
    }

    class pcConnecter extends AsyncTask<String,String,Boolean>{
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                s1 = new Socket();
                s1.connect(new InetSocketAddress(params[0], 2800),3000);
            } catch (IOException e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressDialog.dismiss();
            if(aBoolean){
                button.setText("Disconnect");
                SharedPreferenceManager.setValue(MainActivity.this,KEY,"true");
                Intent intent = new Intent(MainActivity.this, CopyListenerService.class);
                startService(intent);
                Log.d(CopyListenerService.DEBUG_TAG,"connecting");
                Toast.makeText(MainActivity.this, "Connected Successfully", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this, "Something is wrong! Cannot connect to your device", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
