package budget.analyzer

/**
 * This provides a Grails service for accessing the various types of
 * transactions including bills, budgets, deposits, etc.  It also offers the
 * grouping of transactions into months via balance groups and items.
 */
class TransactionService {

	// mark this Grails service as transactional
	static transactional = true
	
	/**
	 * Get the specific transaction with the given id.
	 * 
	 * @param transactionId  The id of the transaction
	 * 
	 * @return The transaction for the given id
	 */
	def getTransaction(int transactionId) {
		return Transaction.get(transactionId);
	}
	
	/**
	 * Update the given transaction and its state to the database.
	 * 
	 * @param transaction  The transaction to update
	 * 
	 * @return The success state of saving the transaction
	 */
	def updateTransaction(Transaction transaction) {
		return transaction.save(flush: true);
	}
	
	/**
	 * Delete the given transaction from the database. If an error occurs, an
	 * exception is thrown.
	 * 
	 * @param transaction  The transaction to delete
	 */
	void deleteTransaction(Transaction transaction) {
		transaction.delete(flush: true);
	}
	
	/**
	 * Get the list of all known transactions for all known transaction types.
	 * 
	 * @return  The list of all transactions for all transaction types
	 */
	def getTransactions() {
		// get the list of all transactions (do not do by type in order to
		// reduce the query cache since the full list is not all that
		// intensive and can be easily filtered
		return Transaction.list(sort: 'date');
	}
	
	/**
	 * Get the list of transactions by the associated type.
	 * 
	 * @param type  The type of transaction
	 * 
	 * @return  The list of all transactions for the given type
	 */
	def getTransactionsByType(String type) {
		// get the list of all transactions (do not do by type in order to
		// reduce the query cache since the full list is not all that
		// intensive and can be easily filtered
		def transactions = getTransactions();
		
		// filter out the associated type and return
		return transactions.findAll { transaction -> transaction.type == type }
	}
	
	/**
	 * Get the list of all transaction instances as balance items by taking the 
	 * recurrence into account.  This returns a list of balance items per each
	 * transaction where the transaction would have occurred or will be occuring
	 * between the given dates.
	 * 
	 * @return The list of balance items between the given dates
	 */
    def getTransactionItems(Date startDate, Date endDate) {
		
		// define list of balance items
		def items = [];
		
		// get the list of all transactions (do not do by type in order to
		// reduce the query cache since the full list is not all that
		// intensive and can be easily filtered
		def transactions = getTransactions();
		
		// process each transaction and each recurring instance updating
		// the running amount and updating the relevant balance groups per
		// month and each balance item per transaction/instance
		transactions.each { transaction ->
			
			// non-deposits should be negative values against running balance
			def bamount = transaction.amount;
			if (transaction.type != Transaction.TYPE_DEPOSIT) {
				bamount = -bamount;
			}

			// handle all dates between start date through end date
			def tdate = startDate;
			while (tdate != null && tdate < endDate) {
				tdate = transaction.getNextDate(tdate);
				if (tdate != null && tdate <= endDate) {
					items.push(new TransactionItem(
						name:transaction.name, date:tdate,
						amount:bamount, transaction:transaction
					))
				}
			}
		}

		// sort the items by their respective dates
		items.sort { a, b -> a.date <=> b.date }
		
		// return list of items
		return items;
    }
}
