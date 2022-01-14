package comp490.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import comp490.patterns.MethodDeclarationPatterns;
import comp490.patterns.MethodInvocationPatterns;

public class SampleHandler extends AbstractHandler {

	public static final String CONSOLE_NAME = "COMP490";
	private static MessageConsole myConsole;
	private static MessageConsoleStream out;
	
	/**
	 * Entry point of the plug-in application. By clicking on the plug-in icon, the following
	 * code will be first executed.
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		SampleHandler.myConsole = findConsole(CONSOLE_NAME);
		SampleHandler.out = myConsole.newMessageStream();
		
		SampleHandler.printMessage("~~~~~~~~~~~~~~~~~~~~~~~~~~~ start~~~~~~~~~~~~~~~~~~~~~~~~~~~");

		MethodDeclarationPatterns methodDeclarationPatterns = new MethodDeclarationPatterns();
		methodDeclarationPatterns.extractCFG();
		
		MethodInvocationPatterns methodInvocationPatterns = new MethodInvocationPatterns();
		int numLoggingMethods = methodInvocationPatterns.countNbrOfLoggingMethodInvocations();
		SampleHandler.printMessage("number of logging methods: " + numLoggingMethods);
		methodInvocationPatterns.extractCG();

		SampleHandler.printMessage("~~~~~~~~~~~~~~~~~~~~~~~~~~~finish~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		return null;
	}
	
	/**
	 * Create (or return an existing) MessageConsole object 
	 * 
	 * @param name name of MessageConsole
	 * @return MessageConsole
	 */
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

	/**
	 * Print text message to console 
	 * 	
	 * @param string message to be printed
	 */
	public static void printMessage(String string) {
		out.println(string);
	}
	
}
