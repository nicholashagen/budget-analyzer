<!-- 
This provides a jQuery Mobile dialog layout that all dialogs should extend
from.
-->

<!DOCTYPE html>
<html>
    <head>
        <title><g:layoutTitle default="Overview" /></title>

        <!-- Define our content type and mobile scaling parameters -->
		<meta content="text/html; charset=iso-8859-1" http-equiv="Content-Type" />
		<meta content="minimum-scale=1.0, width=device-width, maximum-scale=0.6667, user-scalable=no" name="viewport" /> 

        <!-- Add our resource files including jQuery Mobile -->
		<link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />
		<link rel="stylesheet" href="http://code.jquery.com/mobile/1.2.0/jquery.mobile-1.3.0-beta.1.min.css" />
		
		<script src="http://code.jquery.com/jquery-1.8.3.min.js"></script>
		<script src="http://code.jquery.com/mobile/1.3.0-beta.1/jquery.mobile-1.3.0-beta.1.min.js"></script>

        <g:layoutHead />
    </head>
    <body>
    	<div data-role="dialog">
			<div data-role="header" data-theme="d">
				<h1><g:layoutTitle default="Overview" /></h1>
			</div>

			<div data-role="content" data-theme="c">
				<g:layoutBody />
			</div>
		</div>
    </body>
</html>
