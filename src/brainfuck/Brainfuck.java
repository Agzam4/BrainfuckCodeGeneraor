package brainfuck;

import java.util.Scanner;

import brainfuck.Annotations.Brainfucked;
import brainfuck.Annotations.Debug;

public class Brainfuck {

	public boolean shortVersion = false; 
	public boolean coments = false; 
	/* Including */
	public int minValue = 0, maxValue = 255; 
	
	private int[] buffer = new int[3000];
	protected boolean onlyCode = false;
	
	private int index = 0;
	
	private String input = "";
	private int inputIndex = 0;
	
	private CodeBuilder code = new CodeBuilder();

	public Brainfuck() {}
	
	public Brainfuck(String input) {
		Log.run("=====[ BRAINFUCK runnable ]===================================");
		this.input = input;
	}
	
	Variable[] variables = new Variable[buffer.length];
	
	private Byte $input;

	private Scanner scanner = new Scanner(System.in);
	private StringBuilder output = new StringBuilder();
	
	protected Byte $input() {
		if($input == null) $input = variable("$input");
		return $input;
	}
	
	/**
	 * @param name - name of variable
	 * @param size - amount of cells
	 */
	@Brainfucked
	public Variable variable(String name, int size) {
		// searching free memory
		for (int i = 0; i < variables.length; i++) {
			if(variables[i] != null) continue;
			boolean free = true;
			for (int j = 0; j < size; j++) {
				if(variables[i+j] != null) {
					free = false;
					break;
				}
			}
			if(!free) continue;
			Variable variable = new Variable(this, name, size, i);
			for (int k = 0; k < size; k++) { // take up memory for a variable
				variables[variable.index + k] = variable;
			}
			return variable;
		}
		throw new OutOfMemoryError("Not enoth memory for declarating varrible");
	}

	/**
	 * Creates array of cell that following each other
	 * @param name
	 * @param size
	 */
	@Brainfucked
	public Byte[] byteArray(String name, int size) {
		for (int i = 0; i < variables.length; i++) {
			if(variables[i] != null) continue;
			boolean free = true;
			for (int j = 0; j < size; j++) {
				if(variables[i+j] != null) {
					free = false;
					break;
				}
			}
			if(!free) continue;
			Byte[] bs = new Byte[size];
			for (int k = 0; k < size; k++) { // take up memory for a variable
				bs[k] = new Byte(this, name + "/" + k, i+k);
			}
			return bs;
		}
		throw new OutOfMemoryError("Not enoth memory for declarating array");
	}
	
	
	/**
	 * @param name - name of variable
	 */
	@Brainfucked
	public Byte variable(String name) {
		// searching free memory
		for (int i = 0; i < variables.length; i++) {
			if(variables[i] != null) continue;
			Byte variable = new Byte(this, name, i);
			variables[variable.index] = variable;
			return variable;
		}
		throw new OutOfMemoryError("Not enoth memory for declarating varrible");
	}
	
	/**
	 * Repeating {@code run} while {@code var != 0}
	 */
	@Brainfucked
	public void repeatWhile(Byte var, Runnable run) {
		var.select();
		CodeBuilder parent = code;
		parent.append('[');
		boolean runned = false;
//		dump();
		while (buffer[index] != 0 && !onlyCode) {
			code = new CodeBuilder();
			run.run();
			var.select();
			runned = true;
		}
		if(!runned) {
//			Log.run("{Only code}");
//			comment("{skip start}");
			boolean oc = onlyCode;
			int ind = index;
			if(!var.toBoolean()) onlyCode = true; // is no one loop has been runned only code will be added 
			code = new CodeBuilder();
			comment("{skip start}");
			var.select();
			run.run();
			var.select();
			comment("{skip end}");
			onlyCode = oc;
			if(!onlyCode) {
				index = ind;
//				Log.run("{NOT оnly code}");
			}
		}
		
		parent.append(code);
		parent.append(']');
		code = parent;
	}
	
	/**
	 * Running {@code run} if {@code var != 0}
	 */
	@Brainfucked
	public void ifNotZero(Byte var, Runnable run) {
		Byte lock = variable("$bf/ifByte/circle");
		lock.set(var);
		repeatWhile(lock, () -> {
			run.run();
			lock.reset();
		});
		lock.delete();
	}

	/**
	 * Running {@code run} if {@code var == 0}
	 */
	@Brainfucked
	public void ifZeroElse(Byte var, Runnable runZero, Runnable runNoZero) {
		Byte inversed = variable("$bf/ifByte/circle");
		inversed.add(1);
		ifNotZero(var, () -> {
			runNoZero.run();
			inversed.add(-1);
		});
		ifNotZero(inversed, runZero);
		inversed.delete();
	}
	
