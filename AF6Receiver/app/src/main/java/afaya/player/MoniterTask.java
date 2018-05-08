package afaya.player;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MoniterTask extends AsyncTask<Object, Object, Object> implements Serializable{
	private Handler hr;
	private static MoniterTask instance;
	private Process process;
	private boolean b_run;
	private boolean start_receive = false;
	private String packageName = "";

	public static MoniterTask getInstance(Handler hr, String pack) {
		if (instance == null) {
			instance = new MoniterTask(hr, pack);

		} else {
			if (instance.getStatus() == Status.FINISHED)
				instance = new MoniterTask(hr, pack);
			instance.hr = hr;
			instance.packageName = pack;
		}
		return instance;

	}

	public MoniterTask(Handler hr, String pack) {
		this.hr = hr;
		this.packageName = pack;

	}

	public void StopTask() {
		b_run = false;
		instance = null;
	}

	public void stopC() {
		Log.e("process = ", "" + process);
		if (process != null) {
			Log.e("stopC ", "deal");
			boolean pid = false;
			String s = "/n";
			String name = "afdevice";
			try {
				int first = 0;
				Process p = Runtime.getRuntime().exec("ps " + name);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						p.getInputStream()));
				String line = null;
				while ((line = in.readLine()) != null) {
					String[] names = line.replaceAll("\\s{1,}", " ").split(" ");

					if (first > 0) {
						pid = true;
						break;
					}
					first++;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (pid) {
				BufferedWriter stdOutput = new BufferedWriter(
						new OutputStreamWriter(process.getOutputStream()));
				Log.e("stdOutput = ", "" + stdOutput);
				try {
					if (stdOutput != null) {
						stdOutput.write("Quit");
						stdOutput.flush();
						stdOutput.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	protected Object doInBackground(Object... params) {
		Runtime rt = Runtime.getRuntime();
		b_run = true;
		String line = null;
		try {
			String str2 = "/data/data/" + packageName + "/files/afdevice"
					+ "\n";
			//Log.e("run ", "" + str2);
			process = rt.exec(str2);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(
					process.getInputStream()));

			String outs = null;
			while (b_run) {
				while (stdInput.ready()) {
					char[] b = new char[32];     
					stdInput.read(b);
					outs = String.valueOf(b);
					//Log.e("outs = ", "" + outs);
					if (outs.length() > 5) {
						if (outs.substring(9, 14).compareTo("START") == 0) {
							Message msg = new Message();
							msg.obj = "connected";
							if (this.hr != null)
								this.hr.sendMessage(msg);
						}
					}
					if (outs.startsWith("I_")) {
						Message msg = new Message();
						msg.obj = outs.substring(2);
						if (this.hr != null)
							this.hr.sendMessage(msg);
					} else if (outs.startsWith("e1")) {
						b_run = false;
						Message msg = new Message();
						msg.obj = outs;
						msg.arg1 = 101;
						if (this.hr != null)
							this.hr.sendMessage(msg);
					}
				}
			}
		} catch (IOException e) {
			try{
				String str2 = "/data/data/" + packageName + "/files/afdevice"
						+ "\n";
				//Log.e("run ", "" + str2);
				process = rt.exec(str2);
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(
						process.getInputStream()));

				String outs = null;
				while (b_run) {
					while (stdInput.ready()) {
						char[] b = new char[32];     
						stdInput.read(b);
						outs = String.valueOf(b);
						//Log.e("outs = ", "" + outs);
						if (outs.length() > 5) {
							if (outs.substring(9, 14).compareTo("START") == 0) {
								Message msg = new Message();
								msg.obj = "connected";
								if (this.hr != null)
									this.hr.sendMessage(msg);
							}
						}
						if (outs.startsWith("I_")) {
							Message msg = new Message();
							msg.obj = outs.substring(2);
							if (this.hr != null)
								this.hr.sendMessage(msg);
						} else if (outs.startsWith("e1")) {
							b_run = false;
							Message msg = new Message();
							msg.obj = outs;
							msg.arg1 = 101;
							if (this.hr != null)
								this.hr.sendMessage(msg);
						}
					}
				}
			}catch(Exception exc){
				
			}
		}
		return null;
	}
}
