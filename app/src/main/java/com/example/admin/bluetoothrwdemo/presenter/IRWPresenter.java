package com.example.admin.bluetoothrwdemo.presenter;

public interface IRWPresenter {

	/**
	 * 读取数据
	 * @param area 标签区域
	 * @param addressStart 起始地址
	 * @param length 长度
	 */
	void readData(String area, String addressStart, String length);

	/**
	 * 写入数据
	 * @param area 标签区域
	 * @param data 数据
	 * @param addressStart 起始地址
	 * @param length 长度
	 */
	void writeData(String area, String data, String addressStart, String length);

	/**
	 * 销毁数据
	 * @param area 标签区域
	 * @param addressStart 起始地址
	 * @param length 长度
	 */
	void destroyData(String area, String addressStart, String length);
}
