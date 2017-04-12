package agent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
	private String[][] reply;//面对指令的不同回复内容（索引数组
	private Device[] devices;//拥有设备，记录当前状态，可以进行的操作
										//传感器状态，人的状态，也放在这里面
	public static void main(String[] args){
		int[] a;
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
		reply =new String[9][];
		//reply index 0 -- jobs done
		reply[0]=new String[]{"好的","没问题","不用客气"};
		//reply index 1 -- what's else
		reply[1]=new String[]{"没有别的了吗","可以了吗"};
		//reply index 2 -- who am i
		reply[2]=new String[]{"你是我的宝贝甜蜜饯","你是"+master+",不过你叫什么我就不知道了"};
		//reply index 3 -- who are u
		reply[3]=new String[]{"我是"+name+",宇宙第一管家，越用越贴心"};
		//reply index 4 -- i like u
		reply[4]=new String[]{"我也喜欢你","对不起，"+master+",我只是一个机器","好巧"};
		//reply index 5 -- do u have a bf
		reply[5]=new String[]{"其实我和空调中控系统以为眉来眼去两个月了","单身才能做好管家！"};
		//reply index 6 -- can u speak other language
		reply[6]=new String[]{"我什么语言都会说，就是我的工程师没有把选择权交给用户而已",
				"你要是真的有需要，就去找我的工程师"};
		//reply index 7 -- nice weather
		reply[7]=new String[]{"还行吧，对我来讲无所谓","这么好的天气应该出去走走，家里有我照顾",
				"你开心就好"};
		//reply index 8 -- i am busy today
		reply[8]=new String[]{"那有我可以帮忙的吗？","需要一些"};
		//reply index last -- still learning,function limited
		reply[9]=new String[]{"抱歉我还理解不了你说的话","我还在学习，现在还听不懂你说的话"};
		
		//初始化devices
		devices = new Device[]{
				new Device(1, "light",new String[]{"灯","电灯"}, false, 0),
				new Device(1, "door",new String[]{"门"}, false, 0),
				new Device(2, "fan", new String[]{"风扇","电扇"},true, 0),
				new Device(3, "musicSwitch", new String[]{"音乐","音响"},false, 0),
				new Device(4, "songs", new String[]{},true, 0),
				new Device(5,"intensity",null,true,0),
				new Device(6,"temperature",null,true,0)
		};
	}
	
}
