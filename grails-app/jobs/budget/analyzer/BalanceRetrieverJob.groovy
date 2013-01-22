package budget.analyzer

import java.text.DateFormat;
import java.text.NumberFormat


/**
 * This is a scheduled job that retrieves the current balance every hour in
 * order to keep the active balance updated.  It also checks the available
 * balance for up to a month in the future based on the available transactions
 * and their recurrences to ensure a given threshold is maintained.
 */
class BalanceRetrieverJob {
	
	// Define the timeout when this job runs
    def timeout = 1000L * 60L * 60L;

	// Inject the mail service plugin to send emails
	def mailService
	
	// Inject the Grails application to access configuration
	def grailsApplication
	
	// Inject the balance loader to retrieve the actual balance
	BalanceLoader balanceLoader
	
	// Inject the balance service to update the retrieve balance 
	BalanceService balanceService
	
	// Inject the transaction service to load the available transactions
	TransactionService transactionService
	
	/**
	 * Execute the job to retrieve the active balance from an underlying balance
	 * loader implementation and update the database to reflect the active
	 * balance.  Also, use the active balance to check for the balance
	 * threshold.
	 */
    def execute() {
		
		// load and set the current balance
		def balance = null;
		try { balance = loadAndSetBalance(); }
		catch (Exception e) {
			log.error("unable to update balance", e);
			return
		}

		// ignore if no balance set
		if (!balance) { return }
		
		// check the balance threshold and send email if threshold exceeded
		try { checkBalanceThreshold(balance); }
		catch (BalanceThresholdException bte) {
			sendBalanceThresholdEmail(bte.balanceThreshold);
		}
		catch (Exception e) {
			log.error("unable to check balance threshold", e);
			return
		}
    }
	
	/**
	 * Load the current balance from the configured balance loader and update
	 * the database with the current balance.
	 * 
	 * @return The current balance
	 */
	def loadAndSetBalance() {
		def balance = balanceLoader.loadBalance();
                if (balance) {
		    balanceService.setBalance(balance);
                }
		return balance;
	}
	
	/**
	 * Check the given balance against the list of available transactions based
	 * on their recurrence to validate whether at any point within the next
	 * month ahead the balance will go below the configured threshold.  If the
	 * threshold is met, then a balance threshold exception is thrown with the
	 * given transaction item and balance.
	 * 
	 * @param balance  The current balance
	 */
	void checkBalanceThreshold(def balance) {
		
		// get current threshold
		def balanceThreshold = balanceService.getBalanceThreshold()
		
		// define the start/end dates from today through a month from now
		def start = new Date(), end = start + 30;
		
		// load the transaction items between the given dates
		def items = transactionService.getTransactionItems(start, end);
		
		// check each item updating the running balance to determine if
		// threshold is met and throw an exception indicating the error
		def running = balance
		for (def item : items) {
			running += item.amount
			if (running < balanceThreshold.threshold) {
				// check if previous threshold already alerted and ignore
				if (item.date == balanceThreshold.date &&
					item.transaction.id == balanceThreshold.transaction?.id) {
					return
				}

				// otherwise, update the last warning and save
				balanceThreshold.date = item.date
				balanceThreshold.current = balance
				balanceThreshold.balance = running
				balanceThreshold.transaction = item.transaction
				balanceService.saveBalanceThreshold(balanceThreshold);

				// throw exception with threshold
				println "Generating balance threshold alert of ${running}..."
				throw new BalanceThresholdException(
					balanceThreshold:balanceThreshold
				)
			}
		}
	}
	
	/**
	 * Send an email indicating the threshold has been exceeded via the
	 * reflected balance that occurred by the given item.  The associated date
	 * is represented by the date of the given transaction item.
	 * 
	 * @param balanceThreshold The threshold containing the warning
	 */
	void sendBalanceThresholdEmail(BalanceThreshold balanceThreshold) {
		
		// define the currency and date formatters
		def cformatter = NumberFormat.getCurrencyInstance();
		def dformatter = DateFormat.getDateInstance(DateFormat.LONG);
		
		// get the associated properties
		def current = balanceThreshold.current
		def threshold = balanceThreshold.threshold
		def balance = balanceThreshold.balance
		def date = balanceThreshold.date
		
		// define the email body
		def emailBody = 
			"The current balance of ${cformatter.format(current)} will " +
			"exceed the threshold of ${cformatter.format(threshold)} on " +
			"${dformatter.format(date)} with a balance of " +
			"${cformatter.format(balance)}.";
		
		// get the configured addresses
		def toEmail = grailsApplication.config.budget?.threshold?.email?.to
		def fromEmail = grailsApplication.config.budget?.threshold?.email?.from
		def emailSubject = grailsApplication.config.budget?.threshold?.email?.subject
		if (!emailSubject) {
			emailSubject = "Budget Analyzer Threshold Warning"
		}
		
		// send the email
		if (toEmail) {
			println "Sending balance threshold alert to ${toEmail}"
			mailService.sendMail {
				to toEmail
				if (fromEmail) {
					from fromEmail
				}
				subject emailSubject
				body emailBody
			}
		}
	}
	
	/**
	 * Custom exception that represents when a balance goes beneath the
	 * configured threshold and provides the transaction item that caused the
	 * exception as well as the balance at that point.
	 */
	class BalanceThresholdException extends Exception {

		// The current balance threshold containing the warning 
		def balanceThreshold;
	}
}
