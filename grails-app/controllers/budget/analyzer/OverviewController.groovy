package budget.analyzer;

import java.text.SimpleDateFormat

/**
 * This provides a Grails controller that is responsible for controlling the
 * overview pages such as the running balance overview.
 */
class OverviewController {

	// The set of allowable HTTP methods per method
	static allowedMethods = [savePayment: "POST", saveBalance: "POST"]
	
	// Inject the bank service responsible for retrieving balances
	BalanceService balanceService;
	
	// Inject the transaction service responsible for retrieving transactions
	TransactionService transactionService;
	
	/**
	 * Handle the default page action and redirect to the list action.
	 */
    def index() { 
		redirect(action: "list", params: params)
	}
	
	/**
	 * Handle the list page action to list the running balance based on the
	 * current balance and the list of recurring budgets, bills, etc.
	 * 
	 * @return The page data model
	 */
	def list() {
		// process from three days ago to 2 months ahead
		def today = new Date();
		def start = today - 3, end = today + 60;
		
		// retrieve the current balance and running amount
		def balance = balanceService.getBalance(), amount = balance;
		
		// retrieve the list of all transaction items
		def items = transactionService.getTransactionItems(start, end);

		// setup two main groups (previous days and upcoming days)
		// process each item organizing into the previous/upcoming groups
		// as well into a group per month for organizing in the UI
		def previous = [:], upcoming = [:];
		items.each { item -> 
			def key = (item.date.year * 10) + item.date.month;
			if (item.date <= today) {
				if (!previous[key]) {
					previous[key] = new BalanceGroup(
						month:item.date.month, year:item.date.year
					);
				}
				
				previous[key].items << item
			}
			else {
				if (!upcoming[key]) {
					upcoming[key] = new BalanceGroup(
						month:item.date.month, 
						year:item.date.year
					);
				}

				amount += item.amount;
				item.amount = amount;
				upcoming[key].items << item
			}
		}
		
		// return the model for the page
		[balance: balance, previous: previous, upcoming: upcoming]
	}
	
	/**
	 * Handle the add payment action requested when the user clicks the Add
	 * Payment button on the Overview page.  This sets up a base transaction.
	 * 
	 * @return The page data model
	 */
	def addPayment() {
		// create an initialized transaction
		def transaction = new Transaction(startDate:new Date());
		
		// return the model for the page
		[transaction: transaction, type:params.type]
	}
	
	/**
	 * Handle the save payment action requested as part of the add payment
	 * form submission.  This action is only allowed as a POST and is used to
	 * validate the user-submitted features and upon successful validation,
	 * create a new one-time transaction of type payment.
	 */
	def savePayment() {
		
		// initialize the transaction state (one time payment)
		def transaction = new Transaction(
			type:Transaction.TYPE_PAYMENT,
			recursion:Transaction.RECURSION_NONE
		);
	
		// parse the start date as an HTML5 date field
		if (params.startDate) {
			params.startDate =
				new SimpleDateFormat("yyyy-MM-dd").parse(params.startDate);
		}
		
		// set the remaining properties from the form submission
		// and save the transaction accordingly
		// TODO: this is unsafe to purely set the properties from the params
		def valid = true
		transaction.properties = params
		if (!transaction.hasErrors()) {
			Transaction.withTransaction { status ->
				valid = transaction.save(flush: true);
			}
		}

		// if successful, redirect back to the list page with the updated data		
		if (valid) {
			redirect(controller:"overview", action: "list")
		}
		
		// otherwise, re-render the add payment page with the transaction
		else {
			render(view: "addPayment", model: [transaction: transaction])
		}
	}
	
	/**
	 * Handle the edit balance action requested when the user clicks the Edit
	 * Balance button on the Overview page.  This retrieves the last available
	 * balance so the user can update as needed.
	 *
	 * @return The page data model
	 */
	def editBalance() {
		// get the current balance
		def balance = balanceService.getBalance()
		
		// return the page data model
		[balance: balance]
	}
	
	/**
	 * Handle the save balance action requested as part of the edit balance
	 * form submission.  This action is only allowed as a POST and is used to
	 * validate the user-submitted balance and upon successful validation,
	 * update the balance.
	 */
	def saveBalance() {
		// parse the submitted balance
		def balance = Double.valueOf(params.balance)
		
		// update and save the balance
		balanceService.setBalance(balance);
		
		// redirect to the list page with the updated data
		redirect(action:'list')
	}
}
