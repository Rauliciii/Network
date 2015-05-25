package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Interpreter {

	private static final String DELIMITER = "_";
	private static List<String> list;

	public static List<String> interpretString(String string) {
		System.out.println("S: Interpreting the command");
		list = new ArrayList<String>();
		// first pos in the list: type of connection
		// second and third: parameters
		if (string.indexOf('_') == -1) {
			list.add(string);
		} else {
			StringTokenizer tokenizer = new StringTokenizer(string);
			list.add(0, tokenizer.nextToken(DELIMITER));
			list.add(1, tokenizer.nextToken(DELIMITER));
			list.add(2, tokenizer.nextToken(DELIMITER));
		}
		System.out.println(list);
		return list;

	}

}
