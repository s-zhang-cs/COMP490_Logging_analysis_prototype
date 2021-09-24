package comp490.logs;

import org.eclipse.jdt.core.dom.MethodInvocation;

public class JavaUtilLoggingStatement extends LoggingStatement{
	
	public JavaUtilLoggingStatement(MethodInvocation methodInvocation, String message, String loggingLevel) {
		super(methodInvocation, message, loggingLevel);
	}
	
}
