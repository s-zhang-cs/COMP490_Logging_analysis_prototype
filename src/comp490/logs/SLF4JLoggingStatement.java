package comp490.logs;

import org.eclipse.jdt.core.dom.MethodInvocation;

public class SLF4JLoggingStatement extends LoggingStatement{
	public SLF4JLoggingStatement(MethodInvocation methodInvocation, String message) {
		super(methodInvocation, message);
		// TODO Auto-generated constructor stub
	}

	enum LoggingLevel {
		DEBUG,
		ERROR,
		INFO,
		TRACE,
		WARN
	};

	LoggingLevel loggingLevel;
	
	
}
