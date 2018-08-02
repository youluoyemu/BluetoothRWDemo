package com.example.admin.bluetoothrwdemo.presenter;

import com.example.admin.bluetoothrwdemo.bean.AlgorithmSettings;
import com.example.admin.bluetoothrwdemo.model.BluetoothManager;
import com.example.admin.bluetoothrwdemo.model.IBluetoothModel;
import com.example.admin.bluetoothrwdemo.ui.fragment.ISettingsView;

public class SettingsPresenterImpl implements ISettingsPresenter {

	private ISettingsView mSettingsView;
	private IBluetoothModel mBluetoothModel;

	public SettingsPresenterImpl(ISettingsView settingsView) {
		mSettingsView = settingsView;
		mBluetoothModel = BluetoothManager.getInstance();
	}

	@Override
	public void setPower(String power) {
		mBluetoothModel.setPower(power, new OnSetPowerCallback());
	}

	@Override
	public void setProfile(String profile) {
		mBluetoothModel.setRFProfile(profile, new OnSetProfileCallback());
	}

	@Override
	public void setRegion(String region) {
		mBluetoothModel.setRegion(region, new OnSetRegionCallback());
	}

	@Override
	public void setSkipFrequencyTime(String openTime, String closeTime) {

	}

	@Override
	public void setFrequencyPoint(String frequencyPoint) {

	}

	@Override
	public void setAlgorithm(AlgorithmSettings algorithmSettings) {

	}

	private class OnSetPowerCallback implements IBluetoothModel.OnBluetoothReceiveCallback {

		@Override
		public void onBluetoothReceive(String result) {
			mSettingsView.displayResultMsg(result, true);
		}
	}

	private class OnSetProfileCallback implements IBluetoothModel.OnBluetoothReceiveCallback {

		@Override
		public void onBluetoothReceive(String result) {
			mSettingsView.displayResultMsg(result, false);
		}
	}

	private class OnSetRegionCallback implements IBluetoothModel.OnBluetoothReceiveCallback {

		@Override
		public void onBluetoothReceive(String result) {
			mSettingsView.displayResultMsg(result, false);
		}
	}
}
