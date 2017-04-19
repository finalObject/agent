package com.baidu.android.voicedemo;

import java.util.ArrayList;
import java.util.Random;



// @author finalObject
//         http://www.finalobject.cn
//         i@finalobject.cn
//         https://github.com/finalObject
// @date 2017.4.11 21:03:32
// @version 1.0
public class Agent {
	
//单条指令的解析
	
//近几条指令的记忆功能
	
//事件记忆功能（某个条件下，包括环境调节。进行某种操作）
//对某些事件产生试探性提问，根据回答进行相应操作
//某些事件直接进行操作
	
	private String name;
	private String master;
	private boolean isWaiting;//询问主人等待确认的指令，获得许可
	private String waitingCmd;//等待确认的指令
	private ArrayList<String> hisCmd;//记录历史指令
	private int lastReply;//记录上一个语句中回复种类
	private int lastDevice;//记录上一条指令操作的设备，可能下一条指令可以不说明设备直接操作
	private String[][] replies;//面对指令的不同回复内容（索引数组
	private Device[] devices;//拥有设备，记录当前状态，可以进行的操作
										//传感器状态，人的状态，也放在这里面
	private String cmdFormat;//储存单次回复指令
	private String word;//储存单次回复的文字内容
	
	private BlueTooth bt ;
	public int getLastDevice(){
		return lastDevice;
	}
	public Device[] getDevices(){
		return devices;
	}
	public static void main(String[] args){
//		Agent agent =new Agent("Saber","Master");
//		Scanner in = new Scanner(System.in);
//		while(true){
//			Device.displayDevices(agent.getDevices());
//			System.out.println(agent.step(in.nextLine()));
//		}
	}
	public Agent(String name,String master){
		this.name=name;
		this.master=master;
		init();
	}
	private void init(){
		bt = new BlueTooth();
		hisCmd=new ArrayList<>();
		isWaiting = false;
		waitingCmd = null;
		lastReply=-1;
		lastDevice=-1;
		//初始化replies
		replies =new String[][]{
			//reply index 0 -- jobs done
			new String[]{"好的","没问题","不用客气"},
			//reply index 1 -- what's else
			new String[]{"没有别的了吗","可以了吗"},
			//reply index 2 -- who am i
			new String[]{"你是我的宝贝甜蜜饯","你是"+master},
			//reply index 3 -- who are u
			new String[]{"我是"+name+",宇宙第一管家，越用越贴心"},
			//reply index 4 -- i like u
			new String[]{"我也喜欢你","对不起，"+master+",我只是一个机器","好巧"},
			//reply index 5 -- do u have a bf
			new String[]{"其实我和空调中控系统以为眉来眼去两个月了","单身才能做好管家！"},
			//reply index 6 -- can u speak other language
			new String[]{"我什么语言都会说，就是我的工程师没有把选择权交给用户而已",
					"你要是真的有需要，就去找我的工程师"},
			//reply index 7 -- nice weather
			new String[]{"还行吧，对我来讲无所谓","这么好的天气应该出去走走，家里有我照顾",
					"你开心就好"},
			//reply index 8 -- still learning,function limited
			new String[]{"抱歉我还理解不了你说的话","我还在学习，现在还听不懂你说的话","你说啥"},
			//reply index 9 -- bad cmd
			new String[]{"命令有问题"},
			//reply index 10 -- greeting
			new String[]{"你好"},
			//reply index 11 -- re to haha
			new String[]{"哈哈","你开心就好"}
		};

		
		//初始化devices
		devices = new Device[]{
				new Device(1, "light",new String[]{"灯","电灯"}, 1, 0),
				new Device(1, "door",new String[]{"门"}, 1, 0),
				new Device(2, "fan", new String[]{"风扇","电扇"},5, 0),
				new Device(3, "musicSwitch", new String[]{"音乐","音响"},1, 0),
				new Device(4, "songs", new String[]{},1, 0),
				new Device(5,"intensity",null,1,0),
				new Device(6,"temperature",null,1,0)
		};
	}
	//基本调试正确，可以正常使用
	public String dealWithDevices(String cmd){
		cmdFormat="#";
		int device = Device.findDevice(devices, cmd);
		//如果设备没有找到，而且没有上一语句的设备，返回#
		if (device==-1){
				if (lastDevice!=-1){
					device = lastDevice;
				}else{
					return cmdFormat;
				}
		}
		lastDevice=device;
		int sState = Device.getSettingState(devices[device], cmd);
		//如果状态指定错误，停止生成指令，返回#
		if (sState==-1){
			return cmdFormat;
		}
		//如果设备正确，状态更改指令正确，生成指令
		for (int i=0;i<devices.length;i++){
			if (devices[i].getPossChiness()!=null){
				if (i==device){
					cmdFormat = cmdFormat+String.format("%d",sState);
				}else{
				cmdFormat = cmdFormat+String.format("%d", devices[i].getState()); 
				}
			}
		}
		cmdFormat = cmdFormat+"!";
		//改变状态
		devices[device].setState(sState);
		return cmdFormat;
	}
	//写完了，有待测试
	public String dealWithReply(String cmd){
		int reply = -1;
		//确定返回种类
		if(cmd.equals("#")){
			reply = 0;
		}else if((cmd.indexOf("你好")!=-1)||(cmd.indexOf("上午好")!=-1)||(cmd.indexOf("下午好")!=-1)){
			reply = 10;
		}else if(cmd.indexOf("我是谁")!=-1){
			reply = 2;
		}else if((cmd.indexOf("你是谁")!=-1)||(cmd.indexOf("你是哪位")!=-1)){
			reply = 3;
		}else if((cmd.indexOf("我爱你")!=-1)||(cmd.indexOf("我喜欢你")!=-1)){
			reply = 4;
		}else if((cmd.indexOf("男朋友")!=-1)||(cmd.indexOf("男友")!=-1)||(cmd.indexOf("喜欢的人")!=-1)){
			reply = 5;
		}else if(((cmd.indexOf("会")!=-1)||(cmd.indexOf("能")!=-1))&&
				((cmd.indexOf("讲")!=-1)||(cmd.indexOf("说")!=-1))&&(cmd.indexOf("语")!=-1)){
			reply = 6;
		}else if((cmd.indexOf("天")!=-1)&&((cmd.indexOf("不错")!=-1)||(cmd.indexOf("好")!=-1))){
			reply = 7;
		}else if((cmd.indexOf("哈")!=-1)||(cmd.indexOf("嘻")!=-1)){
			reply = 11;
		}else{
			reply = 8;
		}
		lastReply = reply ;
		//随机选取返回语句
		word = replies[reply][new Random().nextInt(replies[reply].length)];
		return word;
	}
	public String dealWithBt(String cmd){
		String result = "#";
		if (cmd.indexOf("连接蓝牙")!=-1){
			result = bt.connect();
		}
		return result;
	}

	public String step(String cmd){
		hisCmd.add(new String(cmd));
		
		//优先开启蓝牙；
		String blueT = dealWithBt(cmd);
		if(blueT.equals("#")==false){
			return blueT;
		}	
		//先处理Devices
		cmdFormat = dealWithDevices(cmd);
		//发送的指令正常生成，规定回复文本种类为job done
		if (cmdFormat.length()!=1){
			//在dealWithReply里面判断#为job done
			cmd="#";
		}
		
		//蓝牙发送指令
		if (cmdFormat.equals("#")==false){
			if(bt.sendMessage(cmdFormat).equals("successful")){
				cmdFormat = cmdFormat +":get";
			}
		}
		word = dealWithReply(cmd);
		//反馈文本
		return cmdFormat+"--"+word;
	}
	//差一个后台处理，控制自动判断的函数
}
