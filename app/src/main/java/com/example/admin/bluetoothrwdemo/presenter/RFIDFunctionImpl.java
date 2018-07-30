package com.example.admin.bluetoothrwdemo.presenter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import com.example.admin.bluetoothrwdemo.bean.AlgorithmSettings;
import com.example.admin.bluetoothrwdemo.bean.TagInfo;
import com.thinta.function.RFIDFunction;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class RFIDFunctionImpl implements IRFIDFunction {

    // the well-known SPP UUID
    private static final java.util.UUID UUID = java.util.UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static RFIDFunctionImpl INSTANCE;
    private RFIDFunction mRFIDFunction;
    private volatile boolean mCheckedStop;
    private OnBluetoothInitCallback mCallback;

    private RFIDFunctionImpl(Context context) {
        mRFIDFunction = new RFIDFunction(context);
    }

    public static RFIDFunctionImpl getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new RFIDFunctionImpl(context);
        }
        return INSTANCE;
    }

    @Override
    public boolean initBluetooth() {
        return false;
    }

    @Override
    public void connectBluetooth(String address, OnBluetoothInitCallback callback) {
//		mRFIDFunction.rfidInit(address);
        mCallback = callback;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (BluetoothAdapter.checkBluetoothAddress(address)) {
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
            ConnectedThread connectedThread = new ConnectedThread(device);
            connectedThread.start();
        }
    }

    @Override
    public void startCheckTag(final List<TagInfo> tagInfoList, final OnTagInfoUpdateCallback callback) {
        mCheckedStop = false;
        new Thread() {
            @Override
            public void run() {
                List<String> tagIdList = null;
                while (!mCheckedStop) {
                    tagIdList = mRFIDFunction.searchTag();
                }
                if (tagIdList != null) {
                    for (String id : tagIdList) {
                        TagInfo tagInfo = new TagInfo();
                        tagInfo.setOrderNumber(id);
                        tagInfoList.add(tagInfo);
                    }
                    callback.onTagInfoUpdate();
                }
            }
        }.start();
    }

    @Override
    public void stopCheckTag() {
        mCheckedStop = true;
    }

    @Override
    public String getPower() {
        return null;
    }

    @Override
    public String readData(String area, String addressStart, String length) {
        return null;
    }

    @Override
    public boolean writeData(String area, String data, String addressStart, String length) {
        return false;
    }

    @Override
    public boolean destroyData(String area, String addressStart, String length) {
        return false;
    }

    @Override
    public boolean setPower(String power) {
        return false;
    }

    @Override
    public boolean setProfile(String profile) {
        return false;
    }

    @Override
    public boolean setArea(String area) {
        return false;
    }

    @Override
    public boolean setSkipFrequencyTime(String openTime, String closeTime) {
        return false;
    }

    @Override
    public boolean setFrequencyPoint(String frequencyPoint) {
        return false;
    }

    @Override
    public boolean setAlgorithm(AlgorithmSettings algorithmSettings) {
        return false;
    }

    private class ConnectedThread extends Thread {

        private final BluetoothSocket mSocket;

        ConnectedThread(BluetoothDevice device) {
            BluetoothSocket temp = null;
            try {
                temp = device.createRfcommSocketToServiceRecord(UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mSocket = temp;
        }

        @Override
        public void run() {
            try {
                mCallback.onBluetoothInit(true);
                mSocket.connect();
                manageSocket(mSocket);
            } catch (IOException e) {
                try {
                    mSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private void manageSocket(final BluetoothSocket socket) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OutputStream outputStream = socket.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
