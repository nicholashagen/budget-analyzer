import budget.analyzer.Balance
import budget.analyzer.BalanceRetrieverJob;
import budget.analyzer.Transaction

class BootStrap {

    def init = { servletContext ->
		
		// update transactions
		if (Transaction.count() == 0) {

                        // Sample Transaction
                        new Transaction(type: Transaction.TYPE_BILL, name:'Sample', amount:250.0,
                                        recursion:Transaction.RECURSION_MONTHLY, date:15)
                            .save(failOnError: true)

			// Deposit
			new Transaction(type: Transaction.TYPE_DEPOSIT, name:'Work', amount:1500.0,
                                        recursion:Transaction.RECURSION_WEEKLY, startDate:new Date(), day:2, weeks:2)
				.save(failOnError: true)
	
			// Groceries
			new Transaction(type: Transaction.TYPE_BUDGET, name:'Groceries', amount:150.0,
                                        recursion:Transaction.RECURSION_WEEKLY, day:0, weeks:1)
				.save(failOnError: true)
		}
    }
    def destroy = {
    }
}
