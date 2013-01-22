<!--
Define the edit balance dialog that allows a user to change the current 
balance.  This is a simple dialog layout.
-->

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="dialog" />
        
        <title>Set Balance</title>
    </head>
    <body>
    	<g:form action="saveBalance">
    	
            <!-- Define the form elements (balance) -->
    		<div data-role="fieldcontain">
				<label for="balance"><g:message code="balance.label" default="Balance" /></label>
              	<g:textField name="balance" type="number" min="0" step="any" value="${balance}" />
			</div>
			
			<!-- Define the cancel and submit buttons -->
			<fieldset class="ui-grid-a">
				<div class="ui-block-a">
					<a href="#" data-role="button" data-theme="d" data-rel="back">Cancel</a>
				</div>
				<div class="ui-block-b">
					<button type="submit" data-theme="a">Submit</button>
				</div>
	    	</fieldset>
    	</g:form>
    </body>
</html>
