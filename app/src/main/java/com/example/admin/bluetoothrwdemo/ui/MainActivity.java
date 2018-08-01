package com.example.admin.bluetoothrwdemo.ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.bluetoothrwdemo.presenter.IMainPresenter;
import com.example.admin.bluetoothrwdemo.presenter.MainPresenterImpl;
import com.example.admin.bluetoothrwdemo.ui.activity.FunctionActivity;
import com.example.admin.bluetoothrwdemo.R;

import java.util.List;

public class MainActivity extends AppCompatActivity implements IMainView, View.OnClickListener, AdapterView.OnItemSelectedListener {

	public static final String TAG = "MainActivity";
	private static final int REQUEST_ENABLE_BT = 1;

	private Spinner mSpAddress;
	private Button mBtnConn;
	private EditText mEtAddress;
	private TextView mTvProgress;
	private LinearLayout mLlProgress;

	private BluetoothAdapter mBluetoothAdapter;
	private ArrayAdapter<String> mAddressListAdapter;
	private boolean mStartup;

	private BroadcastReceiver mBluetoothReceiver;
	private IMainPresenter mMainPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initBluetooth();
		initView();
		initData();
		initEvent();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!mStartup) {
			startupBluetooth();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mBluetoothReceiver);
		if (mMainPresenter != null) {
			mMainPresenter.onDestroy();
			mMainPresenter = null;
		}
		mBluetoothAdapter.disable();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
			updateAddressListData();
		}
	}

	private void initBluetooth() {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, getString(R.string.not_support), Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	private void initView() {
		mSpAddress = findViewById(R.id.sp_address);
		mBtnConn = findViewById(R.id.btn_conn);
		mEtAddress = findViewById(R.id.et_address);
		mTvProgress = findViewById(R.id.tv_progress);
		mLlProgress = findViewById(R.id.ll_progress);
		mTvProgress = findViewById(R.id.tv_progress);
	}

	private void initData() {
		mMainPresenter = new MainPresenterImpl(this);
		mMainPresenter.onCreate();
	}

	private void initEvent() {
		mSpAddress.setOnItemSelectedListener(this);
		mBtnConn.setOnClickListener(this);
		mBluetoothReceiver = new BluetoothReceiver();
		IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mBluetoothReceiver, intentFilter);
	}

	private void updateAddressListData() {
		mMainPresenter.updateAddressList(null);
		if (mBluetoothAdapter.isDiscovering()) {
			mBluetoothAdapter.cancelDiscovery();
		}
		Log.d(TAG, "startDiscovery: ");

		mBluetoothAdapter.startDiscovery();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_conn:
				connectBluetooth();
				break;
			default:
				break;
		}
	}

	private void startupBluetooth() {
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBT, REQUEST_ENABLE_BT);
		} else {
			updateAddressListData();
		}
		mStartup = true;
	}

	private void connectBluetooth() {
		mLlProgress.setVisibility(View.VISIBLE);
		mTvProgress.setText(R.string.connect_loading);
		mBluetoothAdapter.cancelDiscovery();
		String bluetoothAddress = mEtAddress.getText().toString().trim();
		mMainPresenter.connectBluetooth(bluetoothAddress);
	}

	@Override
	public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
		if (0 == i) {
			return;
		}
		mMainPresenter.onSpinnerItemSelected(i);
	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView) {

	}

	@Override
	public void showSpinnerList(List<String> addressList) {
		mAddressListAdapter = new ArrayAdapter<>(this,
				android.R.layout.simple_spinner_item, addressList);
		mAddressListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpAddress.setAdapter(mAddressListAdapter);
	}

	@Override
	public void setAddressText(String address) {
		mEtAddress.setText(address);
		mEtAddress.setSelection(address.length());
		mSpAddress.setSelection(0);
	}

	@Override
	public void refreshSpinnerList() {
		mAddressListAdapter.notifyDataSetChanged();
	}

	@Override
	public void startFunctionActivity() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mTvProgress.setText(R.string.connect_success);
				Intent intent = new Intent(MainActivity.this, FunctionActivity.class);
				startActivity(intent);
			}
		});

	}

	@Override
	public void displayConnFailedMessage() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(MainActivity.this, getString(R.string.conn_failed), Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void hideProgressBar() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mLlProgress.setVisibility(View.GONE);
			}
		});
	}

	private class BluetoothReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				Log.d(TAG, "onReceive: " + device.getName() + "\n" + device.getAddress());
				if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
					return;
				}
				mMainPresenter.updateAddressList(device.getName() + "：" + device.getAddress());
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				Toast.makeText(context, getString(R.string.discovery_finish), Toast.LENGTH_SHORT).show();
			}
		}
	}
}
