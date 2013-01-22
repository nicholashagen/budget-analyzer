package budget.analyzer

/**
 * This provides an external balance loader that loads the current balance by
 * executing an external program (ie: Phantom JS) to actually load the balance.
 * The program reads the output of the application as the balance value.
 */
class ExternalBalanceLoader implements BalanceLoader {

	// The path to the phantomjs application
	String phantomjsApplication;
	
	// The set of optional arguments to include when invoking phantomjs
	List<String> phantomjsArguments;
	
	// The actual phantomjs script to run
	String phantomjsScript;
	
	/**
	 * Load the balance by executing Phantom JS to load the actual balance.
	 * 
	 * @return The current balance
	 */
	double loadBalance() {
		// validate params
		if (!phantomjsApplication || !phantomjsScript) {
			throw new IllegalArgumentException("invalid app/script");
		}
		
		// setup the command line
		def execution = [];
		execution << phantomjsApplication;
		if (phantomjsArguments) {
			phantomjsArguments.each { execution << it }
		}
		execution << phantomjsScript;
		
		// run the application
		def process = execution.execute();
		process.waitForOrKill(30000);
		
		// validate the result
		def exitValue = process.exitValue(); 
		if (exitValue != 0) {
			throw new RuntimeException("unable to run: ${exitValue}");
		}
		
		// validate the output
		def result = process.in.text;
		if (!result || !result.isDouble()) {
			throw new RuntimeException("invalid output: ${result}");
		}
		
		// return balance
		return result as double;
	}
}
