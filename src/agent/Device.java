package agent;

import java.util.Scanner;

//@author finalObject
//        http://www.finalobject.cn
//        i@finalobject.cn
//        https://github.com/finalObject
//@date 2017.4.11 21:27:32
//@version 1.0
public class Device {
	private int id ;
	private String name;
	private String[] possChiness;
	private boolean type ;//anology is true, digital is false
	private int state;
	public Device(int id,String name,String[] possChiness,boolean type,int state){
		this.id=id;
		this.name=name;
		this.possChiness=possChiness;
		this.type=type;
		this.state=state;
	}
	public void setState(int state){
		this.state=state;
	}
	public int getState(){
		return this.state;
	}
	public boolean getType(){
		return this.type;
	}
	public int getId(){
		return this.id;
	}
	public String getName(){
		return this.name;
	}
	public String[] getPossChiness(){
		return this.possChiness;
	}
	public static void displayDevices(Device[] devices){
		for (int i=0;i<devices.length;i++){
			System.out.print(devices[i].name+":"+devices[i].state+";");		
		}
		System.out.println();
	}
	public static int findDevice(Device[] devices,String cmd){
		int device = -1;
		for (int i=0;i<devices.length;i++){
			if (devices[i].possChiness==null){
				continue;
			}
			for (int j =0;j<devices[i].possChiness.length;j++){
				if (cmd.indexOf(devices[i].possChiness[j])!=-1){
					device = i;
					break;
				}
			}
			if (device!=-1){
				break;
			}
		}
		return device;
	}
	public static int getSettingState(Device device,String cmd){
		int sState = 0;
		if (device.getType()==false){	
			//数字器件
			if (cmd.indexOf("开")!=-1&&device.getState()!=1){
				sState = 1;
			}else if (((cmd.indexOf("关")!=-1)||(cmd.indexOf("闭")!=-1))&&(device.getState()!=0)){
				sState = 0;
			}else{
				sState = -1;
			}
		}else {
			//模拟器件
			if (((cmd.indexOf("关")!=-1)||(cmd.indexOf("闭")!=-1))&&(device.getState()!=0)){
				sState = 0;
			}else if(cmd.indexOf("最大")!=-1&&device.getState()<255){
				sState = 255;
			}else if((cmd.indexOf("大")!=-1||(cmd.indexOf("开")!=-1))&&device.getState()<255){
				sState = device.getState()+64;
				if (sState>255){sState=255;}
			}else if(cmd.indexOf("小")!=-1&&device.getState()>0){
				sState = device.getState()-64;
				if (sState<0){sState=0;}
			}else{
				sState=-1;
			}
		}
		return sState; 
	}
	//这个函数啊，封装在这里里面好像不太合适
	//别用了，放到Agent里去吧，我就不删除了，你别用了
	public static String getCmdAndChangeStates(Device[] devices,String cmd){
		String cmdFormat = "#";
		int device = Device.findDevice(devices, cmd);
		//如果设备寻找错误，停止生成指令，返回#
		if (device==-1){
			return cmdFormat;
		}
		int sState = Device.getSettingState(devices[device], cmd);
		//如果状态指定错误，停止生成指令，返回#
		if (sState==-1){
			return cmdFormat;
		}
		//如果设备正确，状态更改指令正确，生成指令
		for (int i=0;i<devices.length;i++){
			if (devices[i].possChiness!=null){
				if (i==device){
					cmdFormat = cmdFormat+String.format("%03d", device,sState);
				}else{
				cmdFormat = cmdFormat+String.format("%03d", devices[i].id,devices[i].state); 
				}
			}
		}
		cmdFormat = cmdFormat+"!";
		//改变状态
		devices[device].setState(sState);
		return cmdFormat;
	}
	public static void main(String[] agrs){
		Scanner in = new Scanner(System.in);
		Device[] devices = new Device[]{
				new Device(1, "light",new String[]{"灯","电灯"}, false, 0),
				new Device(1, "door",new String[]{"门"}, false, 0),
				new Device(2, "fan", new String[]{"风扇","电扇"},true, 0),
				new Device(3, "musicSwitch", new String[]{"音乐","音响"},false, 0),
				new Device(4, "songs", new String[]{},true, 0),
				new Device(5,"intensity",null,true,0),
				new Device(6,"temperature",null,true,0)
		};
		Device.displayDevices(devices);
		String cmd;
		while (true){
			cmd = in.nextLine();
			System.out.println(Device.getCmdAndChangeStates(devices, cmd));
			Device.displayDevices(devices);
		}

	}
}
