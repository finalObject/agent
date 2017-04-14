package agent;

import java.util.Scanner;

//@author finalObject
//        http://www.finalobject.cn
//        i@finalobject.cn
//        https://github.com/finalObject
//@date 2017.4.11 21:27:32
//@version 2.1
public class Device {
	private int id ;
	private String name;
	private String[] possChiness;
	private int stateRange ;//anology is true, digital is false
	private int state;
	public Device(int id,String name,String[] possChiness,int stateRange,int state){
		this.id=id;
		this.name=name;
		this.possChiness=possChiness;
		this.stateRange = stateRange;
		this.state=state;
	}
	public void setState(int state){
		this.state=state;
	}
	public int getState(){
		return this.state;
	}
	public int getStateRange(){
		return this.stateRange;
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
		int sState = -1;
		//先进行指定状态判定
		if (((cmd.indexOf("最大")!=-1)||(cmd.indexOf("最高")!=-1))&&(device.getState()!=device.getStateRange())){
			sState = device.getStateRange();
		}else if(((cmd.indexOf("最小")!=-1)||(cmd.indexOf("最低")!=-1))&&(device.getState()>1)){
			sState = 1;
		}else if(((cmd.indexOf("五")!=-1)||(cmd.indexOf("5")!=-1))&&(device.getState()!=5)&&(device.getStateRange()>=5)){
			sState = 5;
		}else if(((cmd.indexOf("四")!=-1)||(cmd.indexOf("4")!=-1))&&(device.getState()!=4)&&(device.getStateRange()>=4)){
			sState = 4;
		}else if(((cmd.indexOf("三")!=-1)||(cmd.indexOf("3")!=-1))&&(device.getState()!=3)&&(device.getStateRange()>=3)){
			sState = 3;
		}else if(((cmd.indexOf("二")!=-1)||(cmd.indexOf("2")!=-1))&&(device.getState()!=2)&&(device.getStateRange()>=2)){
			sState = 2;
		}else if(((cmd.indexOf("一")!=-1)||(cmd.indexOf("1")!=-1))&&(device.getState()!=1)&&(device.getStateRange()>=1)&&
                (cmd.indexOf("小")==-1)&&(cmd.indexOf("低")==-1)
				){
			sState = 1;
		}
		//然后进行增减判定
		else  if(((cmd.indexOf("大")!=-1)||(cmd.indexOf("高")!=-1))&&(device.getState()<device.getStateRange())){
			sState = device.getState()+1;
		}else if(((cmd.indexOf("小")!=-1)||(cmd.indexOf("低")!=-1))&&(device.getState()>0)){
			sState = device.getState()-1;
		}
		//最后进行开关判定
		else if(((cmd.indexOf("开")!=-1)||(cmd.indexOf("启动")!=-1))&&(device.getState()==0)){
			sState = 1;
		}else if(((cmd.indexOf("关")!=-1)||(cmd.indexOf("闭")!=-1))&&(device.getState()!=0)){
			sState = 0;
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
		Agent agent = new Agent("1","2");
		String  cmd ;
		while (true){
			cmd=in.nextLine();
			System.out.println(agent.dealWithDevices(cmd));
			Device.displayDevices(agent.getDevices());
			System.out.println("last device:"+agent.getLastDevice());
		}

	}
}
