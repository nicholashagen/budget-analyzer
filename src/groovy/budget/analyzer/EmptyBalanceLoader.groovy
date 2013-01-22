package budget.analyzer

/**
 * This provides a noop balance loader that simply returns null to signify that
 * no balance can be downloaded.
 */
class EmptyBalanceLoader implements BalanceLoader {

	/**
	 * Simply return <code>null</code> signifying no balance could be 
	 * downloaded.
	 */
	Double loadBalance() {
		return null;
	}

}
