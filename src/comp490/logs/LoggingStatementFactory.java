package comp490.logs;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.StringLiteral;

public class LoggingStatementFactory {
	
	public enum LoggingFramework {
		JAVAUTILLOGGING,
		LOG4J2,
		SLF4J
	}
	
	public static LoggingFramework loggingFramework = LoggingFramework.SLF4J;
	
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
	
	private final static Set<String> log4j2MethodNames = new HashSet<>(Arrays.asList(
			"fatal",
			"error",
			"warn",
			"trace",
			"debug",
			"info",
			"log"));
	
	private final static Set<String> slf4jMethodNames = new HashSet<>(Arrays.asList(
			"debug",
			"error",
			"info",
			"trace",
			"warn",
			"log"));
	
	public static void setLoggingFramework(LoggingFramework lf) {
		loggingFramework = lf;
	}
	
	public static boolean isLoggingStatement(MethodInvocation methodInvocation) {
		IMethodBinding methodBinding = methodInvocation.resolveMethodBinding();
		if(methodBinding == null) {
			return false;
		}
		if(methodBinding.getDeclaringClass().getQualifiedName().equals("java.util.logging.Logger")) {
			return true;
		}
		if(methodBinding.getDeclaringClass().getQualifiedName().equals("org.slf4j.Logger")) {
			return true;
		}
		return false;
	}

//	public static LoggingStatement getLoggingStatement(MethodInvocation methodInvocation) {
//		
//		String name = methodInvocation.getName().getIdentifier();
//		String className = methodInvocation.resolveMethodBinding().getDeclaringClass().getName();
//		if(!className.equals("Logger")) {
//			return null;
//		}
//		List<?> arguments = methodInvocation.arguments();
//		if(arguments == null) {
//			return null;
//		}
//
//		switch(loggingFramework) {
//		case JAVAUTILLOGGING:
//			if(!javaUtilLoggingMethodNames.contains(name)) {
//				return null;
//			}
//			if(!name.equals("log")) {
//				if(name.equals("logp")) {
//					String level = ((QualifiedName)arguments.get(0)).getName().getIdentifier().toLowerCase();
//					String message = ((StringLiteral)arguments.get(3)).getEscapedValue();
//					return new JavaUtilLoggingStatement(methodInvocation, message, level);
//				}
//				if(name.equals("logrb")) {
//					String level = ((QualifiedName)arguments.get(0)).getName().getIdentifier().toLowerCase();
//					String message = ((StringLiteral)arguments.get(4)).getEscapedValue();
//					return new JavaUtilLoggingStatement(methodInvocation, message, level);
//				}
//				String message = ((StringLiteral)arguments.get(0)).getEscapedValue();
//				return new JavaUtilLoggingStatement(methodInvocation, message, name);
//			}
//			String level = ((QualifiedName)arguments.get(0)).getName().getIdentifier().toLowerCase();
//			String message = ((StringLiteral)arguments.get(1)).getEscapedValue();
//			return new JavaUtilLoggingStatement(methodInvocation, message, level);
//		case LOG4J2:
//			if(!log4j2MethodNames.contains(name)) {
//				return null;
//			}
//			if(!name.equals("log")) {
//				String message = ((StringLiteral)arguments.get(0)).getEscapedValue();
//				return new Log4j2LoggingStatement(methodInvocation, message, name);
//			}
//			if(name.equals("log")) {
//				String level = ((QualifiedName)arguments.get(0)).getName().getIdentifier().toLowerCase();
//				String message = ((StringLiteral)arguments.get(1)).getEscapedValue();
//				return new Log4j2LoggingStatement(methodInvocation, message, level);
//			}
//		case SLF4J:
//			if(!slf4jMethodNames.contains(name)) {
//				return null;
//			}
//			if(name.equals("log")) {
//				return null;
//			}
//		default:
//			return null;
//		}
//
//	}
}
