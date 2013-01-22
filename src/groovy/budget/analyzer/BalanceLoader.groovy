package budget.analyzer

/**
 * This represents a balance loader interface that provides a method to
 * retrieve the current balance from a given system.  The implementations should
 * provide the underlying functionality.
 */
interface BalanceLoader {

	/**
	 * Load the current balance.
	 * 
	 * @return The current balance
	 */
	Double loadBalance();
}
