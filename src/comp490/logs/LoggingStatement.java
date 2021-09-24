package comp490.logs;

import org.eclipse.jdt.core.dom.MethodInvocation;

public abstract class LoggingStatement {

	private MethodInvocation methodInvocation;
	private String message;
	private String loggingLevel;
	
	public LoggingStatement(MethodInvocation methodInvocation, String message, String loggingLevel) {
		this.methodInvocation = methodInvocation;
		this.message = message;
		this.loggingLevel = loggingLevel;
	}
	
	public MethodInvocation getMethodInvocation() {
		return methodInvocation;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getLoggingLevel() {
		return loggingLevel;
	}
}
