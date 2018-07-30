package com.example.admin.bluetoothrwdemo.presenter;

import com.example.admin.bluetoothrwdemo.model.BluetoothManager;
import com.example.admin.bluetoothrwdemo.model.IBluetoothModel;
import com.example.admin.bluetoothrwdemo.ui.activity.IFunctionView;

public class FunctionPresenterImpl implements IFunctionPresenter, IBluetoothModel.OnBluetoothReceiveCallback {

	private IFunctionView mFunctionView;
	private IBluetoothModel mBluetoothModel;

	public FunctionPresenterImpl(IFunctionView functionView){
		mFunctionView = functionView;
		mBluetoothModel = BluetoothManager.getInstance();
	}

	@Override
	public void onCreate() {
		mBluetoothModel.getPower(this);
	}

	@Override
	public void onBluetoothReceive(String result) {
		mFunctionView.setPowerText(result);
	}
}