	/**
	 * Running {@code run} if {@code var == 0}
	 */
	@Brainfucked
	public void ifZero(Byte var, Runnable run) {
		Byte inversed = variable("$bf/ifByte/circle");
		inversed.add(1);
		ifNotZero(var, () -> inversed.add(-1));
		ifNotZero(inversed, run);
		inversed.delete();
	}
	
	/**
	 * Repeating {@code run} while {@code cell[index] != 0}
	 */
//	@Brainfucked
//	public void repeatWhile(Runnable run) {
//		CodeBuilder parent = code;
//		parent.append('[');
//		boolean runned = false;
////		dump();
//		while (buffer[index] != 0 && !onlyCode) {
//			code = new CodeBuilder();
//			run.run();
//			runned = true;
//		}
//		if(!runned) {
////			Log.run("{Only code}");
////			comment("{skip start}");
//			boolean oc = onlyCode;
//			int ind = index;
//			onlyCode = true;
//			code = new CodeBuilder();
//			comment("{skip start}");
//			run.run();
//			comment("{skip end}");
//			onlyCode = oc;
//			if(!onlyCode) {
//				index = ind;
////				Log.run("{NOT оnly code}");
//			}
//		}
//		
//		parent.append(code);
//		parent.append(']');
//		code = parent;
//	}

	/**
	 * Running {@code ifRun} if {@code var == value} and {@code elseRun} if not
	 */
	@Brainfucked
	public void ifEqualElse(Byte var, int value, Runnable ifRun, Runnable elseRun) {
		/**
		 * statement = 1;
		 * copy = var
		 * copy -= value
		 * while(copy != 0) {
		 * 		statement = 0;
		 * 		copy = 0;
		 * }
		 * while(statement != 0) {
		 * 		
		 * 		
		 * 		statement = 0;
		 * }
		 */
		comment("{begin if}");
		Byte statement = variable("$bf/tmp/if/statement");
		Byte copy = variable("$bf/tmp/if/copy");
		
		statement.add(1);
		copy.set(var);
		comment("{(" + copy.index + ") remove " + value + "}");
		copy.add(-value);
		comment("{else}");
		repeatWhile(copy, () -> {
			comment("{(" + statement.index + ") = 0}");
			statement.add(-1);
			comment("{copy.reset()}");
			copy.reset();
			elseRun.run();
		});
		comment("{if}");
		repeatWhile(statement, () -> {
			ifRun.run();
			statement.add(-1);
		});
		statement.delete();
		copy.delete();
		comment("{end if}");
	}

	/**
	 * Running {@code ifRun} if {@code x < y} and {@code elseRun} if not<br>
	 * <a href="https://stackoverflow.com/questions/6168584/brainfuck-compare-2-numbers-as-greater-than-or-less-than">
	 * 	Stackoverflow
	 * </a>
	 */
	@Brainfucked
	public void ifLessThanElse(Byte x, Byte y, Runnable ifRun, Runnable elseRun) {
        /*
         *  [0 1 0 x y 0]
         */
		Byte[] arr = byteArray("$bf/lessthan/array", 6);
		arr[1].add(1);
		arr[3].set(x);
		arr[4].set(y);
		// if x == 0 and y == 0
		arr[3].add(1);
		arr[4].add(1);
		
		arr[3].select();
		comment("{magic loop}");
		code.append("[->-[>]<<]");
		comment("{magic loop end}");
		code.append("<[-");
		int min = Math.min(x.value(), y.value()) + 1;
		boolean less = x.value() < y.value();
//		if(less) {
//			index = arr[2].index; // setting it by hand
//		} else {
//			index = arr[1].index; // setting it by hand
//		}
		buffer[arr[3].index] -= min;
		buffer[arr[4].index] -= min;


		// if x >= y block
		boolean oc = onlyCode;
		onlyCode = less;
		
		index = arr[1].index; // setting it by hand
		elseRun.run();
		arr[1].select();
		onlyCode = oc;
		// end

		code.append("]<[-<");
		
		// if x < y block
		oc = onlyCode;
		onlyCode = !less;
		
		index = arr[0].index; // setting it by hand
		ifRun.run();
		arr[0].select();
		onlyCode = oc;
		code.append("]");
	
		for (int i = 0; i < arr.length; i++) {
			arr[i].delete();
		}
	}

	/**
	 * Set index
	 * @param index
	 */
	public Brainfuck setIndex(int index) {
		while (index != this.index) {
			if(this.index > index) prevIndex();
			if(this.index < index) nextIndex();
		}
		return this;
	}

