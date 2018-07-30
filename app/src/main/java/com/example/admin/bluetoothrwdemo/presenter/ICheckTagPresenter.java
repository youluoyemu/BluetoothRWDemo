package com.example.admin.bluetoothrwdemo.presenter;

public interface ICheckTagPresenter {
	/**
	 * 初始化数据
	 */
	void onCreate();

	/**
	 * 开始盘点
	 */
	void startCheckTag();

	/**
	 * 停止盘点
	 */
	void stopCheckTag();

	/**
	 * 清除已盘点标签信息
	 */
	void clearCheckedTagInfo();
}
