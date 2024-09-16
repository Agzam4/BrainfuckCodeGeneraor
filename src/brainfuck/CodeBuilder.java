package brainfuck;

public class CodeBuilder {
	
	public static boolean format = true;
	
	StringBuilder builder = new StringBuilder();

	int deepth;
	
	public void append(char c) {
		builder.append(c);
	}

	public void append(String string) {
		builder.append(string);
	}

	public void append(CodeBuilder code) {
		builder.append(code);
	}
	
	
	@Override
	public String toString() {
		return builder.toString();
	}
	
	public String format() {
		if(format) {
			StringBuilder formated = new StringBuilder();
			int deepth = 0;
			for (int b = 0; b < builder.length(); b++) {
				char c = builder.charAt(b);
				if(c == '\n') continue;
				if(c == '[' || c == ']') {
					if(c == ']') {
						deepth--;
					}
					formated.append('\n');
					for (int i = 0; i < deepth; i++) {
						formated.append("    ");
					}
					formated.append(c);
					if(c == '[') deepth++;
					formated.append('\n');
					for (int i = 0; i < deepth; i++) {
						formated.append("    ");
					}
					continue;
				}
				formated.append(c);
			}
			return formated.toString();//.replaceAll("@", "[-]");
		}
		return builder.toString();//.replaceAll("@", "[-]");
	}

}
