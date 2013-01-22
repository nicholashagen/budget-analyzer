package budget.analyzer

/**
 * This reflects a single balance within the system along with the date it was
 * last updated.  Balances also contain an expiration check when the associated
 * date has not been updated in a while.
 */
class Balance {

	// The length of time (ms) to keep balance active (4 hrs)
	static long EXPIRATION = 1000L * 60L * 60L * 4L;
	
	// GORM constraints and transients
    static constraints = { }
	static transients = ['expired']

	// The last reflected balance date
	Date date;
	
	// The amount associated with the balance
	double amount;
	
	/**
	 * Get the state of whether this balance is still valid and active or
	 * whether it has expired.
	 * 
	 * @return <code>true</code> if the balance has expired,
	 *         <code>false</code> otherwise
	 */
	boolean isExpired() {
		long diff = System.currentTimeMillis() - (date?.time ?: 0);
		return (diff > EXPIRATION);
	}
}
