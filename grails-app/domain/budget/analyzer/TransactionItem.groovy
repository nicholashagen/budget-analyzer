package budget.analyzer

/**
 * This reflects a specific instance of a recurring transaction on a specific
 * date.  This is used when iterating over transactions to assign a specifc
 * date and value for the item regardless of its actual recurring states.
 */
class TransactionItem {

	// GORM constraints
    static constraints = { }
	
	// The specific name of this instance of the transaction
	String name;
	
	// The specific date this transaction is applied on
	Date date;
	
	// The specific amount of this instance of the transaction
	double amount;
	
	// The associated transaction instance
	Transaction transaction;
}
