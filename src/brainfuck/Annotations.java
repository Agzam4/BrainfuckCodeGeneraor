package brainfuck;

public interface Annotations {

	/**
	 * Can be used for generation brainfuck code
	 */
	public @interface Brainfucked {
		
	}

	/**
	 * It means: "pls not change variable"
	 */
	public @interface Final {
		
	}
	
	/**
	 * NEVER USE FOR GENERATION OF BRAINFUCK CODE<br>
	 * NEVER DO SOMETHING LIKE THIS:<br>
	 * <code>
	 * Byte v = bf.variable("v");<br>
	 * ...<br>
	 * for(int i = 0; i < v.value(); i++) {<br>
	 * &emsp;&emsp;Byte e = bf.variable("e" + i);<br>
	 * &emsp;&emsp;...<br>
	 * }<br>
	 * </code>
	 */
	public @interface Debug {
		
	}
	

	@java.lang.annotation.Documented
	@java.lang.annotation.Retention(value=java.lang.annotation.RetentionPolicy.RUNTIME)
	@java.lang.annotation.Target(value={java.lang.annotation.ElementType.CONSTRUCTOR,java.lang.annotation.ElementType.FIELD,java.lang.annotation.ElementType.LOCAL_VARIABLE,java.lang.annotation.ElementType.METHOD,java.lang.annotation.ElementType.PACKAGE,java.lang.annotation.ElementType.MODULE,java.lang.annotation.ElementType.PARAMETER,java.lang.annotation.ElementType.TYPE})
	public @interface WIP {
		
	}
}
