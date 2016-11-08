package in.karanpurohit.paster;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import in.karanpurohit.paster.CopyListenerService;

public class MainActivity extends AppCompatActivity {

    String started="";                        //used by shared preference
    public static String KEY= "runningState"; //used by shared preference

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
            button.setText("Connect");
            stopService(new Intent(this, CopyListenerService.class));
            if(s1!=null){
                try {
                    Log.d(CopyListenerService.DEBUG_TAG,"hello");
//                  0821__bye is just a Disconnect token, tells the server that I(app) am closing
//                  the connection.
                    BufferedWriter bWriter = new BufferedWriter(new PrintWriter(MainActivity.s1.getOutputStream()));
                    bWriter.write("0821__bye"+"\n");
                    bWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                s1.close();
            }
            edittext.setEnabled(true);
            SharedPreferenceManager.setValue(this,KEY,"false");
            Log.d(CopyListenerService.DEBUG_TAG,"disconnectiong");
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

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
                //This wait is pure show off
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
                button.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.connected));
                button.setText("Disconnect");
                edittext.setEnabled(false);
                SharedPreferenceManager.setValue(MainActivity.this,KEY,"true");
                Intent intent = new Intent(MainActivity.this, CopyListenerService.class);
                startService(intent);
                Log.d(CopyListenerService.DEBUG_TAG,"connecting");
                Toast.makeText(MainActivity.this, "Connected Successfully", Toast.LENGTH_SHORT).show();
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }else{
                Toast.makeText(MainActivity.this, "Something is wrong! Cannot connect to your device", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
