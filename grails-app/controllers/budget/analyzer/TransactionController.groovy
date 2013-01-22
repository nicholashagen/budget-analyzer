package budget.analyzer

import java.text.SimpleDateFormat;

/**
 * This provides a Grails controller that is responsible for controlling the
 * various transaction pages based on the associated type (bill, budget, etc).
 * This class is generic enough to handle any of the transaction types including
 * viewing, editing, creating, and deleting.
 */
class TransactionController {

	// The set of allowable HTTP methods per method
	static allowedMethods = [save: 'POST', update: 'POST', delete: 'POST']

	// Inject the transaction service responsible for retrieving transactions
	TransactionService transactionService;
	
	/**
	 * Handle the default page action and redirect to the list action.
	 */
    def index() {
        redirect(action: 'list', params: params)
    }
	 
	/**
	 * Handle the list page action that provides the list of transactions
	 * configured for the associated type.  The type should be part of the 
	 * query (via URL mappings).
	 * 
	 * @return The page model data
	 */
    def list() {
		
		// retrieve all available transactions
		def transactions = 
			transactionService.getTransactionsByType(params.type);
		
		// return the page model data
        [ 
			transactions: transactions, 
			total: transactions.size(), 
			type: params.type 
		]
    }

	/**
	 * Handle the create transaction action requested when the Add button is
	 * clicked.  This sets up an initial transaction for the user to modify.
	 * 
	 * @return The page model data
	 */
    def create() {
		// iniialize the transaction defaults
		def transaction = new Transaction(
			recursion:Transaction.RECURSION_MONTHLY,
			startDate:new Date()
		);
	
		// return the page model data
		return [transaction: transaction, type: params.type]
    }

	/**
	 * Handle the save transaction action requested when the user submits the
	 * create transaction form.  This validates the transaction properties
	 * configured by the user and adds the transaction to the database.
	 */
    def save() {

		// convert the start date via HTML5 date formats
		if (params.startDate) {
			params.startDate =
				new SimpleDateFormat('yyyy-MM-dd').parse(params.startDate);
		}

		// setup a new transaction and copy the properties
		// TODO: copy the properties in this manner is unsafe
		def transaction = new Transaction();
		transaction.properties = params
		
		// save and validate the transaction
		def valid = false
		if (!transaction.hasErrors()) {
			valid = transactionService.updateTransaction(transaction);
		}
		
		// if the transaction properly processed, redirect back to the list page
		if (valid) {
			redirect(action: 'list', params: [type: params.type])
		}
		
		// otherwise, re-render the create view with the transaction data
		else {
			def model =  [transaction: transaction, type: params.type];
			render(view: 'create', model: model)
		}
    }

	/**
	 * Handle the show page action when a specific transaction is requested to
	 * show its associated details.
	 * 
	 * @return The page model data
	 */
    def show() {
		// lookup the transaction with the given id
        def transaction = transactionService.getTransaction(params.int('id'));
		
		// if the transaction does not exist, redirect back to list page
        if (!transaction) {
            redirect(action: 'list', params: [type: params.type])
        }
		
		// otherwise, setup te page model data
		// note that the end date is the number of days out to show for upcoming
		// transaction instances based on recurring
        else {
            [
				transaction: transaction, 
				type: params.type, 
				endDate: new Date() + 60
			]
        }
    }

	/**
	 * Handle the edit page action when the Edit button is invoked for a 
	 * specific transaction to edit its details.  This sets up the transaction
	 * state to be editted.
	 * 
	 * @return THe page model data
	 */
    def edit() {
		// lookup the transaction with the given id
		def transaction = transactionService.getTransaction(params.int('id'));
		
		// if the transaction does not exist, redirect back to list page
        if (!transaction) {
            redirect(action: 'list', params:[type: params.type])
        }
		
		// otherwise, setup the page model data
        else {
			[transaction: transaction, type: params.type]
        }
    }

	/**
	 * Handle the update transaction action requested when the user submits the
	 * edit transaction form.  This validates the transaction properties
	 * configured by the user and updates the transaction in the database.
	 */
    def update() {
		// lookup the transaction with the given id
        def transaction = transactionService.getTransaction(params.int('id'));
		
		// if the transaction does not exist, redirect back to list page
        if (!transaction) {
			redirect(action: 'list', params:[type: params.type])
        }
		
		// otherwise, validate the transaction and save
		else {
			
			// check the version to ensure another user did not update first
            if (params.version) {
                def version = params.version.toLong()
                if (transaction.version > version) {
                    transaction.errors.rejectValue(
						"version", 
						"default.optimistic.locking.failure", 
						[
							message(code: 'transaction.label', default: 'Transaction')
						] as Object[], 
						"Another user has updated this Transaction while you were editing"
					)
					
					def model = [transaction: transaction, type: params.type];
                    render(view: "edit", model: model)
                    return
                }
            }

			// convert the start date per the HTML5 date format
			if (params.startDate) {
				params.startDate =
					new SimpleDateFormat("yyyy-MM-dd").parse(params.startDate);
			}
			
			// copy the properties from the form
			// TODO: this is unsafe copying in this manner
			transaction.properties = params
			
			// validate and update the transaction
			def valid = false
			if (!transaction.hasErrors()) {
				valid = transactionService.updateTransaction(transaction);
			}
			
			// if successful, redirect back to the transaction details page
			if (valid) {
				redirect(
					action: 'show', id: transaction.id, 
					params: [type: params.type]
				)
            }
			
			// otherwise, re-render the edit page with the transaction data
            else {
				def model =  [transaction: transaction, type: params.type]
                render(view: 'edit', model: model)
            }
        }
    }

	/**
	 * Handle the delete transaction action requested when the user clicks the
	 * delete transaction button.  This removes the transaction from the 
	 * database.
	 */
    def delete() {
		
		// lookup the associated transaction with the given id
        def transaction = transactionService.getTransaction(params.int('id'));
		
		// if the transaction does not exist, redirect back to list page
        if (!transaction) {
			redirect(action: "list", params: [type: params.type])
        }
		
		// otherwise, delete the associated transaction
		else {
			transactionService.deleteTransaction(transaction);
			
			// if successful, redirect back to the list page
            redirect(action: 'list', params: [type: params.type])
        }
    }
}
