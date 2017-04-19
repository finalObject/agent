package com.baidu.android.voicedemo;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;




import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;


public class BlueTooth extends Thread {
	private String uuid = "00001101-0000-1000-8000-00805F9B34FB";
	private String deviceName = "HC-06";
	private Boolean state = false;
	private BluetoothSocket bts = null;
	public BluetoothSocket getBts(){
		return bts;
	}
	public Boolean getConnectState(){
		return state;
	}
	public void setState(Boolean state){
		this.state = state;
	}
	public BlueTooth(){

	}
	public String connect(){
		String result ="";
		// 如果已经配对，直接返回
		if (state) {
			result = result+"设备已连接\n";
			return result;
		}
		// 蓝牙的初始化
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (adapter != null) {
			result = result+"设备拥有蓝牙\n";
			if (!adapter.isEnabled()) {
				result="设备蓝牙未启动\n";


			} else {
				result=result+"设备蓝牙已启动\n";
			}
			// 遍历配对的蓝牙设备
			// Log.i("tag","222");
			Set<BluetoothDevice> devices = adapter.getBondedDevices();
			if (devices.size() > 0) {
				Boolean flag = false;
				// Log.i("tag","1");
				for (Iterator iterator = devices.iterator(); iterator
						.hasNext();) {
					// Log.i("tag","2");
					BluetoothDevice device = (BluetoothDevice) iterator
							.next();
					result=result+"发现设备"+ device.getName()+"\n";
					if (device.getName().equals(deviceName)) {
						try {
							bts = device
									.createRfcommSocketToServiceRecord(UUID
											.fromString(uuid));
							bts.connect();
							result=result+"和单片机连接成功\n";
							state = true;

							// Log.i("tag", "start");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							result=result+"和单片机连接失败，重启一下单片机和这个程序吧\n";
						}
						// Log.i("tag","true");
						flag = true;
						break;
					}
				}
				if (flag == false) {
					result=result+ "配对设备里没有"+ deviceName +"\n" ;
				}

			} else {
				result=result+"没有配对设备，请去系统蓝牙自行配对\n";
			}

		} else {
			result=result+"这只手机好像没有蓝牙\n";
		}
	
		return result;
	}


	public String sendMessage(String message){
		String  result = "successful";
		if (this.state==false){
			return "not connect";
		}
		try {
			new DataOutputStream(bts.getOutputStream()).write(message.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			result = "exception";
		}
		return result;
	}
	
	@Override
	public void run() {
		
	}
}
