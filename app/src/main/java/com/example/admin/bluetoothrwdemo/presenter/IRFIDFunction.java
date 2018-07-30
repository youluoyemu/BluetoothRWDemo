package com.example.admin.bluetoothrwdemo.presenter;

import com.example.admin.bluetoothrwdemo.bean.AlgorithmSettings;
import com.example.admin.bluetoothrwdemo.bean.TagInfo;

import java.util.List;

public interface IRFIDFunction {

	boolean initBluetooth();

	void connectBluetooth(String address, OnBluetoothInitCallback callback);

	void startCheckTag(List<TagInfo> tagInfoList, OnTagInfoUpdateCallback callback);

	void stopCheckTag();

	String getPower();

	String readData(String area, String addressStart, String length);

	boolean writeData(String area, String data, String addressStart, String length);

	boolean destroyData(String area, String addressStart, String length);

	boolean setPower(String power);

	boolean setProfile(String profile);

	boolean setArea(String area);

	boolean setSkipFrequencyTime(String openTime, String closeTime);

	boolean setFrequencyPoint(String frequencyPoint);

	boolean setAlgorithm(AlgorithmSettings algorithmSettings);

	interface OnTagInfoUpdateCallback {
		void onTagInfoUpdate();
	}

	interface OnBluetoothInitCallback {
		void onBluetoothInit(boolean isInitial);
	}
}
