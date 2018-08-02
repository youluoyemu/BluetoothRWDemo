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
import com.example.admin.bluetoothrwdemo.ui.fragment.PermissionFragment;
import com.example.admin.bluetoothrwdemo.ui.fragment.RWFragment;
import com.example.admin.bluetoothrwdemo.ui.fragment.SettingsFragment;

public class FunctionActivity extends AppCompatActivity implements View.OnClickListener, CheckTagFragment.OnCheckStoppedListener, IFunctionView, SettingsFragment.OnPowerChangedListener {

	// 常量
	private static final String BLUETOOTH_ADDRESS = "address";
	private static final String TAG_CHECK = "CheckTagFragment";
	private static final String TAG_RW = "RWFragment";
	private static final String TAG_PERMISSION = "PermissionFragment";
	private static final String TAG_SETTINGS = "SettingsFragment";
	private static final String BUNDLE_CUR_TAG = "curTag";

	// 成员变量
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

	IFunctionPresenter mFunctionPresenter;
	private Fragment mCurFragment;
	private Fragment mCheckTagFragment;
	private BroadcastReceiver mBatteryReceiver;
	private String mCurTag = TAG_CHECK;

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
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mCurTag = savedInstanceState.getString(BUNDLE_CUR_TAG);
		FragmentManager fragmentManager = getSupportFragmentManager();
		switch (mCurTag) {
			case TAG_CHECK:
				mCurFragment = fragmentManager.findFragmentByTag(TAG_CHECK);
				mBtnCurChoice = mBtnCheckTag;
				break;
			case TAG_RW:
				mCurFragment = fragmentManager.findFragmentByTag(TAG_RW);
				mBtnCurChoice = mBtnRW;
				break;
			case TAG_PERMISSION:
				mCurFragment = fragmentManager.findFragmentByTag(TAG_PERMISSION);
				mBtnCurChoice = mBtnPermission;
				break;
			case TAG_SETTINGS:
				mCurFragment = fragmentManager.findFragmentByTag(TAG_SETTINGS);
				mBtnCurChoice = mBtnSettings;
				break;
			default:
				break;
		}
		mBtnCurChoice.setSelected(true);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(BUNDLE_CUR_TAG, mCurTag);
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
		mCurTag = TAG_CHECK;
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
				mCurTag = TAG_CHECK;
				break;
			case R.id.tv_rw:
				Fragment rwFragment = fragmentManager.findFragmentByTag(TAG_RW);
				if (rwFragment == null) {
					rwFragment = new RWFragment();
					transaction.add(R.id.fl_contain, rwFragment, TAG_RW);
				} else {
					transaction.show(rwFragment);
				}
				mCurFragment = rwFragment;
				mBtnCurChoice = mBtnRW;
				mCurTag = TAG_RW;
				break;
			case R.id.tv_permission:
				Fragment permissionFragment = fragmentManager.findFragmentByTag(TAG_PERMISSION);
				if (permissionFragment == null) {
					permissionFragment = new PermissionFragment();
					transaction.add(R.id.fl_contain, permissionFragment, TAG_PERMISSION);
				} else {
					transaction.show(permissionFragment);
				}
				mCurFragment = permissionFragment;
				mBtnCurChoice = mBtnPermission;
				mCurTag = TAG_PERMISSION;
				break;
			case R.id.tv_settings:
				Fragment settingsFragment = fragmentManager.findFragmentByTag(TAG_SETTINGS);
				if (settingsFragment == null) {
					settingsFragment = new SettingsFragment();
					transaction.add(R.id.fl_contain, settingsFragment, TAG_SETTINGS);
				} else {
					transaction.show(settingsFragment);
				}
				mCurFragment = settingsFragment;
				mBtnCurChoice = mBtnSettings;
				mCurTag = TAG_SETTINGS;
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

	@Override
	public void onPowerChanged(String newPower) {
//		mTvPower.setText(getString(R.string.power, newPower));
		mFunctionPresenter.onCreate();
	}

	private class BatteryBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int level = intent.getIntExtra("level", 0);
			mTvBattery.setText(getString(R.string.battery, level));
		}
	}
}
