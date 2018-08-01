package com.example.admin.bluetoothrwdemo.ui.fragment;

import com.example.admin.bluetoothrwdemo.bean.TagInfo;

import java.util.List;

public interface ICheckTagView {

	/**
	 * 展示标签信息列表
	 * @param tagInfoList 标签信息集合
	 */
	void showTagInfoList(List<TagInfo> tagInfoList);

	/**
	 * 刷新标签信息列表
	 */
	void refreshTagInfoList();

	/**
	 * 刷新底部按钮状态
	 */
	void refreshBtnState();
}
