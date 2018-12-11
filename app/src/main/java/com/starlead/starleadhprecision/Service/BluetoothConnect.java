package com.starlead.starleadhprecision.Service;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.starlead.starleadhprecision.GPSDatainfo;
import com.starlead.starleadhprecision.R;
import com.starlead.starleadhprecision.entity.DevicesInfo;
import com.starlead.starleadhprecision.entity.GPSData;
import com.starlead.starleadhprecision.util.Constants;
import com.starlead.starleadhprecision.util.GPSDataUtils;
import com.starlead.starleadhprecision.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class BluetoothConnect implements BluetoothServiceListenter {

    private Activity mActivity;

    private List<DevicesInfo> mDevicesInfoList = new ArrayList<>();
    private AlertDialog mDevicesDialog;
    private AlertDialog mProgressDialog;
    private DevicesInfoAdapter mDevicesInfoAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private View mDeviceDialogVIew;
    private boolean isEnabled = false;
    private BluetoothService mBluetoothService;
    private GPSDatainfo mGPSDatainfo;





    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                DevicesInfo devicesInfo = new DevicesInfo(device.getName(), device.getAddress());
                if (mDevicesInfoList.contains(devicesInfo)) {
                    return;
                }
                mDevicesInfoList.add(devicesInfo);
                mDevicesInfoAdapter.notifyItemInserted(mDevicesInfoList.size() - 1);
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                mDeviceDialogVIew.findViewById(R.id.progressBar).setVisibility(View.GONE);
            }
        }
    };


    public BluetoothConnect(Activity activity, GPSDatainfo gpsDatainfo) {
        mActivity = activity;
        mGPSDatainfo = gpsDatainfo;
        mBluetoothService = BluetoothService.getInstance(this);
    }




    public void stop() {
        if (mBluetoothService != null) {
            mBluetoothService.stop();
        }
    }


    public void Detach() {
        mBluetoothService.DetachListenter();
    }

    public int getState(){
        return mBluetoothService.getState();
    }


    //初始化并弹出对话框方法
    public void showDeviceDialog() {
        //获取蓝牙对象
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            mActivity.finish();
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mActivity.startActivityForResult(enableBtIntent, 1);
        } else {
            isEnabled = true;
        }

        if (!isEnabled) {
            return;
        }


        //蓝牙设备搜索广播注册
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        mActivity.registerReceiver(mReceiver, filter);

        mDevicesInfoList.clear();
        mBluetoothAdapter.startDiscovery();

        mDeviceDialogVIew = LayoutInflater.from(mActivity).inflate(R.layout.dialog_devices_connect_layout, null, false);

        ;
        //可匹配设备列表
        RecyclerView recyViewDeviceDevice = mDeviceDialogVIew.findViewById(R.id.recyclerViewDevicesList);
        LinearLayoutManager lineLayoutManagerDevice = new LinearLayoutManager(mActivity);
        recyViewDeviceDevice.setLayoutManager(lineLayoutManagerDevice);
        mDevicesInfoAdapter = new DevicesInfoAdapter(mDevicesInfoList);
        recyViewDeviceDevice.setAdapter(mDevicesInfoAdapter);


        //弹出对话框
        mDevicesDialog = new AlertDialog.Builder(mActivity).setView(mDeviceDialogVIew).create();
        mDevicesDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBluetoothAdapter.cancelDiscovery();
                mActivity.unregisterReceiver(mReceiver);
            }
        });

        mDevicesDialog.show();
    }

    public void DataDispose(String data) {
        if (Pattern.matches(".*\\$GPGGA.*", data) || data.contains("$GPGGA")) {
            GPSData gpsData = new GPSData(data);
            LatLng latLng = GPSDataUtils.GPSDataDispose(gpsData.getLatitude(), gpsData.getLongitude());
            mGPSDatainfo.sendGPSData(latLng);
        }
    }

    @Override
    public void haveGPSData(String GPSData) {
        DataDispose(GPSData);
    }

    @Override
    public void UpdateStatus(int Status) {
        switch (Status) {
            case BluetoothService.STATE_CONNECTED:
                ToastUtils.showToast(mActivity, "已连接..", Toast.LENGTH_SHORT);
                break;
            case BluetoothService.STATE_CONNECTING:
                View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_bluetooth_progress_item, null, false);
                mProgressDialog = new AlertDialog.Builder(mActivity).setView(view).create();
                mProgressDialog.show();
                break;
            case BluetoothService.STATE_CONNECTSUCCESS:
                mProgressDialog.dismiss();
                ToastUtils.showToast(mActivity, "连接成功..", Toast.LENGTH_SHORT);
                break;
            case BluetoothService.STATE_CONNECTFAILED:
                mProgressDialog.dismiss();
                ToastUtils.showToast(mActivity, "连接失败..", Toast.LENGTH_SHORT);
                break;
            case BluetoothService.STATE_CONNECTSTOP:
                ToastUtils.showToast(mActivity, "断开连接..", Toast.LENGTH_SHORT);
                break;
        }

    }


    class DevicesInfoAdapter extends RecyclerView.Adapter<DevicesInfoAdapter.ViewHolder> {

        private List<DevicesInfo> mDevicesInfoList;

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView mTextViewdevicesName;
            TextView mTextViewdevicesAddress;
            ;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                mTextViewdevicesName = itemView.findViewById(R.id.TextViewdevicesName);
                mTextViewdevicesAddress = itemView.findViewById(R.id.TextViewdevicesAddress);
            }


        }

        public DevicesInfoAdapter(List<DevicesInfo> devicesInfoList) {
            this.mDevicesInfoList = devicesInfoList;
        }


        @NonNull
        @Override
        public DevicesInfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.devices_item, viewGroup, false);
            DevicesInfoAdapter.ViewHolder viewHolder = new DevicesInfoAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(DevicesInfoAdapter.ViewHolder viewHolder, int i) {

            final DevicesInfo devicesInfo = mDevicesInfoList.get(i);
            viewHolder.mTextViewdevicesAddress.setText(devicesInfo.getDevicetAddress());
            viewHolder.mTextViewdevicesName.setText(devicesInfo.getDeviceName());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BluetoothDevice bluetoothDevice = mBluetoothAdapter.getRemoteDevice(devicesInfo.getDevicetAddress());
                    mBluetoothService.connect(bluetoothDevice);
                    mDevicesDialog.dismiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDevicesInfoList.size();
        }


    }

}
