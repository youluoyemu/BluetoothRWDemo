package com.example.admin.bluetoothrwdemo.presenter;

import com.example.admin.bluetoothrwdemo.model.BluetoothManager;
import com.example.admin.bluetoothrwdemo.model.IBluetoothModel;
import com.example.admin.bluetoothrwdemo.ui.IMainView;

import java.util.List;

public class MainPresenterImpl implements IMainPresenter, IBluetoothModel.OnBluetoothConnectedCallback {

	private IMainView mMainView;
	private IBluetoothModel mBluetoothManager;

	private List<String> mAddressList;


	public MainPresenterImpl(IMainView mainView) {
		mMainView = mainView;
	}

	@Override
	public void onCreate() {
		mBluetoothManager = BluetoothManager.getInstance();
		mAddressList = mBluetoothManager.getAddressList();
		mMainView.showSpinnerList(mAddressList);
	}

	@Override
	public void updateAddressList(String address) {
		if (address == null) {
			mBluetoothManager.addBondedDevAddress();
		} else {
			mBluetoothManager.addAddress(address);
		}
		mMainView.refreshSpinnerList();
	}

	@Override
	public void onSpinnerItemSelected(int pos) {
		String[] splitAddress = mAddressList.get(pos).split("：");
		mMainView.setAddressText(splitAddress[1]);
	}

	@Override
	public void connectBluetooth(String address) {
		mBluetoothManager.connectBluetooth(address, this);
	}

	@Override
	public void onDestroy() {
		mBluetoothManager.disconnectBluetooth();
	}

	@Override
	public void onBluetoothConnected(boolean isConnected) {
		if (isConnected) {
			mMainView.startFunctionActivity();
		} else {
			mMainView.displayConnFailedMessage();
		}
		mMainView.hideProgressBar();
	}
}
