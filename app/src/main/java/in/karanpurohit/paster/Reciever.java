package in.karanpurohit.paster;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;

class Reciever extends Thread{
		Context context;
		boolean running = true;
		CopyListenerService copyListenerService;
		BufferedReader bReader =null;
		public Reciever(BufferedReader bReader,Context context,CopyListenerService copyListenerService) {
			super();
			this.bReader=bReader;
			this.context = context;
			this.copyListenerService=copyListenerService;
		}
		@Override
		public void run(){
			Log.d(CopyListenerService.DEBUG_TAG,"Running Thread");
			while(running){
				try {
					System.out.println("Waiting for input");
					String data = bReader.readLine();
					if(data!=null){
						Log.d(CopyListenerService.DEBUG_TAG,data);
						copyListenerService.setClipBoard(data);
					}
					else{
						Log.d(CopyListenerService.DEBUG_TAG,"Oops");
						return;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
			}
			try {
				bReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		void stopRunning(){
			running=false;
		}
	}