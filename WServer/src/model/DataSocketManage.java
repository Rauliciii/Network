package model;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


public class DataSocketManage {
	private static Map<String, Socket> map = new HashMap<String, Socket>();

	public static Socket getSocketFromString(String s) {
		return map.get(s);
	}
	
	public static boolean add(String nickname, Socket dos) {
		map.put(nickname, dos);
		return true;
	}
}
