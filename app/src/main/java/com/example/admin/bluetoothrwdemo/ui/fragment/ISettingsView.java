package com.example.admin.bluetoothrwdemo.ui.fragment;

public interface ISettingsView {
	/**
	 * 展示设置结果信息
	 *
	 * @param msg 结果信息
	 * @param isSetPower 是否是设置功率的结果
	 */
	void displayResultMsg(String msg, boolean isSetPower);
}
