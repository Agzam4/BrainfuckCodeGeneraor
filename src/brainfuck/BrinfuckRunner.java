package brainfuck;

import java.util.Stack;

import brainfuck.Annotations.WIP;

/**
 * IT'S BROKEN NOW
 */
@WIP
@Deprecated // to highlight
public class BrinfuckRunner {


	/**
	 * IT'S BROKEN NOW
	 */
	@WIP
	@Deprecated // to highlight
	public static void run(String code, String input) {
		char[] buffer = new char[30000];
		int index = 0;
		int inputIndex = 0;
		System.out.println("\nRunning:");
		int i = 0;
		Stack<Integer> loops = new Stack<Integer>();
		while (true) {
			if(i >= code.length()) break;
			char c = code.charAt(i);
			if(c == '@') {
				buffer[index] = 0;
			}
			if(c == '[') {
				loops.push(i);
				if(buffer[index] == 0) {
					int deepth = 0;
					for (int j = i; j < code.length(); j++) {
						if(code.charAt(j) == '[') deepth++;
						if(code.charAt(j) == ']') {
							deepth--;
							if(deepth == 0) {
								i = j+1;
								break;
							}
						}
					}
					continue;
				}
			}
			if(c == ']') {
				if(buffer[index] != 0) {
					i = loops.peek();
					continue;
				}
				loops.pop();
			}
			
			if(c == '>') index++;
			if(c == '<') index--;
			if(c == '+') buffer[index]++;
			if(c == '-') buffer[index]--;
			if(c == '.') System.out.print(buffer[index]);
			if(c == ',') {
				buffer[index] = input.charAt(inputIndex++);
			}
			i++;
		}
		StringBuilder str = new StringBuilder();
		int max = 0;
		for (i = 0; i < buffer.length; i++) {
			if(buffer[i] != 0) max = Math.max(max, i);
		}
		str.append("\nMemory (" + (max+1) + "/" + buffer.length + " bytes):\n");
		for (i = 0; i <= max; i++) {
			str.append("#");
			str.append((int)i);
			str.append(" \t");
		}
		str.append("\n");
		for (i = 0; i <= max; i++) {
			str.append("[");
			str.append((int)buffer[i]);
			str.append("]\t");
		}
		System.out.println(str);
	}
}
