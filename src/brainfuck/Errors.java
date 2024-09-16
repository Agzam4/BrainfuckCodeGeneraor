package brainfuck;

public class Errors {

	public static class DeletedVariableManipulation extends VirtualMachineError {

		public DeletedVariableManipulation(String string) {
			super("Cannot " + string + "deleted variable");
		}

		private static final long serialVersionUID = -4869622734382157686L;
		
	}
	
}
