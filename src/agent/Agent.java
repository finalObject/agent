package agent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.LayoutStyle;

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
	private int[] waitingCmd;//等待确认的指令
	private ArrayList<String> hisCmd;//记录历史指令
	private int lastReply;//记录上一个语句中回复种类
	private int lastDevice;//记录上一条指令操作的设备，可能下一条指令可以不说明设备直接操作
	private String[][] replies;//面对指令的不同回复内容（索引数组
	private Device[] devices;//拥有设备，记录当前状态，可以进行的操作
										//传感器状态，人的状态，也放在这里面
	private String cmdFormat;//储存单次回复指令
	private String word;//储存单次回复的文字内容
	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
		Agent agent = new Agent("1","2");
		String  cmd ;
		while (true){
			cmd=in.nextLine();
			System.out.println(agent.dealWithDevices(cmd));
			Device.displayDevices(agent.devices);
			System.out.println("last device:"+agent.lastDevice);
		}
	}
	public Agent(String name,String master){
		this.name=name;
		this.master=master;
		init();
	}
	private void init(){
		hisCmd=new ArrayList<>();
		isWaiting = false;
		waitingCmd = new int[]{-1,-1};
		lastReply=-1;
		lastDevice=-1;
		//初始化reply
		replies =new String[10][];
		//reply index 0 -- jobs done
		replies[0]=new String[]{"好的","没问题","不用客气"};
		//reply index 1 -- what's else
		replies[1]=new String[]{"没有别的了吗","可以了吗"};
		//reply index 2 -- who am i
		replies[2]=new String[]{"你是我的宝贝甜蜜饯","你是"+master+",不过你叫什么我就不知道了"};
		//reply index 3 -- who are u
		replies[3]=new String[]{"我是"+name+",宇宙第一管家，越用越贴心"};
		//reply index 4 -- i like u
		replies[4]=new String[]{"我也喜欢你","对不起，"+master+",我只是一个机器","好巧"};
		//reply index 5 -- do u have a bf
		replies[5]=new String[]{"其实我和空调中控系统以为眉来眼去两个月了","单身才能做好管家！"};
		//reply index 6 -- can u speak other language
		replies[6]=new String[]{"我什么语言都会说，就是我的工程师没有把选择权交给用户而已",
				"你要是真的有需要，就去找我的工程师"};
		//reply index 7 -- nice weather
		replies[7]=new String[]{"还行吧，对我来讲无所谓","这么好的天气应该出去走走，家里有我照顾",
				"你开心就好"};
		//reply index 8 -- i am busy today
		replies[8]=new String[]{"那有我可以帮忙的吗？","需要一些"};
		//reply index last -- still learning,function limited
		replies[9]=new String[]{"抱歉我还理解不了你说的话","我还在学习，现在还听不懂你说的话"};
		
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
	//to be continuing
	public String dealWithReply(String cmd){
		int reply = -1;
		//确定返回种类
		//随机选取返回语句
		int randIndex =-1;
		word = replies[reply][randIndex];
		return word;
	}
	//main function.差好多啊
	public void step(String cmd){
		//先处理Devices
		cmdFormat = dealWithDevices(cmd);
		//有结果的话发送数据，规定回复文本种类为job done
		if (cmdFormat.equals("#")==false){
			
		}
		//没有结果的话，处理一下，判断回复种类
		//反馈文本
	}
	//差一个后台处理，控制自动判断的函数
}
