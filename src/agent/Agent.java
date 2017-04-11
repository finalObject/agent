package agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// @author finalObject
//		   http://www.finalobject.cn
//         i@finalobject.cn
//         https://github.com/finalObject
// @date 2017.4.11 21:03:32
// @version 1.0
public class Agent {
//单条指令的解析
//近几条指令的记忆功能
//事件记忆功能（某个条件下，包括环境调节。进行某种操作）
	private String[] hisCmd;//记录历史指令
	private ArrayList<ArrayList<String>> relpy;//面对指令的不同回复内容（索引数组
	private Device lastDevice;//记录上一条指令操作的设备，可能下一条指令可以不说明设备直接操作
	private Map<String,Device> devices;//拥有设备，记录当前状态，可以进行的操作
	
	public static void main(String[] args){
		String[] a={};
		ArrayList<ArrayList<String>> b=new ArrayList<>();
		b.add(new ArrayList<>());
		b.get(0).add("Hello");
		b.add(new ArrayList<>());
		b.get(1).add("World");
		System.out.println(b.get(0).get(0)+" "+b.get(1).get(0));
		Map<String,Integer> device = new HashMap<>();
		device.put("表", 2);
		System.out.println(device.get("表"));
	}
}
