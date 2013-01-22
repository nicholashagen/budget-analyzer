package budget.analyzer


/**
 * This provides a Grails service for accessing the current balance from a 
 * given bank.  If the balance already exists and is not yet expired, then it
 * is returned.  Otherwise, the balance is acquired accordingly.  This also
 * provides access to the threshold and threshold warnings.
 */
class BalanceService {

	// mark this Grails service as transactional
    static transactional = true

	// inject the balance loader
	BalanceLoader balanceLoader;
	
	/**
	 * Get the active balance.  If the balance does not exist or has expired,
	 * then the balance is acquired and returned.  Otherwise, the existing
	 * balance is returned.
	 * 
	 * @return The current balance
	 */
    def getBalance() {
		
		// check if balance exists and is not expirded
		Balance balance = Balance.get(1L);
		if (balance == null || balance.isExpired()) {
			
			// get current balance
			def value = balanceLoader.loadBalance();
			if (!value) { return 0.0; }
			
			// save balance
			if (balance == null) { 
				balance = new Balance(); 
			}
			
			balance.id = 1L;
			balance.date = new Date();
			balance.amount = value;
			balance.save(flush: true);
		}
		
		return balance.amount;
    }
	
	/**
	 * Set the active balance to the given value and reset the expiration
	 * states.
	 * 
	 * @param value  The value to set
	 */
	def setBalance(double value) {
		
		// lookup the current balance
		// if the balance does not exist or is expired, then a new balance
		// is defined
		Balance balance = Balance.get(1L);
		if (balance == null || balance.isExpired()) {
			if (balance == null) { 
				balance = new Balance(); 
			}
			balance.id = 1L;
		}

		// update the last updated date and amount and save
		balance.date = new Date();
		balance.amount = value;
		balance.save(flush: true);
	}
	
	/**
	 * Get the current state of the balance threshold including any previously
	 * generated warnings.
	 * 
	 * @return  The current balance threshold
	 */
	def getBalanceThreshold() {
		
		// get the current threshold information
		BalanceThreshold balanceThreshold = BalanceThreshold.get(1L);
		
		// if no threshold exists, create an initial threshold
		if (balanceThreshold == null) {
			balanceThreshold = new BalanceThreshold(
				id:1L, threshold:100
			);
		
			// save the threshold information
			balanceThreshold.save(flush: true);
		}

		// return the threshold
		return balanceThreshold;
	}
	
	/**
	 * Set the current threshold value within the current balance threshold.
	 * 
	 * @param threshold  The threshold value in dollars
	 */
	def setBalanceThreshold(double threshold) {
		
		// get the current threshold information
		BalanceThreshold balanceThreshold = BalanceThreshold.get(1L);
		
		// if no threshold exists, create an initial threshold
		if (balanceThreshold == null) {
			balanceThreshold = new BalanceThreshold(
				id:1L, threshold:threshold
			);
		}

		// save the threshold information
		balanceThreshold.save(flush: true);
	}
	
	/**
	 * Save the given balance threshold, including any warnings, to the 
	 * database.
	 * 
	 * @param balanceThreshold  The balance threshold to save
	 */
	def saveBalanceThreshold(BalanceThreshold balanceThreshold) {
		balanceThreshold.save(flush: true);
	}
}
