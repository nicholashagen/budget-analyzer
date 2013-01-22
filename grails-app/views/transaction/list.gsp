<!-- 
Define the list of transactions view that displays all the available 
transactions for a particular type (bills, deposits, budgets, etc).
-->

<%@ page import="budget.analyzer.Transaction" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        
        <title>${Transaction.title(type)}</title>
    </head>
    <body data-id="${type}" data-nobackbtn="true">
        
        <!-- Define the list of buttons in the page header -->
    	<content tag="buttons">
			<g:link action="create" params="[type:type]" class="ui-btn-right"
			        data-icon="plus" data-transition="slideup">
				Add
			</g:link>    	
    	</content>
    	
    	<!-- Define the content including the main listview for the transactions. -->
    	<content tag="content">
	        <ul data-role="listview" data-dividertheme="b">
	        
	            <!-- Include the total summation of the transactions -->
				<li data-theme="e">
	        		<h4 class="listview-content">Total</h4>
	        		<h4 class="listview-note ui-li-aside">
	        			<g:formatNumber number="${transactions*.amount.sum() ?: 0.0}" type="currency" currencyCode="USD" />
	        		</h4>
	        	</li>
	        	
	        	<!-- Include each transaction -->
	        	<g:each in="${transactions}" status="i" var="transaction">
		        	<li> 
		        		<g:link controller="transaction" action="show" 
							    params="[type:type]" id="${transaction.id}"
							    class="ui-btn-right" data-transition="slide">
		        			<h4 class="listview-info listview-info-p ${transaction.type}">
		        				<g:if test="${transaction.date > 0}">
		        					${transaction.date}
		        				</g:if>
		        				<g:else>
		        					${transaction.getNextDate(new Date())?.date ?: 'N/A'}
		        				</g:else>
		        			</h4>
		        			<h4 class="listview-content">
								${transaction.name}
			        		</h4>
							<p>
								<g:if test="${transaction.recursion == Transaction.RECURSION_MONTHLY}"> 
									Paid monthly on the ${transaction.date}${Transaction.ordinal(transaction.date)}
								</g:if>
								<g:elseif test="${transaction.recursion == Transaction.RECURSION_WEEKLY}">
									Paid
									<g:if test="${transaction.weeks == 1}">
										every week
									</g:if>
									<g:elseif test="${transaction.weeks == 2}">
										every other week
									</g:elseif>
									<g:else>
										every ${transaction.weeks} weeks
									</g:else>
									 on ${Transaction.weekday(transaction.day)}
								</g:elseif>
							</p>
							<h4 class="listview-note listview-note-p ui-li-aside">
								<g:formatNumber number="${transaction.amount}" type="currency" currencyCode="USD" />
							</h4> 
						</g:link>
					</li>
				</g:each>
	        </ul>
	    </content>
    </body>
</html>
