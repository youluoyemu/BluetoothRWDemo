package com.example.admin.bluetoothrwdemo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.bluetoothrwdemo.R;
import com.example.admin.bluetoothrwdemo.bean.AlgorithmSettings;
import com.example.admin.bluetoothrwdemo.presenter.ISettingsPresenter;
import com.example.admin.bluetoothrwdemo.presenter.SettingsPresenterImpl;

public class SettingsFragment extends BaseFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener, ISettingsView {

	private static final String TAG = "SettingsFragment";

	private AppCompatSpinner mSpPower;
	private AppCompatSpinner mSpProfile;
	private AppCompatSpinner mSpArea;
	private AppCompatSpinner mSpProperty;
	private AppCompatSpinner mSpReverse;
	private AppCompatSpinner mSpQStart;
	private AppCompatSpinner mSpQRangeStart;
	private AppCompatSpinner mSPQRangeEnd;
	private AppCompatSpinner mSpSession;
	private Button mBtnPower;
	private Button mBtnProfile;
	private Button mBtnArea;
	private Button mBtnSkipTime;
	private Button mBtnFrequencyPoint;
	private Button mBtnAlgorithm;
	private EditText mEtOpenTime;
	private EditText mEtCloseTime;
	private EditText mEtFrequencyPoint;

	private ISettingsPresenter mSettingsPresenter;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSettingsPresenter = new SettingsPresenterImpl(this);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_settings, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView(view);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
		initEvent();
	}

	private void initView(View view) {
		mSpPower = view.findViewById(R.id.sp_setting_power);
		mSpProfile = view.findViewById(R.id.sp_setting_profile);
		mSpArea = view.findViewById(R.id.sp_setting_area);
		mSpProperty = view.findViewById(R.id.sp_setting_property);
		mSpReverse = view.findViewById(R.id.sp_setting_reverse);
		mSpQStart = view.findViewById(R.id.sp_setting_q_start);
		mSpQRangeStart = view.findViewById(R.id.sp_setting_q_range_start);
		mSPQRangeEnd = view.findViewById(R.id.sp_setting_q_range_end);
		mSpSession = view.findViewById(R.id.sp_setting_session);
		mBtnPower = view.findViewById(R.id.btn_setting_power);
		mBtnProfile = view.findViewById(R.id.btn_setting_profile);
		mBtnArea = view.findViewById(R.id.btn_setting_area);
		mBtnSkipTime = view.findViewById(R.id.btn_setting_skip);
		mBtnFrequencyPoint = view.findViewById(R.id.btn_setting_point);
		mBtnAlgorithm = view.findViewById(R.id.btn_algorithm);
		mEtOpenTime = view.findViewById(R.id.et_open_time);
		mEtCloseTime = view.findViewById(R.id.et_close_time);
		mEtFrequencyPoint = view.findViewById(R.id.et_frequency_point);
	}

	private void initData() {
		mSettingsPresenter = new SettingsPresenterImpl(this);
	}

	private void initEvent() {
//		mSpPower.setOnItemSelectedListener(this);
//		mSpProfile.setOnItemSelectedListener(this);
//		mSpArea.setOnItemSelectedListener(this);
//		mSpProperty.setOnItemSelectedListener(this);
//		mSpReverse.setOnItemSelectedListener(this);
//		mSpQStart.setOnItemSelectedListener(this);
//		mSpQRangeStart.setOnItemSelectedListener(this);
//		mSPQRangeEnd.setOnItemSelectedListener(this);
//		mSpSession.setOnItemSelectedListener(this);
		mBtnPower.setOnClickListener(this);
		mBtnProfile.setOnClickListener(this);
		mBtnArea.setOnClickListener(this);
		mBtnSkipTime.setOnClickListener(this);
		mBtnFrequencyPoint.setOnClickListener(this);
		mBtnAlgorithm.setOnClickListener(this);
	}

	@Override
	public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//		switch (adapterView.getId()) {
//			case R.id.sp_setting_power:
////				adapterView.get
//				break;
//			case R.id.sp_setting_profile:
//				break;
//			case R.id.sp_setting_area:
//				break;
//			case R.id.sp_setting_property:
//				break;
//			case R.id.sp_setting_reverse:
//				break;
//			case R.id.sp_setting_q_start:
//				break;
//			case R.id.sp_setting_q_range_start:
//				break;
//			case R.id.sp_setting_q_range_end:
//				break;
//			case R.id.sp_setting_session:
//				break;
//			default:
//				break;
//		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView) {

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_setting_power:
				String power = (String) mSpPower.getSelectedItem();
				mSettingsPresenter.setPower(power);
				break;
			case R.id.btn_setting_profile:
				String profile = (String) mSpProfile.getSelectedItem();
				mSettingsPresenter.setProfile(profile);
				break;
			case R.id.btn_setting_area:
				String area = (String) mSpArea.getSelectedItem();
				mSettingsPresenter.setArea(area);
				break;
			case R.id.btn_setting_skip:
				String openTime = mEtOpenTime.getText().toString().trim();
				String closeTime = mEtCloseTime.getText().toString().trim();
				mSettingsPresenter.setSkipFrequencyTime(openTime, closeTime);
				break;
			case R.id.btn_setting_point:
				String frequencyPoint = mEtFrequencyPoint.getText().toString().trim();
				mSettingsPresenter.setFrequencyPoint(frequencyPoint);
				break;
			case R.id.btn_algorithm:
				String property = (String) mSpProperty.getSelectedItem();
				String reverse = (String) mSpReverse.getSelectedItem();
				String qStart = (String) mSpQStart.getSelectedItem();
				String qRangeStart = (String) mSpQRangeStart.getSelectedItem();
				String qRangeEnd = (String) mSPQRangeEnd.getSelectedItem();
				String session = (String) mSpSession.getSelectedItem();
				AlgorithmSettings algorithmSettings = new AlgorithmSettings(property, reverse,
						qStart, qRangeStart, qRangeEnd, session);
				mSettingsPresenter.setAlgorithm(algorithmSettings);
				break;
			default:
				break;
		}
	}

	@Override
	public void displayResultMsg(final String msg) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
			}
		});
	}
}
