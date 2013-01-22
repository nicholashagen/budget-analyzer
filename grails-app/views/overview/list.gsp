<!-- 
Define the main overview page that lists the running balance over time based
on the recurring instances of the transactions. This uses the main layout view.
-->

<%@ page import="budget.analyzer.Transaction" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        
        <title>Balance</title>
    </head>
    <body data-id="overview">
    
        <!-- Define the buttons associated with this page -->
    	<content tag="buttons">
			<g:link action="list" class="ui-btn-left" params="[type:type]"
			        data-icon="refresh" data-transition="fade">
				Refresh
			</g:link>  
			<g:link action="addPayment" class="ui-btn-right"
			        data-icon="add" data-rel="dialog" data-transition="pop">
				Add
			</g:link>   	
    	</content>
    	
    	<!-- 
    	Define the main page content that includes a listview of the previous
    	transactions and upcoming transactions as well as the total/active
    	balance.
    	-->
    	<content tag="content">
	        <ul id="overview-list" data-role="listview" data-dividertheme="b">
	        
	            <!-- Include today's available balance -->
	        	<li data-theme="e">
	        		<h4 class="listview-content">Today's Balance</h4>
	        		<h4 class="listview-note ui-li-aside">
	        			<g:link action="editBalance"
	        			        data-rel="dialog" data-transition="pop">
	        				<g:formatNumber number="${balance}" type="currency" currencyCode="USD" />
	        			</g:link>
	        		</h4>
	        	</li>
	        	
	        	<!-- Include the previous days transactions -->
	        	<g:if test="${previous}">
	        		<li data-role="list-divider" onclick="collapse();" style="cursor:pointer;">
	        			<span>Paid Recently</span>
	        			<span class="ui-li-aside" style="margin: 0 !important;">
	        				<g:formatNumber number="${previous.values()*.items.flatten()*.amount.sum()}" type="currency" currencyCode="USD" />
	        			</span>
	        		</li>
		        	<g:each in="${previous.values()}" status="i" var="group">
		        		<g:each in="${group.items}" status="j" var="item">
				        	<li id="collapse-li-${i}-${j}"> 
								<g:link controller="transaction" action="show" id="${item.transaction.id}"
								        params="[type:item.transaction.type]"
								        class="ui-btn-right" data-transition="slide">
					        		<h4 class="listview-info ${item.transaction.type}">${item.date.date}</h4>
									<h4 class="listview-content">
										${item.name}
									</h4>
									<h4 class="listview-note ui-li-aside">
										<g:formatNumber number="${item.amount}" type="currency" currencyCode="USD" />
									</h4>
								</g:link>  
							</li>
						</g:each>
					</g:each>
	        	</g:if>
	        	
	        	<!-- 
	        	Include all upcoming transactions and the update to the running
	        	balance. This is separated out by month/year as well.
	        	-->
	        	<g:each in="${upcoming.values()}" status="i" var="group">
		        	<li data-role="list-divider">${group}</li>
		        	<g:each in="${group.items}" status="j" var="item">
			        	<li> 
							<g:link controller="transaction" action="show" 
							        params="[type:item.transaction.type]" id="${item.transaction.id}"
							        class="ui-btn-right" data-transition="slide">
				        		<h4 class="listview-info ${item.transaction.type}">${item.date.date}</h4>
								<h4 class="listview-content">
									${item.name}
								</h4>
								<h4 class="listview-note ui-li-aside amount">
									<g:formatNumber number="${item.amount}" type="currency" currencyCode="USD" />
								</h4>
								<h4 class="ui-li-aside buttons" style="display:none;">
									<a href="index.html" data-role="button" data-inline="true">Cancel</a>
									<a href="index.html" data-role="button" data-inline="true" data-theme="b" data-icon="gear">Edit</a>
									<a href="index.html" data-role="button" data-inline="true" data-theme="e" data-icon="delete">Delete</a>
								</h4>
							</g:link> 
						</li>
					</g:each>
				</g:each>
	        </ul>
	        
	        <!-- 
	        Include the page-side javascript to allow updating a single item
	        including deleting, changing its balance, etc
	        -->
	        <g:javascript>
	        	$('#page').live('pageinit', function(event) {
	        		$('#overview-list li').bind('taphold', function() {
	        			$(this).find('a').bind('click', function() { return false; });
	        			
	        			var expanded = $(this).data('expanded');
	        			if (expanded === undefined || expanded === null) { expanded = false; }
	        			
	        			expanded = !expanded
	        			$(this).data('expanded', expanded);
	        			
	        			var $buttons = $(this).find('h4.ui-li-aside.buttons');
	        			if (expanded) {
		       				$buttons.show('slide', { direction:'right' }, 'fast');
		       			}
		       			else {
		       				$buttons.hide('slide', { direction:'right' }, 'fast');
		       			}
	        		});
	        	});
	        </g:javascript>
	    </content>
    </body>
</html>
