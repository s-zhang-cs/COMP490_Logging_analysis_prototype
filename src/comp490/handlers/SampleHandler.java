package comp490.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import comp490.patterns.MethodDeclarationPatterns;
import comp490.patterns.MethodInvocationPatterns;
import comp490.patterns.TypeDeclarationPatterns;

public class SampleHandler extends AbstractHandler {

	public static final String CONSOLE_NAME = "COMP490";
	private static MessageConsole myConsole;
	private static MessageConsoleStream out;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		SampleHandler.myConsole = findConsole(CONSOLE_NAME);
		SampleHandler.out = myConsole.newMessageStream();
		
		SampleHandler.printMessage("~~~~~~~~~~~~~~~~~~~~~~~~~~~ start~~~~~~~~~~~~~~~~~~~~~~~~~~~");

		TypeDeclarationPatterns typeDeclarationPatterns = new TypeDeclarationPatterns();
		SampleHandler.printMessage("Nbr of classes: " + typeDeclarationPatterns.countNbrOfClasses());
		SampleHandler.printMessage("Nbr of interfaces: " + typeDeclarationPatterns.countNbrOfInterfaces());
		
		MethodDeclarationPatterns methodDeclarationPatterns = new MethodDeclarationPatterns();
		methodDeclarationPatterns.extractCFG();
//		SampleHandler.printMessage("Nbr of methods: " + methodDeclarationPatterns.countNbrOfMethods());
//		SampleHandler.printMessage("Nbr of logging methods: " + methodDeclarationPatterns.countNbrOfLoggingMethods());
//		SampleHandler.printMessage("Nbr of searched methods: " + methodDeclarationPatterns.countNbrOfMethods("asdf"));
//		
//		MethodInvocationPatterns methodInvocationPatterns = new MethodInvocationPatterns();
//		methodInvocationPatterns.extractCG(null, null, "logMessage");
//		methodInvocationPatterns.extractICFG(null, null, "logMessage");
//		SampleHandler.printMessage("Nbr of method invocations: " + methodInvocationPatterns.countNbrOfMethodInvocations());
//		SampleHandler.printMessage("Nbr of logging method invocations: " + methodInvocationPatterns.countNbrOfLoggingMethodInvocations());
//		SampleHandler.printMessage("Nbr of searched method invocations: " + methodInvocationPatterns.countNbrOfMethodInvocations("asdf"));
//		
//		SampleHandler.printMessage("All the logging method declarations: ");
//		for(JavaModel.LoggingMethod i : JavaModel.getLoggingMethods()) {
//			SampleHandler.printMessage("method: " + i.getMethod().toString());
//			SampleHandler.printMessage("method line: " + i.getLineNbr());
//			SampleHandler.printMessage("method class: " + ((TypeDeclaration)i.getParentClass()).getName().getIdentifier());
//			SampleHandler.printMessage("method package: " + i.getParentPackage().toString());
//			SampleHandler.printMessage("============================================================");
//		}
//		
//		SampleHandler.printMessage("All the logging method invocations: ");
//		for(JavaModel.LoggingMethod i : JavaModel.getLoggingMethodInvocations()) {
//			SampleHandler.printMessage("method: " + i.getMethod().toString());
//			SampleHandler.printMessage("method line: " + i.getLineNbr());
//			SampleHandler.printMessage("method class: " + ((TypeDeclaration)i.getParentClass()).getName().getIdentifier());
//			SampleHandler.printMessage("method package: " + i.getParentPackage().toString());
//			SampleHandler.printMessage("============================================================");
//		}
//		

		SampleHandler.printMessage("~~~~~~~~~~~~~~~~~~~~~~~~~~~finish~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		return null;
	}
	
	private MessageConsole findConsole(String name) {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		
		//return the existing console if it exists
		for(IConsole i : existing) {
			if(name.equals(i.getName())) {
				return (MessageConsole) i;
			}
		}
		
		//create a new console if it does not exist
		MessageConsole myConsole = new MessageConsole(name, null);
		conMan.addConsoles(new IConsole[] {myConsole});
		return myConsole;
	}

	public static void printMessage(String string) {
		out.println(string);
	}
	
}
