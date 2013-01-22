<!-- 
This defines the create transaction page based on the associated type (bill,
budget, etc).  This includes the standard form for transactions.  Note that this
view does not include the footer.
-->

<%@ page import="budget.analyzer.Transaction" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        
        <title>Add ${Transaction.title(type)}</title>
    </head>
    <body data-nofooter="true">
    
        <!-- Include the buttons for the page header -->
       	<content tag="buttons">
			<g:link action="list" params="[type:type]" 
			        data-icon="delete" data-theme="a"
			        data-transition="slideup" data-direction="reverse">
				Cancel
			</g:link>
    	</content>
    	
    	<!-- 
    	Include the page content as the error messages and the actual form 
    	-->
    	<content tag="content">
            <g:render template="messages" />
            <g:form action="save" params="[type:type]" name="create-trans">
				<g:render template="form" model="[transaction:transaction]" />
            </g:form>
        </content>
    </body>
</html>
