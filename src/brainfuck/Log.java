package brainfuck;

public class Log {

	private static String info = "\u001B[36m[INFO]";
	private static String result = "\u001B[32m";
	private static String warning = "\u001B[33m[WARN]";
	private static String runnable = "\u001B[30m\u001B[47m[RUN]";
	private static String end = "\u001B[0m";

	public static void run(Object object) {
		print(runnable + format(" @", object) + end);
	}
	
	public static void run(String str, Object... args) {
		print(runnable + " " + format(str, args) + end);
	}

	public static void warn(Object object) {
		print(warning + format(" @", object) + end);
	}
	
	public static void warn(String str, Object... args) {
		print(warning + " " + format(str, args) + end);
	}

	public static void info(Object object) {
		print(info + format(" @", object) + end);
	}
	
	public static void info(String str, Object... args) {
		print(info + " " + format(str, args) + end);
	}

	public static void result(Object object) {
		print(result + format(" @", object) + end);
	}
	
	public static void result(String str, Object... args) {
		print(result + " " + format(str, args) + end);
	}
	
	static String last = "";
	static int duplicates = 0;
	public static void print(String str) {
		if(last.equals(str)) {
			duplicates++;
			if(duplicates == 1) System.out.print("\t");
			System.out.print(" " + duplicates);
			return;
		}
		System.out.print("\n" + str);
		last = str;
		duplicates = 0;
	}
	
	public static StringBuilder format(String str, Object... args) {
		StringBuilder formated = new StringBuilder();
		int ai = 0;
		for (int i = 0; i < str.length(); i++) {
			if(str.charAt(i) == '@') {
				formated.append(ai >= args.length ? "@" : (args[ai] == null) ? "null" : args[ai].toString());
				ai++;
				continue;
			}
			formated.append(str.charAt(i));
		}
		for (int i = formated.length(); i < 100; i++) {
			formated.append(' ');
		}
		return formated;
	}
	

	public static StringBuilder paint(CharSequence str) {
		StringBuilder painted = new StringBuilder();
		StringBuilder tag = new StringBuilder();
		boolean isTag = false;
		for (int i = 0; i < str.length(); i++) {
			if(str.charAt(i) == '[') {
				isTag = true;
				continue;
			}
			if(str.charAt(i) == ']') {
				isTag = false;
				if(tag.toString().equals("[white]")) {
					painted.append("" + tag);
				} else {
					tag.append('[');
					painted.append(tag);
					tag.append(']');
				}
				continue;
			}
			if(isTag) tag.append(str.charAt(i));
			else painted.append(str.charAt(i));
		}
		return painted;
	}
}
