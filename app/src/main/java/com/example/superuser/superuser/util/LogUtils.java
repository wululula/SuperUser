package com.example.superuser.superuser.util;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class LogUtils {
	private static final String TAG = "LogUtils";
	private static final boolean DEBUG = true;
	private static final int MAX_LOG_FILES = 3;// 文件保存个数

	private static final String LOG_FILE_HEAD = "Log-";// 文件前缀
	private static final String LOG_FILE_FORMAT = ".txt";// 文件保存格式
	private static final String ENTER = "\r\n";// 换行符

	private final SimpleDateFormat mFileNameFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH-mm-ss");// 日志名称格式
	private final SimpleDateFormat mLogTimeFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");// 日志时间格式

	private static final String LOG_FOLDER_PATH = Environment
			.getExternalStorageDirectory() + "/SuperUser";

	private String mCurLogFileName = null;
	private FileWriter mFileWriter = null;

	private static LogUtils gInstance = null;

	public static LogUtils getInstance() {
		synchronized (LogUtils.class) {
			if (gInstance == null) {
				gInstance = new LogUtils();
			}
			return gInstance;
		}
	}

	private LogUtils() {
		Log.e(TAG, "LogUtils()");
	}

	public String getCurLogFilePath() {
		return mCurLogFileName;
	}

	public boolean start() {
		mCurLogFileName = null;

		if (createLogFolder() == false) {
			return false;
		}

		if (createNewLogFile() == false) {
			return false;
		}

		if (openFileWriter(mCurLogFileName) == false) {
			return false;
		}

		return true;
	}

	public boolean stop() {
		if (closeFileWriter() == false) {
			return false;
		}

		return true;
	}

	public void addLog(String log) {

		if (mFileWriter != null) {
			try {
				String time = mLogTimeFormat.format(new Date()) + " ==> ";
				mFileWriter.write(time + TAG +" ==> "+log + ENTER);
				mFileWriter.flush();
			} catch (IOException e) {
				Log.e(TAG, "addLog ==> IOException");
				errorMessage(e);
			}
		}
	}

	private boolean openFileWriter(String fileName) {
		closeFileWriter();

		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			mFileWriter = new FileWriter(fileName, true);
		} catch (IOException e) {
			Log.e(TAG, "openFileWriter ==> IOException");
			errorMessage(e);
			return false;
		}

		return true;
	}

	private boolean closeFileWriter() {
		if (mFileWriter != null) {
			try {
				mFileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, "closeFileWriter ==> IOException");
				errorMessage(e);
				return false;
			}
		}

		return true;
	}

	private boolean createLogFolder() {
		File folder = new File(LOG_FOLDER_PATH);
		if (!folder.exists()) {
			boolean mkdir = folder.mkdir();
			if (mkdir == false) {
				mkdir = folder.mkdirs();
			}
			Log.i(TAG, "createLogFolder ==> mkdir == " + mkdir);
			return mkdir;
		}

		return true;
	}

	@SuppressLint("NewApi")
	private boolean createNewLogFile() {

		deleteExpiredLogFile();

		String logFileName = LOG_FILE_HEAD + mFileNameFormat.format(new Date())
				+ LOG_FILE_FORMAT;// 日志文件名称
		mCurLogFileName = LOG_FOLDER_PATH + File.separator + logFileName;
		File newFile = new File(LOG_FOLDER_PATH, logFileName);
		if (!newFile.exists()) {
			try {
				newFile.createNewFile();
				newFile.setWritable(Boolean.TRUE);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, "createNewLogFile ==> IOException");
				errorMessage(e);
				return false;
			}
		}

		return true;
	}

	private void deleteExpiredLogFile() {
		File folder = new File(LOG_FOLDER_PATH);
		if (folder.isDirectory()) {
			File[] allFiles = folder.listFiles();

			Log.i(TAG, "allFiles.length == " + allFiles.length);

			ArrayList<String> files = new ArrayList<String>();
			for (int i = 0; i < allFiles.length; i++) {
				File logFile = allFiles[i];
				String name = logFile.getName();

				// Log.i(TAG, "name == " + name);

				if (isLogFile(name)) {
					files.add(name);
				}
			}

			Collections.sort(files);
			for (String file : files) {
				Log.i(TAG, "after sort file == " + file);
			}

			for (int i = 0; i < files.size() - MAX_LOG_FILES + 1; i++) {
				String name = files.get(i);
				Log.e(TAG, "Delete file == " + name);
				File file = new File(LOG_FOLDER_PATH, name);
				file.delete();
			}
		}
	}

	private boolean isLogFile(String name) {
		if (name == null || name.length() < 1) {
			return false;
		}

		if (name.startsWith(LOG_FILE_HEAD) && name.endsWith(LOG_FILE_FORMAT)) {
			return true;
		}
		return false;
	}

	/**
	 * 打印调试LOG
	 *
	 * @param TAG
	 * @param message
	 */
	public static void printLog(String TAG, String message) {
		if (DEBUG) {
			Log.i(TAG, message);
		}
	}

	public void errorMessage(Exception e){
		e.printStackTrace();
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw, true));
		String str = sw.toString();
		addLog(str);
	}
}
