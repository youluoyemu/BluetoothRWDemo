package com.example.admin.bluetoothrwdemo.model;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.text.TextUtils;
import android.util.Log;

import com.example.admin.bluetoothrwdemo.bean.TagInfo;
import com.google.gson.Gson;
import com.thinta.function.frame.RECmd;
import com.thinta.function.frame.ReaderFrame;
import com.thinta.function.frame.ZECmd;
import com.thinta.function.untils.DataConvert;
import com.thinta.product.zlibv2.frame.FramePT02;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BluetoothManager implements IBluetoothModel {

	// 常量
	private static final String TAG = "BluetoothManager";
	private static final java.util.UUID MY_UUID = java.util.UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private static final int STATE_CONNECTED = 0;
	private static final int STATE_DISCONNECTED = 1;

	// 静态变量
	private static BluetoothManager INSTANCE;
	private static int frameIndex = 0x00;
	private static String times = "0001";

	// 成员变量
	private List<String> mAddressList;
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothSocket mSocket;
	private OnBluetoothConnectedCallback mConnectedCallback;
	private OnBluetoothReceiveCallback mReceiveCallback;
	private ConnectThread mConnectThread;
	private ReceiveThread mReceiveThread;
	private volatile int mState;
	private int mOrder = 1;

	private BluetoothManager() {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		initAddressList();
	}

	public static BluetoothManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new BluetoothManager();
		}
		return INSTANCE;
	}

	private void initAddressList() {
		mAddressList = new ArrayList<>();
		mAddressList.add("蓝牙列表");
	}

	public void addBondedDevAddress() {
		Set<BluetoothDevice> bondedDevices = mBluetoothAdapter.getBondedDevices();
		if (bondedDevices.size() == 0) {
			return;
		}
		for (BluetoothDevice device : bondedDevices) {
			mAddressList.add(device.getName() + "：" + device.getAddress());
		}
	}

	public void addAddress(String address) {
		mAddressList.add(address);
	}

	public List<String> getAddressList() {
		return mAddressList;
	}

	@Override
	public synchronized void connectBluetooth(String address, OnBluetoothConnectedCallback callback) {
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		if (mReceiveThread != null) {
			mReceiveThread.cancel();
			mReceiveThread = null;
		}
		mConnectedCallback = callback;
		BluetoothDevice remoteDevice = mBluetoothAdapter.getRemoteDevice(address);
		mConnectThread = new ConnectThread(remoteDevice);
		mConnectThread.start();
	}

	@Override
	public synchronized void startSerialCheckTag(OnBluetoothReceiveCallback callback) {
		if (mReceiveThread != null) {
			mReceiveThread = null;
		}
		mReceiveCallback = callback;
		mReceiveThread = new ReceiveThread();
		byte[] sendFrame = createSendFrame(ZECmd.UHF_COM.getCmd(), RECmd.SEARCH_TAG.getCmdIndex(), times);
		mReceiveThread.write(sendFrame);
		mReceiveThread.start();
	}

	@Override
	public synchronized void stopSerialCheckTag(OnBluetoothReceiveCallback callback) {
		if (mReceiveThread != null) {
			mReceiveThread = null;
		}
		mReceiveCallback = callback;
		mReceiveThread = new ReceiveThread();
		byte[] sendFrame = createSendFrame(ZECmd.UHF_COM.getCmd(), RECmd.END_SEARCH_TAG.getCmdIndex(), "");
		mReceiveThread.write(sendFrame);
		mReceiveThread.start();
	}

	@Override
	public synchronized void receiveBluetoothData(OnBluetoothReceiveCallback callback) {
		if (mConnectThread != null) {
//			mConnectThread.cancel();
			mConnectThread = null;
		}

		if (mReceiveThread != null) {
//			mReceiveThread.cancel();
			mReceiveThread = null;
		}
		mReceiveCallback = callback;
		mReceiveThread = new ReceiveThread();
		FramePT02 sendFrame = new FramePT02();
		sendFrame.setControl(0x2411);
		byte[] dataArrays = new byte[3];
		int timeout = 100;
		dataArrays[0] = (byte) 0x01;
		dataArrays[1] = (byte) (timeout & 0xFF);
		dataArrays[2] = (byte) (timeout >> 8 & 0xFF);
		sendFrame.setDatas(dataArrays);
		byte[] send = sendFrame.toBytes();
		mReceiveThread.write(send);
		mReceiveThread.start();
	}

	@Override
	public synchronized void stopReceiveBluetoothData() {
		mState = STATE_DISCONNECTED;
		Log.d(TAG, "stop receive: ==============");
	}

	@Override
	public synchronized void getPower(OnBluetoothReceiveCallback callback) {
		mReceiveCallback = callback;
		byte[] sendFrame = createSendFrame(ZECmd.UHF_COM.getCmd(), RECmd.GET_SEND_POWER.getCmdIndex(), "");
		if (mReceiveThread == null) {
			mReceiveThread = new ReceiveThread();
			mReceiveThread.write(sendFrame);
			mReceiveThread.start();
		} else {
			mReceiveThread.write(sendFrame);
		}
	}

	@Override
	public void disconnectBluetooth() {
		if (mSocket != null && mSocket.isConnected()) {
			try {
				mSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private byte[] createSendFrame(String zCmd, String rCmd, String data) {
		if (frameIndex >= 16) frameIndex = 0x00;
		String readerFrame = new ReaderFrame(rCmd, data).getFrameString();
		String zFrameCache = "CA" + zCmd + DataConvert.toHexString(frameIndex, 1) +
				DataConvert.toHexString(readerFrame.length() / 2, 1) + "00CA" + readerFrame;
		String zFrameSumVale = DataConvert.getSumValue(zFrameCache);
		String zFrame = zFrameCache + zFrameSumVale + "AC";
		return DataConvert.toBytes(zFrame);
	}

	private String parseRecFrame(String recFrame) {
		if (TextUtils.isEmpty(recFrame)) {
			return null;
		}
		String result = null;
		if (recFrame.substring(2, 6).equals("8811")
				&& recFrame.substring(22, 24).equals("13")) {
			String power = recFrame.substring(28, 32);
			result = Integer.parseInt(power, 16) / 100 + "";
		} else if (recFrame.substring(2, 6).equals("A411")) {
			String data = recFrame.substring(18, recFrame.length() - 4);
			List<TagInfo> tagInfoList = new ArrayList<>();
			int count = data.length() / 34 + 1;
			TagInfo tagInfo = new TagInfo();
			String orderNumber = data.substring(0, 32);
			tagInfo.setOrderNumber(orderNumber.substring(0, 24));
			tagInfoList.add(tagInfo);
			for (int i = 1; i < count; i++) {
				if (data.substring(i * 34 - 2, i * 34).equals("10")) {
					TagInfo info = new TagInfo();
					orderNumber = data.substring(i * 34, (i + 1) * 34 - 2);
					info.setOrderNumber(orderNumber.substring(0, 24));
					tagInfoList.add(info);
				}
			}
			Gson gson = new Gson();
			result = gson.toJson(tagInfoList);
		} else if (recFrame.substring(2, 6).equals("8811")
				&& recFrame.substring(22, 24).equals("83")) {
			TagInfo tagInfo = new TagInfo();
			tagInfo.setOrderNumber(mOrder++ + "");
			String tagId = recFrame.substring(28, 52);
			tagInfo.setEpcTid(tagId);
			tagInfo.setTimes(Integer.parseInt(times, 16) + "");
			String rssiStr = recFrame.substring(60, 64);
			double rssi = Integer.parseInt(rssiStr, 16) - Math.pow(16, 4);
			tagInfo.setRssi(rssi / 10 + "");
			Gson gson = new Gson();
			result = gson.toJson(tagInfo);
		} else if (recFrame.substring(2, 6).equals("8811")
				&& recFrame.substring(16, 18).equals("8D")) {
			return recFrame.substring(18, 20).equals("01") + "";
		} else {
			return recFrame;
		}
		return result;
	}

	private class ConnectThread extends Thread {

		ConnectThread(BluetoothDevice device) {
			try {
				mSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			try {
				mSocket.connect();
				mConnectedCallback.onBluetoothConnected(true);
			} catch (IOException e) {
				e.printStackTrace();
				try {
					mSocket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				mConnectedCallback.onBluetoothConnected(false);
			}
		}

		public void cancel() {
			try {
				mSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class ReceiveThread extends Thread {

		private InputStream mInputStream;
		private OutputStream mOutputStream;

		public ReceiveThread() {
			try {
				mInputStream = mSocket.getInputStream();
				mOutputStream = mSocket.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			try {
				Log.d(TAG, "start receive: ==============");
				mState = STATE_CONNECTED;
				byte[] buffer = new byte[1024];
				int len;
				while (mState == STATE_CONNECTED) {
					len = mInputStream.read(buffer);
					if (len != -1) {
						String recFrame = DataConvert.toHexString(buffer, 0, len);
						String result = parseRecFrame(recFrame);
						mReceiveCallback.onBluetoothReceive(result);
						Log.d(TAG, "length:========= " + len);
						Log.d(TAG, "receive frame:============ " + recFrame);
						Log.d(TAG, "receive result:============ " + result);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				mState = STATE_DISCONNECTED;
				mReceiveCallback.onBluetoothReceive(null);
			}
		}

		public void cancel() {
			try {
				mSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void write(byte[] buffer) {
			try {
				mOutputStream.write(buffer);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
