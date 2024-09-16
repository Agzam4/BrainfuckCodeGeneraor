package main;

import brainfuck.*;
import brainfuck.Byte;

public class Brainfucker {
	
	public static Brainfuck brainfuck;

	public static void main(String[] args) throws Exception {
		/**
		 * Config of program
		 */
		CodeBuilder.format = false; // if final result will be with tabs and new lines
		
		/**
		 * Example code for generating brainfuck code for searching maximum increasing sequence
		 */

		String input = "7\n10 20 3 0 3 4 8\n"; // input for brainfuck code/generator
		
		/**
		 * Brainfuck.java allow you to generates and executes code at the same time
		 * that can help you with debugging
		 */
		brainfuck = new Brainfuck(input); // creating new brainfuck generator
		brainfuck.coments = false; // is comments in code endabled, you can place comments using "brainfuck.comment("My comment");"
		brainfuck.comment(input);
		// You can set maximum in minimum value (values are including) of one cell (pls made it before generating code)
		brainfuck.minValue = 0; 
		brainfuck.maxValue = 0xff;
		
		// declarating new variable
		// names of variables can be same they just for debugging
		// declarated variable value is always 0
		// declarated variables in cycles must be deleted to prevent memory leaks:
		// while(...) {
		// 		Byte b = brainfuck.variable("b");
		//		...
		//		b.delete();
		// }
		// now supported only one-cell (Byte) variables 
		// Byte can store more than byte [0-255] if size of cell will be increased
		// negative numbers math also unsupported now
		Byte length = brainfuck.variable("length"); // sequence length
		
		// You can print in brainfuck runntime using brainfuck.log(format, arguments)
		// if brainfuck code not be executed for this line it will not log anything,
		// use Log.info(...) to print something anyway
		// 
		// @ symbols will be replaced on arguments 
		// Length now is zero it means with you will saw "Hello brainfuck!" but not "Goodbye brainfuck!"
		brainfuck.ifZeroElse(length, () -> {
			brainfuck.log("Hello @!", "brainfuck"); 
			Log.info("It will be printed anyway #@", 1);
		}, () -> {
			brainfuck.log("Goodbye @!", "brainfuck"); 
			Log.info("It will be printed anyway #@", 2);
		});
		
		// reading all characters before '\n' as integer to new variable
		BrainfuckIO.readInt(length, '\n'); 
		length.add(-2); // skipping first number because it will be read to "last" and last numbers because stop-character of it is '\n'
		
		Byte last = brainfuck.variable("last"); // last array element (used for checking difference)
		BrainfuckIO.readInt(last, ' ');	// now stop character will be ' ' because array written in one line

		Byte max = brainfuck.variable("max"); // max increasing sequence length
		Byte count = brainfuck.variable("count"); // current increasing sequence length
		Byte a = brainfuck.variable("i"); // current sequence element variable, value to it will be set in cycle
		
		brainfuck.repeatWhile(length, () -> { // while(length != 0)
			// You can create java method for same blocks of code
			logicForNextElement(a, ' ', last, count, max);
			last.reset();
			last.set(a);
			
			length.add(-1);
		});
		
		logicForNextElement(a, '\n', last, count, max);
		BrainfuckIO.printInt(max); // printing variable value as integer
		
		/*
		 * Well now code for searching maximum increasing sequence is ready
		 * let's print it
		 */
		String code = brainfuck.toString();
		Log.result(code + "\n"); // printing green
		
		/**
		 * You can create and dump of brainfuck runtime's memory anytime (including runnabe)
		 * It will be shown all used memory cells with names of variables
		 * And also it show output of programm
		 */
		Log.info(brainfuck.dump());
		
		/**
		 * Printing information about unsupported operations and etc
		 */
		Warninigs.print();
	}

	private static void logicForNextElement(Byte a, char stopCharacter, Byte last, Byte count, Byte max) {
		BrainfuckIO.readInt(a, stopCharacter);
		brainfuck.ifLessThanElse(last, a, () -> { // if last < a
			count.add(1); // add 1 to vriable's value
			// searching max
			brainfuck.ifLessThanElse(count, max, () -> {
				// if count < max do not nothing
			}, () -> {
				// but else set count to max
				max.reset(); // setting value to 0
				max.set(count);  // copying value from "count" to "max"
			});
		}, () -> { // else
			count.reset(); 
			count.add(1);
		});
	}
	
}
