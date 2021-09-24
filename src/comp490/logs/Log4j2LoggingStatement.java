package comp490.logs;

import org.eclipse.jdt.core.dom.MethodInvocation;

public class Log4j2LoggingStatement extends LoggingStatement{
	
	public Log4j2LoggingStatement(MethodInvocation methodInvocation, String message, String level) {
		super(methodInvocation, message, level);
	}

}
