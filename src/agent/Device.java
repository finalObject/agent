package agent;

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
	public static int findDevice(Device[] devices,String cmd){
		int device = -1;
		for (int i=0;i<devices.length;i++){
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
		int sState =0;
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
	public static void main(String[] agrs){
		Device[] devices = new Device[]{
				new Device(1, "light",new String[]{"灯","电灯"}, false, 0),
				new Device(1, "door",new String[]{"门"}, false, 0),
				new Device(2, "fan", new String[]{"风扇","电扇"},true, 0),
				new Device(3, "musicSwitch", new String[]{"音乐","音响"},false, 0),
				new Device(4, "songs", new String[]{},true, 0),
				new Device(5,"intensity",null,true,0),
				new Device(6,"temperature",null,true,0)
		};
		String cmd = "打开音响";
		int device = Device.findDevice(devices, cmd);
		int sState = Device.getSettingState(devices[device], cmd);
		System.out.println(devices[device].name+":"+sState);
	}
}
