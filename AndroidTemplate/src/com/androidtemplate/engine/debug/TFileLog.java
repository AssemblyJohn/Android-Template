package com.androidtemplate.engine.debug;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;

import com.androidtemplate.engine.utils.TCalendarUtils;

public class TFileLog {
	private static final String TAG = "FILE_LOG";
	
	private static final String FILENAME = "logfile.txt";
	
	private Context mContext;
	public TFileLog(Context context) {
		mContext = context;
	}
	
	public void d(String tag, String message) {
		String time = getTime();
		writeToFile("[" + time + " DEBUG] " + " [" + tag + "] " + message);
	}
	
	public void e(String tag, String message) {
		String time = getTime();
		writeToFile("[" + time + " ERROR] " + " [" + tag + "] " + message);
	}
	
	public void write(String message) {
		writeToFile(message);
	}
	
	public List<String> getLogsFromFile() {
		BufferedReader reader = null;
		
		try {
			reader = getReader();
			List<String> logs = new ArrayList<String>();
			String line = null;
			while((line = reader.readLine()) != null) {
				logs.add(line);
			}
			
			return logs;
		} catch(FileNotFoundException e) {
			if(Log.DEBUG_MODE) e.printStackTrace();
		} catch (IOException e) {
			if(Log.DEBUG_MODE) e.printStackTrace();
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					if(Log.DEBUG_MODE) e.printStackTrace();
				}
			}
		}
		
		return Collections.emptyList();
	}
	
	/** Sends the log file to a server at the specified ip using sckets */
	public void sendFileToServer(String host, short port) {
		Socket socket = null;
		FileInputStream is = null;
		
		try {
			Log.d(TAG, "Creating socket!");
			
			socket = new Socket(host, port);
			OutputStream stream = socket.getOutputStream();
			
			Log.d(TAG, "Creating file input steam for temp file!");
			
			is = new FileInputStream(new File(mContext.getCacheDir(), FILENAME));
			
			// Write file to socket
			byte buffer[] = new byte[1024];
			
			int count;
			while((count = (is.read(buffer))) != -1) {
				stream.write(buffer, 0, count);
			}
		} catch (IOException e) {
			if(Log.DEBUG_MODE) e.printStackTrace();
		} finally {
			if(socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					if(Log.DEBUG_MODE) e.printStackTrace();
				}
			}

			if(is != null) {
				try {
					is.close();
				} catch (IOException e) {
					if(Log.DEBUG_MODE) e.printStackTrace();
				}
			}
		}
	}
	
	// Writes a single line of text to the log file
	private void writeToFile(String data) {
		OutputStreamWriter file = null;
		try {
			file = getFile();
			
			if(file != null) {
				file.write(data);
				file.write("\n");
				file.flush();
			}
		} catch (IOException e) {
			if(Log.DEBUG_MODE) e.printStackTrace();
		} finally {
			if(file != null) {
				try {
					file.close();
				} catch (IOException e) {
					if(Log.DEBUG_MODE) e.printStackTrace();
				}
			}
		}
	}
	
	private BufferedReader getReader() throws FileNotFoundException {
		FileInputStream fis = mContext.openFileInput(FILENAME);
		
		return new BufferedReader(new InputStreamReader(fis));
	}
	
	private OutputStreamWriter getFile() throws IOException {
		FileOutputStream fs = mContext.openFileOutput(FILENAME, Context.MODE_APPEND);
		
		return new OutputStreamWriter(fs);
	}
	
	private String getTime() {
		return TCalendarUtils.getFormatedDate(TCalendarUtils.getTimeMillis()).toString();
	}
	
	private static TFileLog instance;
	public static TFileLog getLog(Context context) {
		if(!Log.DEBUG_MODE) return null;
		
		if(instance == null) {
			instance = new TFileLog(context);
		}
		
		return instance;
	}
}
