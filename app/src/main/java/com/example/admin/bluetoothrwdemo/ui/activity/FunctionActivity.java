package com.example.admin.bluetoothrwdemo.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.bluetoothrwdemo.R;
import com.example.admin.bluetoothrwdemo.presenter.FunctionPresenterImpl;
import com.example.admin.bluetoothrwdemo.presenter.IFunctionPresenter;
import com.example.admin.bluetoothrwdemo.ui.fragment.CheckTagFragment;
import com.example.admin.bluetoothrwdemo.ui.fragment.RWFragment;
import com.example.admin.bluetoothrwdemo.ui.fragment.SettingsFragment;
import com.example.admin.bluetoothrwdemo.presenter.RFIDFunctionImpl;

public class FunctionActivity extends AppCompatActivity implements View.OnClickListener, CheckTagFragment.OnCheckStoppedListener, IFunctionView {

	private static final String BLUETOOTH_ADDRESS = "address";
	private static final String TAG_CHECK = "CheckTagFragment";
	private static final String TAG_RW = "RWFragment";
	private static final String TAG_SETTINGS = "SettingsFragment";

	private TextView mTvDevName;
	private TextView mTvPower;
	private TextView mTvBattery;
	private TextView mBtnCheckTag;
	private TextView mBtnRW;
	private TextView mBtnPermission;
	private TextView mBtnSettings;
	private TextView mBtnCurChoice;
	private TextView mTvTitle;
	private ImageView mIvBack;

	private Fragment mCurFragment;
	private Fragment mCheckTagFragment;
	private Fragment mRWFragment;
	private Fragment mSettingsFragment;
	private BroadcastReceiver mBatteryReceiver;
	private IFunctionPresenter mFunctionPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_function);
		initView();
		initData();
		initEvent();
		if (savedInstanceState == null) {
			initFragment();
			mBtnCurChoice = mBtnCheckTag;
			mBtnCurChoice.setSelected(true);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mBatteryReceiver);
	}

	public static void start(Context context, String address) {
		Intent starter = new Intent(context, FunctionActivity.class);
		starter.putExtra(BLUETOOTH_ADDRESS, address);
		context.startActivity(starter);
	}

	private void initView() {
		mTvDevName = findViewById(R.id.tv_name);
		mTvPower = findViewById(R.id.tv_power);
		mTvBattery = findViewById(R.id.tv_battery);
		mBtnCheckTag = findViewById(R.id.tv_check);
		mBtnRW = findViewById(R.id.tv_rw);
		mBtnPermission = findViewById(R.id.tv_permission);
		mBtnSettings = findViewById(R.id.tv_settings);
		mIvBack = findViewById(R.id.iv_back);
		mTvTitle = findViewById(R.id.tv_title);
	}

	private void initData() {
		mTvTitle.setText(R.string.function_title);
		mTvDevName.setText(getString(R.string.dev_name, Build.BRAND, Build.MODEL));
		mTvPower.setText(getString(R.string.power, "0"));
		mFunctionPresenter = new FunctionPresenterImpl(this);
		mFunctionPresenter.onCreate();
	}

	private void initEvent() {
		mBtnCheckTag.setOnClickListener(this);
		mBtnRW.setOnClickListener(this);
		mBtnPermission.setOnClickListener(this);
		mBtnSettings.setOnClickListener(this);
		mIvBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				FunctionActivity.this.finish();
			}
		});
		mBatteryReceiver = new BatteryBroadCastReceiver();
		IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(mBatteryReceiver, intentFilter);
	}

	private void initFragment() {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		mCheckTagFragment = new CheckTagFragment();
		transaction.add(R.id.fl_contain, mCheckTagFragment, TAG_CHECK);
		transaction.commit();
		mCurFragment = mCheckTagFragment;
	}

	@Override
	public void onClick(View view) {
		mBtnCurChoice.setSelected(false);
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.hide(mCurFragment);
		switch (view.getId()) {
			case R.id.tv_check:
				mCheckTagFragment = fragmentManager.findFragmentByTag(TAG_CHECK);
				if (mCheckTagFragment == null) {
					mCheckTagFragment = new CheckTagFragment();
					transaction.add(R.id.fl_contain, mCheckTagFragment, TAG_CHECK);
				} else {
					transaction.show(mCheckTagFragment);
				}
				mCurFragment = mCheckTagFragment;
				mBtnCurChoice = mBtnCheckTag;
				break;
			case R.id.tv_rw:
				mRWFragment = fragmentManager.findFragmentByTag(TAG_RW);
				if (mRWFragment == null) {
					mRWFragment = new RWFragment();
					transaction.add(R.id.fl_contain, mRWFragment, TAG_RW);
				} else {
					transaction.show(mRWFragment);
				}
				mCurFragment = mRWFragment;
				mBtnCurChoice = mBtnRW;
				break;
			case R.id.tv_permission:
				mBtnCurChoice = mBtnPermission;
				break;
			case R.id.tv_settings:
				mSettingsFragment = fragmentManager.findFragmentByTag(TAG_SETTINGS);
				if (mSettingsFragment == null) {
					mSettingsFragment = new SettingsFragment();
					transaction.add(R.id.fl_contain, mSettingsFragment, TAG_SETTINGS);
				} else {
					transaction.show(mSettingsFragment);
				}
				mCurFragment = mSettingsFragment;
				mBtnCurChoice = mBtnSettings;
				break;
			default:
				break;
		}
		transaction.commit();
		mBtnCurChoice.setSelected(true);
	}

	@Override
	public void onCheckStopped(boolean isCheckStopped) {
		if (isCheckStopped) {
			mBtnRW.setEnabled(true);
			mBtnPermission.setEnabled(true);
			mBtnSettings.setEnabled(true);
			return;
		}
		mBtnRW.setEnabled(false);
		mBtnPermission.setEnabled(false);
		mBtnSettings.setEnabled(false);
	}

	@Override
	public void setPowerText(final String power) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mTvPower.setText(getString(R.string.power, power));
			}
		});
	}

	private class BatteryBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int level = intent.getIntExtra("level", 0);
			mTvBattery.setText(getString(R.string.battery, level));
		}
	}
}
