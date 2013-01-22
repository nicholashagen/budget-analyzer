<!-- 
Define the add payment view that allows users to create a one time payment.
This is a simple dialog that includes the transaction name, amount, and
associated date. 
-->

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="dialog" />
        
        <title>Add Payment</title>
    </head>
    <body>
    	<g:form action="savePayment">
    	
            <!-- Define the form elements (name, amount, date, etc) -->
    		<div id="name-container" data-role="fieldcontain" class="${hasErrors(bean: transaction, field: 'name', 'errors')}">
             	<label for="name"><g:message code="transaction.name.label" default="Name" /></label>
            	<g:textField name="name" value="${fieldValue(bean: transaction, field: 'name')}" />
             </div>
             <div id="amount-container" data-role="fieldcontain" class="${hasErrors(bean: transaction, field: 'amount', 'errors')}">
             	<label for="amount"><g:message code="transaction.amount.label" default="Amount" /></label>
           		<g:field name="amount" type="number" min="0" step="any" value="${fieldValue(bean: transaction, field: 'amount')}" />
           	</div>
    		<div id="date-container" data-role="fieldcontain" class="${hasErrors(bean: transaction, field: 'startDate', 'errors')}">
				<label for="startDate"><g:message code="transaction.date.label" default="Date" /></label>
               	<g:field name="startDate" type="date" value="${transaction.startDate.format('yyyy-MM-dd')}" />
			</div>
			
			<!-- Define the cancel and submit buttons for the form -->
			<fieldset class="ui-grid-a">
				<div class="ui-block-a">
					<a href="#" data-role="button" data-icon="delete" data-theme="a" data-rel="back">Cancel</a>
				</div>
				<div class="ui-block-b">
					<button type="submit" data-icon="check" data-theme="b">Submit</button>
				</div>
	    	</fieldset>
    	</g:form>
    </body>
</html>
