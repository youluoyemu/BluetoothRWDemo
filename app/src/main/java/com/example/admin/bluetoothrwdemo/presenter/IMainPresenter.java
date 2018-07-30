package com.example.admin.bluetoothrwdemo.presenter;

public interface IMainPresenter {
	/**
	 * 初始化数据
	 */
	void onCreate();

	/**
	 * 更新蓝牙地址列表
	 * @param address 蓝牙地址
	 */
	void updateAddressList(String address);

	/**
	 * 选择蓝牙列表 item
	 * @param pos item 位置
	 */
	void onSpinnerItemSelected(int pos);

	/**
	 * 连接蓝牙设备
	 * @param address 设备蓝牙地址
	 */
	void connectBluetooth(String address);

	/**
	 * 销毁，回收资源
	 */
	void onDestroy();

}
