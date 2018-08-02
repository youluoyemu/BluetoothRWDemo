package com.example.admin.bluetoothrwdemo.model;

import java.util.List;

public interface IBluetoothModel {
	/**
	 * 添加蓝牙地址
	 *
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
	 *
	 * @param address  蓝牙地址
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
	 *
	 * @param callback 回调函数
	 */
	void receiveBluetoothData(OnBluetoothReceiveCallback callback);

	/**
	 * 停止接收蓝牙返回数据
	 */
	void stopReceiveBluetoothData();

	/**
	 * 获取功率
	 *
	 * @param callback 回调函数
	 */
	void getPower(OnBluetoothReceiveCallback callback);

	/**
	 * 断开蓝牙连接
	 */
	void disconnectBluetooth();

	/**
	 * 从标签数据区域读取数据
	 *
	 * @param area         区域
	 * @param addressStart 起始地址
	 * @param length       长度
	 * @param callback     回调函数
	 */
	void readTagDataArea(String area, String addressStart, String length, OnBluetoothReceiveCallback callback);

	/**
	 * 写入数据到标签数据区域
	 *
	 * @param area         区域
	 * @param addressStart 起始地址
	 * @param length       长度
	 * @param writeData    写入数据
	 * @param callback     回调函数
	 */
	void writeTagDataArea(String area, String addressStart, String length, String writeData, OnBluetoothReceiveCallback callback);

	/**
	 * 从标签数据区域擦除数据
	 *
	 * @param area         区域
	 * @param addressStart 起始地址
	 * @param length       长度
	 * @param callback     回调函数
	 */
	void destroyTagDataArea(String area, String addressStart, String length, OnBluetoothReceiveCallback callback);

	/**
	 * 设置功率
	 *
	 * @param power    功率
	 * @param callback 回调接口
	 */
	void setPower(String power, OnBluetoothReceiveCallback callback);

	/**
	 * 设置推荐 RF 链路组合
	 * @param profile RF 链路设置
	 * @param callback 回调接口
	 */
	void setRFProfile(String profile, OnBluetoothReceiveCallback callback);

	/**
	 * 设置区域
	 *
	 * @param region   区域
	 * @param callback 回调接口
	 */
	void setRegion(String region, OnBluetoothReceiveCallback callback);

	/**
	 * 蓝牙连接回调接口
	 */
	interface OnBluetoothConnectedCallback {
		void onBluetoothConnected(boolean isConnected);
	}

	/**
	 * 蓝牙接收数据回调接口
	 */
	interface OnBluetoothReceiveCallback {
		void onBluetoothReceive(String result);
	}
}
