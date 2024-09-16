package brainfuck;

import brainfuck.Annotations.Brainfucked;
import brainfuck.Annotations.Debug;
import brainfuck.Annotations.Final;
import brainfuck.Errors.DeletedVariableManipulation;

/**
 * Variable with 1-cell size (1 byte)<br>
 */
public class Byte extends Variable {

	@Brainfucked
	public Byte(Brainfuck brainfuck, String name, int index) {
		super(brainfuck, name, 1, index);
	}
	
	@Override
	public void add(int i) {
		super.add(i);
	}

	@Debug
	public boolean toBoolean() {
		if(isDeleted()) throw new DeletedVariableManipulation("read");
		return brainfuck.getValue(index) != 0;
	}
	
	@Debug
	public char toChar() {
		if(isDeleted()) throw new DeletedVariableManipulation("read");
		return (char) brainfuck.getValue(index);
	}

	@Debug
	public int value() {
		return brainfuck.getValue(index);
	}

	@Brainfucked
	public void multiply(int mul) {
		if(isDeleted()) throw new DeletedVariableManipulation("change");
		Byte tmp2 = brainfuck.variable("$bf/varible/multiply/tmp2");
		tmp2.add(mul);
		multiply(tmp2);
		tmp2.delete();
	}

	@Brainfucked
	public void multiply(Byte mul) {
		if(isDeleted()) throw new DeletedVariableManipulation("change");
		if(size > 1) Warninigs.add("[TODO] 'multiply' in Varrible.java not implemented correctly for varibles with more than 1 byte");
		/**
		 * temp0[-] 
		 * temp1[-]
		 * x[temp1 + x-]
		 * temp1[
		 * mul[x + temp0 + mul-]temp0[mul + temp0-]
		 * temp1-]
		 * 
		 * temp0 = 0
		 * temp1 = 0
		 * x -> temp1
		 * for [0,temp1] {
		 * 		mul -> x, temp0
		 * 		temp0 -> mul
		 * }
		 */
		select();

		Byte tmp0 = brainfuck.variable("$bf/varible/multiply/tmp0");
		Byte tmp1 = brainfuck.variable("$bf/varible/multiply/tmp1");
		brainfuck.repeatWhile(this, () -> {
			tmp1.add(1);
			add(-1);
		});
		brainfuck.repeatWhile(tmp1, () -> {
			brainfuck.repeatWhile(mul, () -> {
				add(1);
				tmp0.add(1);
				mul.add(-1);
			});
			brainfuck.repeatWhile(tmp0, () -> {
				mul.add(1);
				tmp0.add(-1);
			});
			tmp1.add(-1);
		});
		tmp0.delete();
		tmp1.delete();
	}


	@Brainfucked
	public void set(Byte var) {
		if(isDeleted()) throw new DeletedVariableManipulation("change");
		Byte tmp = brainfuck.variable("$bf/tmp/byte/copy");
		brainfuck.comment("{begin copy value from #" + var.index + " to #" + index + " with tmp #" + tmp.index + "}");
		reset();
		brainfuck.repeatWhile(var, () -> {
			brainfuck.comment("{add 1}");
			add(1);
			brainfuck.comment("{add 1 to tmp}");
			tmp.add(1);
			brainfuck.comment("{remove 1}");
			var.add(-1);
		});
		brainfuck.repeatWhile(tmp, () -> {
			var.add(1);
			tmp.add(-1);
		});
		tmp.delete();
		brainfuck.comment("{end copy}");
	}

	@Brainfucked
	public void print() {
		if(isDeleted()) throw new DeletedVariableManipulation("print");
		select();
		brainfuck.print();
	}

	/**
	 * Divide this variable by "divider"
	 * this /= divider
	 * !!! IMPORTANT !!!
	 * Don't forgot delete mod (mainder of the division)
	 * @param divider
	 * @return mod (remainder of the division)
	 */
	@Brainfucked
	public Byte divide(int divider) {
		Byte $divider = brainfuck.variable("$bf/byte/divide/divider");
		$divider.add(divider);
		Byte mod = divide($divider);
		$divider.delete();
		return mod;
	}

	
	/**
	 * Divide this variable by "divider"
	 * this /= divider
	 * !!! IMPORTANT !!!
	 * Don't forgot delete mod (mainder of the division)
	 * @param divider
	 * @return mod (remainder of the division)
	 */
	@Brainfucked
	public Byte divide(@Final Byte divider) {
		Byte mod = brainfuck.variable("$bf/byte/divide/mod");
		Byte umod = brainfuck.variable("$bf/byte/divide/umod"); // divider - mod
		Byte div = brainfuck.variable("$bf/byte/divide/div");
		Byte lock = brainfuck.variable("$bf/byte/divide/lock");
		Byte tmpDivider = brainfuck.variable("$bf/byte/divide/lock"); // divider iterator

		lock.add(1);
		brainfuck.repeatWhile(lock, () -> {
			div.add(1); // amount of iterations
//			brainfuck.log("Iteration #@: @", div.value(), this.value());
			brainfuck.ifZeroElse(this, () -> { // cool its already zero
				lock.add(-1);
				umod.set(divider);
			}, () -> {
				tmpDivider.set(divider);
				tmpDivider.add(-1); // one step moved to end of while to we can catch zero
				brainfuck.repeatWhile(tmpDivider, () -> { // value -= divider (with break)
					add(-1);
					tmpDivider.add(-1);
					brainfuck.ifZero(this, () -> { // uh-huh is mean that value < divider
						umod.add(1);
						brainfuck.repeatWhile(tmpDivider, () -> {
							umod.add(1);
							tmpDivider.add(-1);
						});
						add(1); // reversed moved step
						lock.add(-1);
					});
				});
			});
			add(-1);
		});
		div.add(-1); // one iteration not full

		mod.set(divider);
		brainfuck.repeatWhile(umod, () -> {
			mod.add(-1);
			umod.add(-1);
		});

		reset();
		brainfuck.repeatWhile(div, () -> {
			add(1);
			div.add(-1);
		});
		
		umod.delete();
		div.delete();
		lock.delete();
		tmpDivider.delete();
		return mod;
	}

	
}
