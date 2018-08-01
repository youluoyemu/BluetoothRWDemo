package com.example.admin.bluetoothrwdemo.ui.fragment;

public interface IRWView {
	/**
	 * 设置读取结果
	 *
	 * @param result 读取结果
	 */
	void setReadResultText(String result);

	/**
	 * 设置写入结果
	 *
	 * @param result 写入结果
	 */
	void setWriteResultText(String result);

	/**
	 * 设置销毁结果
	 *
	 * @param result 销毁结果
	 */
	void setDestroyResultText(String result);
}
