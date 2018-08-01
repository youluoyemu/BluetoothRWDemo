package com.example.admin.bluetoothrwdemo.ui;

import java.util.List;

public interface IMainView {
	/**
	 * 显示蓝牙地址列表
	 * @param addressList 蓝牙地址集合
	 */
	void showSpinnerList(List<String> addressList);

	/**
	 * 设置蓝牙地址
	 * @param address 蓝牙地址
	 */
	void setAddressText(String address);

	/**
	 * 刷新蓝牙地址列表
	 */
	void refreshSpinnerList();

	void startFunctionActivity();

	/**
	 * 展示蓝牙连接错误消息
	 */
	void displayConnFailedMessage();

	void hideProgressBar();
}