	/**
	 * Add value to current index
	 */
	public Brainfuck addValue(int value) {
		if(value == 0) return this;
		if(value > 0) {
			for (int i = 0; i < value; i++) {
				add();
			}
			return this;
		}
		for (int i = 0; i < -value; i++) {
			remove();
		}
		return this;
	}
	
	/**
	 * Set value to 0
	 */
	public Brainfuck resetCell() {
		code.append(shortVersion ? "@" : "[-]");
		if(!onlyCode) buffer[index] = 0;
		return this;
	}
	
	/**
	 * Increase value by 1
	 */
	public Brainfuck add() {
		code.append('+');
		if(!onlyCode) {
			buffer[index]++;
			if(buffer[index] > maxValue) buffer[index] = minValue;
		}
		return this;
	}

	/**
	 * Increase value by -1
	 */
	public Brainfuck remove() {
		code.append('-');
		if(!onlyCode) {
			buffer[index]--;
			if(buffer[index] < minValue) buffer[index] = maxValue;
		}
		return this;
	}

	/**
	 * Read value to current cell
	 */
	@Brainfucked
	public Brainfuck read() {
		code.append(',');
		if(!onlyCode) {
//			try {
				if(inputIndex >= input.length()) {
					input = scanner.nextLine() + "\n";
					inputIndex = 0;
				}
				buffer[index] = input.charAt(inputIndex++);//inputIndex < input.length() ? input.charAt(inputIndex++) : (char) System.in.read();
//				Log.run("Inputed char: '@' (@)", (char)buffer[index], (int)buffer[index]);
				//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		}
		return this;
	}
	
	/**
	 * Print current value
	 */
	@Brainfucked
	public Brainfuck print() {
		code.append('.');
		if(!onlyCode) {
			output.append((char)buffer[index]);
		}
		return this;
	}

	/**
	 * Move index on +1
	 */
	@Brainfucked
	private Brainfuck nextIndex() {
		code.append('>');
		index++;
		return this;
	}

	/**
	 * Move index on -1
	 */
	@Brainfucked
	private Brainfuck prevIndex() {
		code.append('<');
		index--;
		return this;
	}
	
	@Override
	public String toString() {
		System.out.println();
		StringBuilder str = new StringBuilder();
		str.append(code.format());
//		str.append(dump());
		
		return str.toString();
	}
	
	@Debug
	public String dump() {
		StringBuilder str = new StringBuilder();
		int max = 0;
		int maxVarName = 0;
		for (int i = 0; i < buffer.length; i++) {
			if(buffer[i] != 0 || variables[i] != null) max = Math.max(max, i);
			if(variables[i] == null) continue;
			maxVarName = Math.max(maxVarName, variables[i].name.length());
		}
		str.append("\nIndex: ");
		str.append(index);
		str.append("\nMemory (" + (max+1) + "/" + buffer.length + " bytes):\n");
		for (int i = 0; i <= max; i++) {
			str.append("#");
			str.append((int)i);
			str.append(" \t");
		}
		str.append("\n");
		for (int i = 0; i <= max; i++) {
			str.append("[");
			str.append((int)buffer[i]);
			str.append("]\t");
		}
		str.append("\n");
		for (int i = 0; i <= max; i++) {
			if(buffer[i] == '\n') {
				str.append("[\\n]\t");
			} else {
				str.append("[");
				str.append((char)buffer[i]);
				str.append("]\t");
			}
		}
		
		for (int h = 0; h < maxVarName; h++) {
			str.append("\n");
			for (int i = 0; i <= max; i++) {
				str.append(" ");
				str.append(variables[i] == null || h >= variables[i].name.length()  ? ' ' : variables[i].name.charAt(h));
				str.append(" \t");
			}
		}
		str.append("\nResult: ");
		str.append(output);
		
		return str.toString();
	}

	@Brainfucked
	public void read(Byte in) {
		in.select();
		read();
	}

	@Debug
	public int getIndex() {
		return index;
	}

	@Debug
	public int getValue(int index) {
		return buffer[index];
	}

	@Brainfucked
	public void comment(String string) {
		if(!coments) return;
		string = string.replace('.', ':')
				.replace('-', '–')
				.replace(',', ';')
				.replace('+', '\u2795')
				.replace('<', '\u2039')
				.replace('>', '\u203a')
				.replace('@', '\u00a9')
				.replace('[', '\u2560')
				.replace(']', '\u2563')
					  ;
		code.append(string);
	}

	@Debug
	public void log(Object obj) {
		if(!onlyCode) Log.run(obj);
	}

	@Debug
	public void log(String str, Object... args) {
		if(!onlyCode) Log.run(str, args);
	}








}
