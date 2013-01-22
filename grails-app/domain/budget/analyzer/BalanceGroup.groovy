package budget.analyzer

import java.text.SimpleDateFormat;

/**
 * This reflects a set of balances associated with a specific month and year.
 * This allows the ability to group balances by months for display purposes.
 */
class BalanceGroup {

	// Date formatter for printing dates for the month/year
	static SimpleDateFormat FORMATTER =
		new SimpleDateFormat('MMMM yyyy');

	// GORM constraints
	static constraints = { }

	// The year and month associated with this group
	int month;
	int year;
	
	// The list of items in this group
	def items = [];
	
	/**
	 * Get the textual representation of this group based on the associated
	 * year and month.
	 * 
	 * @return THe textual month/year
	 */
	public String toString() {
		synchronized (FORMATTER) {
			return FORMATTER.format(new Date(this.year, this.month, 1));
		} 
	}
}
