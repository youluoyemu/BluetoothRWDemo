package com.example.admin.bluetoothrwdemo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.admin.bluetoothrwdemo.R;
import com.example.admin.bluetoothrwdemo.presenter.IRWPresenter;
import com.example.admin.bluetoothrwdemo.presenter.RWPresenterImpl;

public class RWFragment extends BaseFragment implements IRWView, View.OnClickListener {

	private Spinner mSpArea;
	private EditText mEtStartAddress;
	private EditText mEtLength;
	private Button mBtnRead;
	private Button mBtnWrite;
	private Button mBtnDestroy;
	private EditText mEtRead;
	private EditText mEtWrite;
	private EditText mEtDestroy;

	private IRWPresenter mRWPresenter;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mRWPresenter = new RWPresenterImpl(this);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_rw, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mSpArea = view.findViewById(R.id.sp_area);
		mEtStartAddress = view.findViewById(R.id.et_start_address);
		mEtLength = view.findViewById(R.id.et_length);
		mBtnRead = view.findViewById(R.id.btn_read);
		mBtnWrite = view.findViewById(R.id.btn_write);
		mBtnDestroy = view.findViewById(R.id.btn_destroy);
		mEtRead = view.findViewById(R.id.et_read);
		mEtWrite = view.findViewById(R.id.et_write);
		mEtDestroy = view.findViewById(R.id.et_destroy);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
		initEvent();
	}

	private void initData() {
		if (getActivity() == null) {
			return;
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.area));
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpArea.setAdapter(adapter);
	}

	private void initEvent() {
		mBtnRead.setOnClickListener(this);
		mBtnWrite.setOnClickListener(this);
		mBtnDestroy.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		String area = (String) mSpArea.getSelectedItem();
		String addressStart = mEtStartAddress.getText().toString().trim();
		String length = mEtLength.getText().toString().trim();
		switch (view.getId()) {
			case R.id.btn_read:
				mRWPresenter.readData(area, addressStart, length);
				break;
			case R.id.btn_write:
				String data = mEtWrite.getText().toString().trim();
				mRWPresenter.writeData(area, data, addressStart, length);
				break;
			case R.id.btn_destroy:
				mRWPresenter.destroyData(area, addressStart, length);
				break;
			default:
				break;
		}
	}

	@Override
	public void setReadResultText(final String result) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mEtRead.setText(result);
			}
		});
	}

	@Override
	public void setWriteResultText(final String result) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mEtWrite.setText(result);
			}
		});
	}

	@Override
	public void setDestroyResultText(final String result) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mEtDestroy.setText(result);
			}
		});
	}
}
