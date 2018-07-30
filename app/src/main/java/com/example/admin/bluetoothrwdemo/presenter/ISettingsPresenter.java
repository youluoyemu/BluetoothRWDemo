package com.example.admin.bluetoothrwdemo.presenter;

import com.example.admin.bluetoothrwdemo.bean.AlgorithmSettings;

public interface ISettingsPresenter {
	void setPower(String power);

	void setProfile(String profile);

	void setArea(String area);

	void setSkipFrequencyTime(String openTime, String closeTime);

	void setFrequencyPoint(String frequencyPoint);

	void setAlgorithm(AlgorithmSettings algorithmSettings);
}
