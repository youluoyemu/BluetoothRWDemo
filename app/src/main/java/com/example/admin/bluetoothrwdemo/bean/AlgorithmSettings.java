package com.example.admin.bluetoothrwdemo.bean;

public class AlgorithmSettings {
	private String mProperty;
	private String mReverse;
	private String mQStart;
	private String mQRangeStart;
	private String mQRangeEnd;
	private String mSession;

	public AlgorithmSettings(String mProperty, String mReverse, String mQStart, String mQRangeStart, String mQRangeEnd, String mSession) {
		this.mProperty = mProperty;
		this.mReverse = mReverse;
		this.mQStart = mQStart;
		this.mQRangeStart = mQRangeStart;
		this.mQRangeEnd = mQRangeEnd;
		this.mSession = mSession;
	}
}
