package budget.analyzer

/**
 * This reflects a transaction within the system.  A transaction may be a
 * recurring bill, a one time payment, a recurring budget, or a recurring
 * deposit.  Transactions may recur either monthly on a specific date each month
 * or weekly on a given day within the week.
 * 
 * If the recursion is monthly, then only the date field as the date within the
 * month (1-31) is relevant.  The start date may also be used to set when the
 * transaction applies from.
 * 
 * If the recursion is weekly, then the weeks field may be used to set every
 * week (1), every other week (2), every 3 weeks (3), etc.  The day field is
 * used to represent the specific day of the week (0-6, Sun-Sat).  The start 
 * date may also be used to set when the transaction applies from, especially
 * for cases where the weeks are greater than 1.
 */
class Transaction {

	// This represents a transaction as a bill
	static String TYPE_BILL = 'bills';
	
	// This represents a transaction as a budget
	static String TYPE_BUDGET = 'budget';
	
	// This represents a transaction as a deposit/income
	static String TYPE_DEPOSIT = 'deposits';
	
	// This reprsents a transaction as a one time payment
	static String TYPE_PAYMENT = 'payments';
	
	// This represents a one time, non-recurring transaction
	static int RECURSION_NONE = 0;
	
	// This represents a monthly recurring transaction
	static int RECURSION_MONTHLY = 1;
	
	// This represents a weekly recurring transaction
	static int RECURSION_WEEKLY = 2;
	
	// Constant for number of milliseconds in a week
	static long MSECS_IN_WEEK = 1000L * 60L * 60L * 24L * 7L;
	
	// GORM constraints
    static constraints = {
		weeks(nullable:true)
		day(nullable:true)
		date(nullable:true)
		startDate(nullable:true)
	}

	// The general name for this transaction (payee)
	String name;
	
	// The amount of the transaction, always positive regardless of bill/deposit
	double amount;
	
	// The type of transaction (TYPE_BILL, TYPE_DEPOSIT, TYPE_BUDGET, etc)
	String type;
	
	// The type of recursion (RECURSION_WEEKLY, RECURSION_MONTHLY, etc)
	int recursion;
	
	// The number of weeks per transaction (every week, every other week, etc)
	Integer weeks = -1;
	
	// The day of the week for weekly recursion (0-6, Sun-Sat)
	Integer day = -1;
	
	// The date within the month for monthly recursion (1-31)
	Integer date = -1;
	
	// The date to start from when applying the transaction
	Date startDate = null;
	
	/**
	 * Get the previous date from the current date that this transaction would
	 * have occurred on.  This takes into effect recursion to determine the
	 * previous date.
	 * 
	 * @return  The previous date this transaction occurred on
	 */
	public Date getPreviousDate() {
		return getPreviousDate(new Date());
	}
	
	/**
	 * Get the previous date from the given date that this transaction would
	 * have occurred on.  This takes into effect recursion to determine the
	 * previous date.
	 *
	 * @return  The previous date this transaction occurred on
	 */
	public Date getPreviousDate(Date date) {

		// if using monthly recursion, then 2 rules apply
		// - if the given date has already passed in the current month, then
		//   use that given date within the current month
		// - if the given date has not yet passed in the current month, then
		//   use the given date in the previous month 
		if (this.recursion == RECURSION_MONTHLY) {
			if (this.date <= date.date) { 
				return new Date(date.year, date.month, this.date); 
			}
			else {
				return new Date(date.year, date.month - 1, this.date);
			}
		}
		
		// if using weekly recursion, then 3 rules apply
		// - if every week and the given day has already passed in current week
		//   then use that given day within the current week
		// - if every week and the given day has not yet passed, then use that
		//   given day in the previous week
		// - if not every week, then calculate from the start date
		else if (this.recursion == RECURSION_WEEKLY) {
			if (this.weeks == 1) {
				if (this.day <= date.day) {
					return new Date(date.year, date.month, date.date - date.day + this.day);
				}
				else {
					return new Date(date.year, date.month, date.date - date.day + this.day - 6)
				}
			}
			else {
				// calculate the diff between start date and given date
				// and determine how many instances may have passed since
				long time = (date.time - this.startDate.time);
				def count = (long) Math.floor(time / (this.weeks * MSECS_IN_WEEK));
				
				// calculate the actual date that would have resulted in and
				// count backwards to the date less than given date
				def result = new Date(this.startDate.time + (count * this.weeks * MSECS_IN_WEEK));
				while (result > date) {
					result -= (7 * this.weeks);
				}
				
				return result;
			}
		}
		
		// if not using recursion, then this is a one time payment, so determine
		// if the given date has already passed or not
		else if (this.recursion == RECURSION_NONE) {
			if (this.startDate >= date) { return null; }
			else { return this.startDate; }
		}
	}
	
