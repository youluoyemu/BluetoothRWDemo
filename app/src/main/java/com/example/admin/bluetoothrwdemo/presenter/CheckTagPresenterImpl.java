package com.example.admin.bluetoothrwdemo.presenter;

import android.nfc.Tag;

import com.example.admin.bluetoothrwdemo.bean.TagInfo;
import com.example.admin.bluetoothrwdemo.model.BluetoothManager;
import com.example.admin.bluetoothrwdemo.model.IBluetoothModel;
import com.example.admin.bluetoothrwdemo.ui.fragment.ICheckTagView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class CheckTagPresenterImpl implements ICheckTagPresenter, IBluetoothModel.OnBluetoothReceiveCallback {

	private ICheckTagView mCheckTagView;
	private IBluetoothModel mBluetoothModel;

	private static List<TagInfo> mTagInfoList;

	public CheckTagPresenterImpl(ICheckTagView checkTagView) {
		mCheckTagView = checkTagView;
		mBluetoothModel = BluetoothManager.getInstance();
		mTagInfoList = new ArrayList<>();
	}

	@Override
	public void onCreate() {
		mCheckTagView.showTagInfoList(mTagInfoList);
	}

	@Override
	public void startCheckTag() {
//		mBluetoothModel.receiveBluetoothData(this);
		mBluetoothModel.startSerialCheckTag(this);
	}

	@Override
	public void stopCheckTag() {
//		mBluetoothModel.stopReceiveBluetoothData();
		mBluetoothModel.stopSerialCheckTag(new StopSerialCheckTagCallback());
	}

	@Override
	public void clearCheckedTagInfo() {
		mTagInfoList.clear();
		mCheckTagView.refreshTagInfoList();
	}

	@Override
	public void onBluetoothReceive(String result) {
		Gson gson = new Gson();
//		List<TagInfo> tagInfoList = gson.fromJson(result, new TypeToken<List<TagInfo>>() {
//		}.getType());
//		mTagInfoList.addAll(tagInfoList);
//		mCheckTagView.refreshTagInfoList();
		TagInfo tagInfo = gson.fromJson(result, TagInfo.class);
		mTagInfoList.add(tagInfo);
		mCheckTagView.refreshTagInfoList();
	}

	private class StopSerialCheckTagCallback implements IBluetoothModel.OnBluetoothReceiveCallback {

		@Override
		public void onBluetoothReceive(String result) {
			if (Boolean.parseBoolean(result)) mCheckTagView.refreshBtnState();
		}
	}
}
