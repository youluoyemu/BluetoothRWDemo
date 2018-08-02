package com.example.admin.bluetoothrwdemo.presenter;

import com.example.admin.bluetoothrwdemo.bean.AlgorithmSettings;

public interface ISettingsPresenter {
	/**
	 * 设置发射功率
	 *
	 * @param power 发射功率
	 */
	void setPower(String power);

	/**
	 * 设置配置信息
	 *
	 * @param profile 配置
	 */
	void setProfile(String profile);

	/**
	 * 设置区域
	 *
	 * @param region 区域类型
	 */
	void setRegion(String region);

	/**
	 * 设置跳频时间
	 *
	 * @param openTime  开启时间
	 * @param closeTime 关闭时间
	 */
	void setSkipFrequencyTime(String openTime, String closeTime);

	/**
	 * 设置频点
	 *
	 * @param frequencyPoint 频点
	 */
	void setFrequencyPoint(String frequencyPoint);

	/**
	 * 设置算法相关
	 *
	 * @param algorithmSettings 算法设置
	 */
	void setAlgorithm(AlgorithmSettings algorithmSettings);
}
