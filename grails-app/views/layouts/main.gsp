<!-- 
This provides the base layout that all pages should extend from.  The base
layout includes the page structure, definition, header, layout elements, and
all other common aspects of the page.
-->

<%@ page import="budget.analyzer.Transaction" %>

<!DOCTYPE html>
<html>
    <head>
        <title><g:layoutTitle default="Overview" /></title>

        <!-- Define the content type and mobile scaling parameters -->
        <meta content="text/html; charset=iso-8859-1" http-equiv="Content-Type" />
        <meta content="minimum-scale=1.0, width=device-width, maximum-scale=0.6667, user-scalable=no" name="viewport" /> 
        
        <!-- Support iOS devices saving icon to home screen -->
        <meta content="yes" name="apple-mobile-web-app-capable" />
        
        <!-- Define the application icon -->
        <link rel="shortcut icon" href="/favicon.png" />

        <!-- Define the icons and splash screen for iOS devices -->
        <link rel="apple-touch-icon" href="/touch.png"/>
        <link rel="apple-touch-startup-image" href="/splash.png" />

        <!-- Include our resources including jQuery Mobile -->
		<link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />
		<link rel="stylesheet" href="http://code.jquery.com/mobile/1.3.0-beta.1/jquery.mobile-1.3.0-beta.1.min.css" />
		
		<script src="http://code.jquery.com/jquery-1.8.3.min.js"></script>
		<script src="http://code.jquery.com/mobile/1.3.0-beta.1/jquery.mobile-1.3.0-beta.1.min.js"></script>

        <g:layoutHead />
    </head>
    <body>
		<div id="page" data-role="page">
		
            <!-- Include page header with title and optional buttons -->
            <!-- The header is fixed to the top of the page -->
			<div data-role="header" data-id="header" data-position="fixed" 
			     data-nobackbtn="${pageProperty(name:'body.data-nobackbtn') == 'true'}">
			     
				<h1>
				    <!-- Set the title as a link to allow forceful refreshing -->
					<g:link class="nolink" action="${actionName}" controller="${controllerName}" 
					        params="${params.type ? [type:params.type] : []}" data-ajax="false">
						<g:layoutTitle default="Overview" />
					</g:link>
				</h1>
				
				<g:pageProperty name="page.buttons" />
			</div>

			<div data-role="content">
				<div>
					<g:pageProperty name="page.content" />
				</div>
			</div>

            <!-- Include the footer and navigation if this is a standard page -->
            <!-- The footer stays fixed to the bottom at all times -->
			<g:if test="${pageProperty(name:'body.data-role') != 'dialog' && pageProperty(name:'body.data-nofooter') != 'true'}">
				<div data-role="footer" data-id="nav" data-position="fixed">
				
				    <!-- 
				    The navigation bar includes the overview page and each of
				    the main transaction types.
				    --> 
					<div data-role="navbar">
						<ul>
							<li>
								<g:link controller="overview" action="list" data-transition="none"
								        class="${pageProperty(name:'body.data-id') == 'overview' ? 'ui-btn-active' : ''}">
								    Balance
								</g:link>
							</li>
							<g:each in="${Transaction.types()}" var="type">
								<li>
									<g:link controller="transaction" action="list" 
								            params="[type:type]" data-transition="none"
								            class="${pageProperty(name:'body.data-id') == type ? 'ui-btn-active' : ''}">
								    	${Transaction.title(type)}
								    </g:link>
								</li>
							</g:each>
						</ul>
					</div>
				</div>
			</g:if>
		</div>
    </body>
</html>
