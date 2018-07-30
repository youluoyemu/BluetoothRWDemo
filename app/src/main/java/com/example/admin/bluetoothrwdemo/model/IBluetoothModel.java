package com.example.admin.bluetoothrwdemo.model;

import java.util.List;

public interface IBluetoothModel {
	/**
	 * 添加蓝牙地址
	 * @param address 蓝牙地址
	 */
	void addAddress(String address);

	/**
	 * 添加所有配对设备的地址
	 */
	void addBondedDevAddress();

	List<String> getAddressList();

	/**
	 * 连接蓝牙
	 * @param address 蓝牙地址
	 * @param callback 回调函数
	 */
	void connectBluetooth(String address, OnBluetoothConnectedCallback callback);

	/**
	 * 开始连续盘存标签
	 */
	void startSerialCheckTag(OnBluetoothReceiveCallback callback);

	/**
	 * 停止连续盘存标签
	 */
	void stopSerialCheckTag(OnBluetoothReceiveCallback callback);

	/**
	 * 接收蓝牙返回数据
	 * @param callback 回调函数
	 */
	void receiveBluetoothData(OnBluetoothReceiveCallback callback);

	/**
	 * 停止接收蓝牙返回数据
	 */
	void stopReceiveBluetoothData();

	/**
	 * 获取功率
	 * @param callback 回调函数
	 */
	void getPower(OnBluetoothReceiveCallback callback);

	/**
	 * 断开蓝牙连接
	 */
	void disconnectBluetooth();

	/**
	 * 蓝牙连接回调接口
	 */
	interface OnBluetoothConnectedCallback {
		void onBluetoothConnected(boolean isConnected);
	}

	/**
	 * 蓝牙接收数据回调接口
	 */
	interface OnBluetoothReceiveCallback{
		void onBluetoothReceive(String result);
	}
}
