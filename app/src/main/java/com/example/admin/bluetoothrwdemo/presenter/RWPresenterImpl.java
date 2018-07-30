package com.example.admin.bluetoothrwdemo.presenter;

import com.example.admin.bluetoothrwdemo.model.IBluetoothModel;
import com.example.admin.bluetoothrwdemo.ui.fragment.IRWView;

public class RWPresenterImpl implements IRWPresenter {

	private IRWView mRWView;
	private IBluetoothModel mBluetoothModel;

	public RWPresenterImpl(IRWView rwView){
		mRWView = rwView;
	}

	@Override
	public void readData(String area, String addressStart, String length) {

	}

	@Override
	public void writeData(String area, String data, String addressStart, String length) {

	}

	@Override
	public void destroyData(String area, String addressStart, String length) {

	}
}