	/**
	 * Get the next date from the current date that this transaction would
	 * be occurring on.  This takes into effect recursion to determine the
	 * next date.
	 *
	 * @return  The next date this transaction will occur on
	 */
	public Date getNextDate() {
		return getNextDate(new Date());
	}
	
	/**
	 * Get the next date from the given date that this transaction would
	 * be occurring on.  This takes into effect recursion to determine the
	 * next date.
	 *
	 * @return  The next date this transaction will occur on
	 */
	public Date getNextDate(Date date) {
		// if using monthly recursion, then 2 rules apply
		// - if the given date has already passed in the current month, then
		//   use the given date in the next month
		// - if the given date has not yet passed in the current month, then
		//   use that given date in the current month
		if (this.recursion == RECURSION_MONTHLY) {
			if (this.date <= date.date) {
				return new Date(date.year, date.month + 1, this.date);
			}
			else {
				return new Date(date.year, date.month, this.date);
			}
		}
		
		// if using weekly recursion, then 3 rules apply
		// - if every week and the given day has already passed in current week
		//   then use that given day in the next week
		// - if every week and the given day has not yet passed, then use that
		//   given day within the current week
		// - if not every week, then calculate from the start date
		else if (this.recursion == RECURSION_WEEKLY) {
			if (this.weeks == 1) {
				if (this.day <= date.day) {
					return new Date(date.year, date.month, date.date - date.day + this.day + 7);
				}
				else {
					return new Date(date.year, date.month, date.date + this.day - date.date);
				}
			}
			else {
				// calculate the diff between start date and given date
				// and determine how many instances may have passed since
				long time = (date.time - this.startDate.time);
				def count = (long) Math.ceil(time / (this.weeks * MSECS_IN_WEEK));
				
				// calculate the actual date that would have resulted in and
				// count backwards to the date less than given date
				def result = new Date(this.startDate.time + (count * this.weeks * MSECS_IN_WEEK));
				while (result <= date) {
					result += (7 * this.weeks);
				}
				
				return result;
			}
		}
		
		// if not using recursion, then this is a one time payment, so determine
		// if the given date has already passed or not
		else if (this.recursion == RECURSION_NONE) {
			if (this.startDate <= date) { return null; }
			else { return this.startDate; }
		}
	}
	
	/**
	 * Get the types of transactions supported such as TYPE_BILL, TYPE_BUDGET,
	 * etc.
	 * 
	 * @return The list of transaction types
	 */
	static String[] types() {
		return [ TYPE_BILL, TYPE_BUDGET, TYPE_DEPOSIT ];
	}
	
	/**
	 * Convert the given type to a textual representation.
	 * 
	 * @param type  The name of the type
	 * 
	 * @return  The title representation
	 */
	static String title(type) {
		return type[0].toUpperCase() + type.substring(1);
	}
	
	/**
	 * Get the name of the weekday (Sun-Sat) associated with the given 
	 * day (0-6).
	 * 
	 * @param day  The day of the week
	 * 
	 * @return  The textual representation of the day
	 */
	static String weekday(int day) {
		return [ 'Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday' ][day];
	}
	
	/**
	 * Get the ordinal based on the given date.  For example, 1 would return
	 * <code>st</code>, 2 would return <code>nd</code>, 3 would return
	 * <code>rd</code>, and others may return <code>th</code>.
	 * 
	 * @param day  The date within a month
	 * 
	 * @return  The textual ordinal display
	 */
	static String ordinal(day) {
		if ((day >= 1 && day <= 3) || (day > 20)) {
			int end = day % 10
			if (end == 1) { return 'st'; }
			else if (end == 2) { return 'nd'; }
			else if (end == 3) { return 'rd'; }
		}
		
		return 'th';
	}
}
