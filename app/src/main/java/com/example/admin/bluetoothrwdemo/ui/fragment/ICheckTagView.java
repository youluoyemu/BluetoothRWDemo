package com.example.admin.bluetoothrwdemo.ui.fragment;

import com.example.admin.bluetoothrwdemo.bean.TagInfo;

import java.util.List;

public interface ICheckTagView {
	void showReceiveData(String data);

	void showTagInfoList(List<TagInfo> tagInfoList);

	void refreshTagInfoList();

	void refreshBtnState();
}
