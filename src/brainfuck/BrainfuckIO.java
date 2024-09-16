package brainfuck;

import brainfuck.Annotations.Brainfucked;

public class BrainfuckIO {

	/**
	 * @param variable - variable for storage input value
	 * @param stop - end of input character
	 */
	@Brainfucked
	public static void readInt(Byte variable, char stop) {
		variable.reset();
		readAddInt(variable, stop);
	}

	/**
	 * @param variable - variable for storage input value
	 * @param stop - end of input character
	 */
	@Brainfucked
	public static void readAddInt(Byte variable, char stop) {
		Brainfuck bf = variable.brainfuck;
		
		Byte needNext = bf.variable("$bf/io/neednext");
		needNext.add(1); // set it to true

		bf.repeatWhile(needNext, () -> {
			Byte in = bf.$input();
			bf.read(in);
			bf.ifEqualElse(in, stop, () -> {
				needNext.add(-1); // stop reading if it stop-character
			}, () -> {
				in.add(-'0');
				variable.multiply(10);
				bf.repeatWhile(in, () -> { // TODO: math
					in.add(-1);
					variable.add(1);
				});
			});
		});
		needNext.delete();
	}

	/**
	 * Warn: can be slow for big nubers beacse division is slow
	 * @param value - value for printing
	 */
	@Brainfucked
	public static void printInt(Byte value) {
		Brainfuck bf = value.brainfuck;
		value.select();

		Byte length = bf.variable("$bf/io/printInt/length");
		Byte out = bf.variable("$bf/io/printInt/out");
		Byte v = bf.variable("$bf/io/printInt/v");
		Byte divider = bf.variable("$bf/io/printInt/divider");
		Byte i = bf.variable("$bf/io/printInt/i");
		v.set(value);
		
		bf.repeatWhile(value, () -> {
			value.divide(10).delete();
			length.add(1);
		});
		
		bf.ifEqualElse(length, 0, () -> {
			// printing 0
			value.reset();
			value.print();
		}, () -> {
			value.set(v);
			bf.repeatWhile(length, () -> {
				i.set(length);
				divider.reset();
				divider.add(1);
				i.add(-1);
				bf.repeatWhile(i, () -> {
					divider.multiply(10);
					i.add(-1);
				});
				out.set(value);
				Byte mod = out.divide(divider);
				out.add('0');
				out.print();
				value.set(mod);
				mod.delete();
				length.add(-1);
			});
		});
		
		value.set(v);
		
		length.delete();
		out.delete();
		v.delete();
		i.delete();
		divider.delete();
	}
}
