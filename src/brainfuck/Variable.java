package brainfuck;

import brainfuck.Annotations.Brainfucked;
import brainfuck.Annotations.Debug;
import brainfuck.Errors.DeletedVariableManipulation;

public class Variable {

	public final String name; // name of variable
	public final int size; // size of variable in cells (bytes)
	public final int index; // start variable's cell index in memory
	protected final Brainfuck brainfuck;
	private boolean exist = true;
	
	protected Variable(Brainfuck brainfuck, String name, int size, int index) {
		this.brainfuck = brainfuck;
		this.name = name.replace('.', '/');
		this.size = size;
		this.index = index;
		
		reset();
		if(!name.startsWith("$")) Log.run("@ declarated at @", this.name, index);
	}

	/**
	 * Add value to variable (can be negative)
	 */
	@Brainfucked
	public void add(int i) {
		if(isDeleted()) throw new DeletedVariableManipulation("change");
		select();
		brainfuck.addValue(i);
		if(size > 1) Warninigs.add("[TODO] 'add' in Varrible.java not implemented correctly for varibles with more than 1 byte");
	}

	/**
	 * Set value to 0
	 */
	@Brainfucked
	public void reset() {
		if(isDeleted()) throw new DeletedVariableManipulation("change");
		brainfuck.comment("{" + name + " reset}");
		select();
		brainfuck.resetCell();
		if(size > 1) Warninigs.add("[TODO] 'reset' in Varrible.java not implemented correctly for varibles with more than 1 byte");
	}
	
	/**
	 * Selecting cell with start index of variable
	 */
	@Brainfucked
	public void select() {
		if(isDeleted()) throw new DeletedVariableManipulation("select");
		brainfuck.setIndex(index);
	}

	@Brainfucked
	public void delete() {
		if(isDeleted()) throw new DeletedVariableManipulation("delete already deleted");
		for (int i = 0; i < size; i++) {
			brainfuck.variables[index + i] = null;
		}
		exist = false;
	}

	@Debug
	public boolean isExist() {
		return exist;
	}
	
	@Debug
	public boolean isDeleted() {
		return !exist;
	}
	
	@Override
	public String toString() {
		long value = 0;
		for (int i = 0; i < size; i++) {
			value |= brainfuck.getValue(i + index) << (i*8);
		}
		return name + "=" + Long.toString(value);
	}
}
