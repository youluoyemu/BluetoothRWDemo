package com.example.admin.bluetoothrwdemo.ui;

import java.util.List;

public interface IMainView {
	void showSpinnerList(List<String> addressList);

	void setAddressText(String address);

	void refreshSpinnerList();

	void startFunctionActivity();

	void displayConnFailedMessage();

	void hideProgressBar();
}
