package comp490.logs;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class LoggingStatementFactory {
	
	/**
	 * Common java logging frameworks
	 *
	 */
	public enum LoggingFramework {
		JAVAUTILLOGGING,
		LOG4J2,
		SLF4J
	}
	
	/**
	 * Setting default framework to SLF4J (since it is a wrapper of logging frameworks and 
	 * is more general)
	 * 
	 */
	public static LoggingFramework loggingFramework = LoggingFramework.SLF4J;
	
	/**
	 * Java.util.logging methods 
	 * 
	 */
	private final static Set<String> javaUtilLoggingMethodNames = new HashSet<>(Arrays.asList(
			"severe",
			"warning",
			"info",
			"config",
			"fine",
			"finer",
			"finest",
			"log",
			"logp",
			"logrb"
			));
	
	/**
	 * Log4j methods
	 */
	private final static Set<String> log4j2MethodNames = new HashSet<>(Arrays.asList(
			"fatal",
			"error",
			"warn",
			"trace",
			"debug",
			"info",
			"log"
			));
	
	/**
	 * SLF4J methods 
	 */
	private final static Set<String> slf4jMethodNames = new HashSet<>(Arrays.asList(
			"debug",
			"error",
			"info",
			"trace",
			"warn"
			));
	
	/**
	 * Change the logging framework  
	 */
	public static void setLoggingFramework(LoggingFramework lf) {
		loggingFramework = lf;
	}
	
	/**
	 * Determine if a certain method invocation is a loggingMethod from one of the
	 * above frameworks
	 * 
	 * @return true if the method invocation is a logging method, false otherwise
	 */
	public static boolean isLoggingStatement(MethodInvocation methodInvocation) {
	
	String name = methodInvocation.getName().getIdentifier();
	IMethodBinding binding = methodInvocation.resolveMethodBinding();
	if(binding == null) {
		return false;
	}
	String className = binding.getDeclaringClass().getName();
	if(!className.equals("Logger")) {
		return false;
	}
	List<?> arguments = methodInvocation.arguments();
	if(arguments == null) {
		return false;
	}

	switch(loggingFramework) {
		case JAVAUTILLOGGING:
			return javaUtilLoggingMethodNames.contains(name);
		case LOG4J2:
			return log4j2MethodNames.contains(name);
		case SLF4J:
			return slf4jMethodNames.contains(name);
		default:
			return false;
		}
	}
	
}
