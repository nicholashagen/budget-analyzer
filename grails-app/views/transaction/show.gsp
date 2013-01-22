<!-- 
Define the transaction details page that displays the associated information
about a specific transaction including upcoming transaction dates based on the
recurrence.  Note that this view does not include the footer.
-->

<%@ page import="budget.analyzer.Transaction" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />

        <title>${transaction.name}</title>
    </head>
    <body data-nofooter="true">
    
        <!-- Define the page header buttons -->
    	<content tag="buttons">
			<g:link action="list" params="[type:type]" data-icon="arrow-l" 
			        data-transition="slide" data-direction="reverse">
				Back
			</g:link>
			<g:link action="edit" id="${transaction.id}" 
			        params="[type:type]" class="ui-btn-right"
			        data-icon="edit" data-transition="slideup">
				Edit
			</g:link>
    	</content>
    
        <!-- Define the page content including details and upcoming dates -->
        <content tag="content">
	        <ul data-role="listview" data-dividertheme="b">
	        
	            <!-- Include the main information (name, amount, etc) -->
				<li data-role="list-divider">
	        		<h4 class="listview-content">Information</h4>
	        	</li>
				<li>
	        		<h4 class="listview-content">Name</h4>
	        		<h4 class="listview-note ui-li-aside" style="width: inherit !important;">
	        			${transaction.name}
	        		</h4>
	       		</li>
	       		<li>
	        		<h4 class="listview-content">Type</h4>
	        		<h4 class="listview-note ui-li-aside" style="width: inherit !important;">
	        			${Transaction.title(transaction.type)}
	        		</h4>
	       		</li>
	        	<li>
	        		<h4 class="listview-content">Amount</h4>
	        		<h4 class="listview-note ui-li-aside" style="width: inherit !important;">
	        			<g:formatNumber number="${transaction.amount}" type="currency" currencyCode="USD" />
	        		</h4>
	       		</li>
	       		<li>
	       			<h4 class="listview-content">Schedule</h4>
	        		<h4 class="listview-note ui-li-aside" style="width: inherit !important;">
		           		<g:if test="${transaction.recursion == Transaction.RECURSION_MONTHLY}"> 
							Every month on the ${transaction.date}${Transaction.ordinal(transaction.date)}
						</g:if>
						<g:elseif test="${transaction.recursion == Transaction.RECURSION_WEEKLY}">
							<g:if test="${transaction.weeks == 1}">
								Every week on ${Transaction.weekday(transaction.day)}
							</g:if>
							<g:elseif test="${transaction.weeks == 2}">
								Every other week on ${Transaction.weekday(transaction.day)}
							</g:elseif>
							<g:else>
								Every ${transaction.weeks} weeks on ${Transaction.weekday(transaction.day)}
							</g:else>
						</g:elseif>
					</h4>
	        	</li>
	       		<g:if test="${transaction.recursion == Transaction.RECURSION_WEEKLY && transaction.startDate}">
		       		<li>
		        		<h4 class="listview-content">Starting</h4>
		        		<h4 class="listview-note ui-li-aside" style="width: inherit !important;">
		        			<g:formatDate date="${transaction.startDate}" format="MMM d, yyyy" />
		        		</h4>
		       		</li>
		       	</g:if>
		       	
		       	<!-- Include the list of upcoming payments based on recursion -->
				<li data-role="list-divider">
	        		<h4 class="listview-content">Upcoming Payments</h4>
	        	</li>
	        	<g:set var="date" value="${transaction.getNextDate(new Date())}" />
	        	<g:while test="${date != null && date < endDate}">
					<li>
						<h4 class="listview-content">
							<g:formatDate date="${date}" format="MMM d, yyyy" />
						</h4>
						<h4 class="listview-note ui-li-aside">
							<g:formatNumber number="${transaction.amount}" type="currency" currencyCode="USD" />
						</h4> 
					</li>
	        		<g:set var="date" value="${transaction.getNextDate(date + 1)}" />
	        	</g:while>
			</ul>
			<br />
			
			<!-- Include the delete form to allow deleting the transaction -->
			<g:form action="delete" name="delete-trans" id="${transaction.id}" params="[type:transaction.type]">
				<g:submitButton name="delete" data-role="button" data-theme="e"
		                        data-icon="delete" data-transition="slideup" data-direction="reverse"
	   		 		            value="${message(code: 'default.button.delete.label', default: 'Delete')}"
	   		 		            onclick="return (confirm('Are you sure you want to delete ${transaction.name}'));" />
            </g:form>  	
        </content>
    </body>
</html>
