package budget.analyzer

import groovy.json.JsonSlurper

/**
 * This provides a remote URL balance loader that loads the current balance by
 * downloading a JSON file from a remote URL to actually load the balance. The
 * output should contain:
 * 
 * <code>
 * {
 *     balance: number,
 *     date: 'yyyy-MM-dd hh:mm:ss'
 * }
 * </code>
 */
class RemoteBalanceLoader implements BalanceLoader {

	// The path to the remote URL to download
	String remoteUrl;
	
	/**
	 * Load the balance by downloading the remote URL and parsing the config
	 * accordingly.
	 * 
	 * @return The current balance
	 */
	Double loadBalance() {
		
		// validate the configuration
		if (!remoteUrl) {
			throw new IllegalArgumentException("missing remote url");
		}
		
		// download and validate the remote file
		def text = new URL(remoteUrl).text;
		if (!text) {
			throw new RuntimeException("unable to download configuration");
		}
		
		// parse and validate the remote file contents
		def result = null;
		def json = new JsonSlurper();
		try { result = json.parseText(text); }
		catch (Exception e) {
			throw new RuntimeException("unable to parse configuration", e);
		}
		
		// get the actual balance and date
		def date = result.date;
		def balance = result.balance;
		println "Downloaded remote balance ${balance} from ${date}"
		
		// return the balance
		return balance as Double;
	}
}
