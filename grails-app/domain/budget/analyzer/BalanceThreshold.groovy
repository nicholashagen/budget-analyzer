package budget.analyzer

/**
 * This represents information regarding the threshold status as well as the
 * last calculated threshold warning.
 */
class BalanceThreshold {

	// GORM constraints
    static constraints = {}

	// The actual threshold to check/monitor
	double threshold
	
	// The date at the last threshold warning
	Date date
	
	// The exceeding balance at the last threshold warning
	double balance
	
	// The current balance at the last threshold warning
	double current
	
	// The transaction at the last threshold warning
	Transaction transaction
}
