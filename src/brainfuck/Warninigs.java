package brainfuck;

import java.util.ArrayList;

public class Warninigs {

	private static ArrayList<String> warninigs = new ArrayList<String>();
	
	public static void add(String line) {
		if(warninigs.contains(line)) return;
		warninigs.add(line);
	}
	
	public static void print() {
		warninigs.forEach(Log::warn);
	}
}
