<!-- 
This defines the default index page used to redirect to the main overview page.
-->

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        
        <title>Budget Analyzer</title>
    </head>
    <body data-nofooter="true">
    	<content tag="content">
	    	<g:javascript>
	    		$(function() {
		    		$.mobile.changePage({
		           		url : '${g.createLink(controller:'overview', action:'list')}', 
		           		type : 'get'
		     		}, 'fade');
		     	});
	    	</g:javascript>
    	</content>
    </body>
</html>