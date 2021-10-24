package comp490.logs;

import org.eclipse.jdt.core.dom.MethodInvocation;

public class SLF4JLoggingStatement extends LoggingStatement{
	
	public SLF4JLoggingStatement(MethodInvocation methodInvocation, String message, String level) {
		super(methodInvocation, message, level);
	}

}
