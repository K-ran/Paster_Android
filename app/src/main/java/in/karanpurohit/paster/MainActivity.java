package in.karanpurohit.paster;

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
import java.net.Socket;

import in.karanpurohit.paster.CopyListenerService;

public class MainActivity extends AppCompatActivity {

    String started="";
    public static String KEY= "runningState";
    static Socket s1;
    EditText edittext;
    Button button ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edittext=((EditText)findViewById(R.id.etIp));
        button=((Button)findViewById(R.id.btnConnect));
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
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                s1 = new Socket(params[0], 2800);
            } catch (IOException e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean){
                button.setText("Disconnect");
                SharedPreferenceManager.setValue(MainActivity.this,KEY,"true");
                Intent intent = new Intent(MainActivity.this, CopyListenerService.class);
                startService(intent);
                Log.d(CopyListenerService.DEBUG_TAG,"connecting");
            }else{
                Toast.makeText(MainActivity.this, "Someting is wrong!\nCannot connect to your computer", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
