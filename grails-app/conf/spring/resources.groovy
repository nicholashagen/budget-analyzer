import grails.util.GrailsUtil
import budget.analyzer.EmptyBalanceLoader

beans = {

        // TODO: CONFIGURE BALANCE LOADER
	balanceLoader(EmptyBalanceLoader) {
	}
}
