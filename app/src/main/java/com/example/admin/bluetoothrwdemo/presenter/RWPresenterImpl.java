package com.example.admin.bluetoothrwdemo.presenter;

import com.example.admin.bluetoothrwdemo.model.BluetoothManager;
import com.example.admin.bluetoothrwdemo.model.IBluetoothModel;
import com.example.admin.bluetoothrwdemo.ui.fragment.IRWView;

public class RWPresenterImpl implements IRWPresenter {

	private IRWView mRWView;
	private IBluetoothModel mBluetoothModel;

	public RWPresenterImpl(IRWView rwView) {
		mRWView = rwView;
		mBluetoothModel = BluetoothManager.getInstance();
	}

	@Override
	public void readData(String area, String addressStart, String length) {
		mBluetoothModel.readTagDataArea(area, addressStart, length, new OnReadTagDataCallback());
	}

	@Override
	public void writeData(String area, String data, String addressStart, String length) {
		mBluetoothModel.writeTagDataArea(area, addressStart, length, data, new OnWriteTagDataCallback());
	}

	@Override
	public void destroyData(String area, String addressStart, String length) {

	}

	private class OnReadTagDataCallback implements IBluetoothModel.OnBluetoothReceiveCallback {

		@Override
		public void onBluetoothReceive(String result) {
			mRWView.setReadResultText(result);
		}
	}

	private class OnWriteTagDataCallback implements IBluetoothModel.OnBluetoothReceiveCallback {

		@Override
		public void onBluetoothReceive(String result) {

		}
	}
}
