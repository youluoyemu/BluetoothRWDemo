package com.example.admin.bluetoothrwdemo.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.bluetoothrwdemo.R;
import com.example.admin.bluetoothrwdemo.bean.TagInfo;
import com.example.admin.bluetoothrwdemo.presenter.CheckTagPresenterImpl;
import com.example.admin.bluetoothrwdemo.presenter.ICheckTagPresenter;
import com.example.admin.bluetoothrwdemo.presenter.IRFIDFunction;
import java.util.List;

public class CheckTagFragment extends Fragment implements View.OnClickListener, IRFIDFunction.OnTagInfoUpdateCallback, ICheckTagView {

	private static final String TAG = CheckTagFragment.class.getSimpleName();

	private ListView mLvTagInfo;
	private Button mBtnStart;
	private Button mBtnStop;
	private Button mBtnClear;

	private OnCheckStoppedListener mCheckStoppedListener;
	private TagInfoAdapter mTagInfoAdapter;

	private ICheckTagPresenter mCheckTagPresenter;

	@Override
	public void onTagInfoUpdate() {
		if (getActivity() == null) {
			return;
		}
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mTagInfoAdapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public void showReceiveData(final String data) {
		if (getActivity() == null) {
			return;
		}
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (TextUtils.isEmpty(data)) {
					Toast.makeText(getActivity(), "return no data", Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void showTagInfoList(List<TagInfo> tagInfoList) {
		mTagInfoAdapter = new TagInfoAdapter(tagInfoList);
		mLvTagInfo.setAdapter(mTagInfoAdapter);
	}

	@Override
	public void refreshTagInfoList() {
		if (getActivity() == null) {
			return;
		}
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mTagInfoAdapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public void refreshBtnState() {
		if (getActivity() == null) {
			return;
		}
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mCheckStoppedListener.onCheckStopped(true);
				mBtnStart.setEnabled(true);
			}
		});
	}

	public interface OnCheckStoppedListener {
		void onCheckStopped(boolean isCheckStopped);
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		try {
			mCheckStoppedListener = (OnCheckStoppedListener) getActivity();
		} catch (ClassCastException e) {
			Log.e(TAG, "The host activity must implement OnCheckStoppedListener! ");
		}
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCheckTagPresenter = new CheckTagPresenterImpl(this);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_check_tag, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mLvTagInfo = view.findViewById(R.id.lv_tag_info);
		mBtnStart = view.findViewById(R.id.btn_start);
		mBtnStop = view.findViewById(R.id.btn_stop);
		mBtnClear = view.findViewById(R.id.btn_clear);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
		initEvent();
	}

	private void initData() {
		mCheckTagPresenter.onCreate();
	}

	private void initEvent() {
		mBtnStart.setOnClickListener(this);
		mBtnStop.setOnClickListener(this);
		mBtnClear.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_start:
				mCheckStoppedListener.onCheckStopped(false);
				mCheckTagPresenter.startCheckTag();
				mBtnStart.setEnabled(false);
				mBtnStop.setEnabled(true);
				break;
			case R.id.btn_stop:
				mCheckTagPresenter.stopCheckTag();
				break;
			case R.id.btn_clear:
				mCheckTagPresenter.clearCheckedTagInfo();
				break;
			default:
				break;
		}
	}

	private class TagInfoAdapter extends BaseAdapter {

		private List<TagInfo> mTagInfoList;

		TagInfoAdapter(List<TagInfo> tagInfoList) {
			mTagInfoList = tagInfoList;
		}

		@Override
		public int getCount() {
			return mTagInfoList.size();
		}

		@Override
		public Object getItem(int i) {
			return mTagInfoList.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			ViewHolder holder;
			if (view == null) {
				holder = new ViewHolder();
				view = LayoutInflater.from(getActivity()).inflate(R.layout.item_tag_info, viewGroup, false);
				holder.tvOrderNumber = view.findViewById(R.id.tv_order_number);
				holder.tvEPCTID = view.findViewById(R.id.tv_epc_tid);
				holder.tvTimes = view.findViewById(R.id.tv_times);
				holder.tvRSSI = view.findViewById(R.id.tv_rssi);
				view.setTag(holder);
			}
			holder = (ViewHolder) view.getTag();
			TagInfo tagInfo = mTagInfoList.get(i);
			holder.tvOrderNumber.setText(tagInfo.getOrderNumber());
			holder.tvEPCTID.setText(tagInfo.getEpcTid());
			holder.tvTimes.setText(tagInfo.getTimes());
			holder.tvRSSI.setText(getString(R.string.rssi_data, tagInfo.getRssi()));
			return view;
		}

		class ViewHolder {
			TextView tvOrderNumber;
			TextView tvEPCTID;
			TextView tvTimes;
			TextView tvRSSI;
		}
	}
}
