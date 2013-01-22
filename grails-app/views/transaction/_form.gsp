<!-- 
Define the shared sub-template for edit/create pages that includes the form for
the transaction (name, amount, recursion, etc).  This also includes the button
actions for submitting the form.
-->

<%@ page import="budget.analyzer.Transaction" %>

<div id="name-container" data-role="fieldcontain" class="${hasErrors(bean: transaction, field: 'name', 'errors')}">
    <label for="name"><g:message code="transaction.name.label" default="Name" /></label>
    <g:textField name="name" value="${fieldValue(bean: transaction, field: 'name')}" />
</div>
<div id="amount-container" data-role="fieldcontain" class=" ${hasErrors(bean: transaction, field: 'amount', 'errors')}">
    <label for="amount"><g:message code="transaction.amount.label" default="Amount" /></label>
    <g:field type="number" min="0" step="any" name="amount" value="${fieldValue(bean: transaction, field: 'amount')}" />
</div>
<div id="recursion-container" data-role="fieldcontain" class="${hasErrors(bean: transaction, field: 'recursion', 'errors')}">
    <fieldset data-role="controlgroup" data-type="horizontal">
        <legend><g:message code="transaction.recursion.label" default="Schedule" /></legend>
            <g:each var="recursion" status="idx" in="${[[name:'Monthly', value:Transaction.RECURSION_MONTHLY], [name:'Weekly', value:Transaction.RECURSION_WEEKLY]]}">
            <g:if test="${transaction.recursion == recursion.value}">
                <g:radio id="recursion-${idx}" name="recursion" value="${recursion.value}"
                         onchange="handleRecursion(this);" checked="true" />
            </g:if>
            <g:else>
                <g:radio id="recursion-${idx}" name="recursion" value="${recursion.value}"
                         onchange="handleRecursion(this);" />                               
            </g:else>
            <label for="recursion-${idx}">${recursion.name}</label>
        </g:each>
    </fieldset>
</div>
<div id="date-container" data-role="fieldcontain" class="${hasErrors(bean: transaction, field: 'date', 'errors')}" 
     style="display: ${fieldValue(bean: transaction, field: 'recursion') == Transaction.RECURSION_MONTHLY.toString() ? 'block' : 'none'};">
    <label for="date"><g:message code="transaction.date.label" default="Date" /></label>
    <g:select name="date" from="${1..28}" noSelection="['-1':'--select--']"
              value="${fieldValue(bean: transaction, field: 'date')}" />
</div>
<div id="weeks-container" data-role="fieldcontain" class="${hasErrors(bean: transaction, field: 'weeks', 'errors')}"
     style="display: ${fieldValue(bean: transaction, field: 'recursion') == Transaction.RECURSION_WEEKLY.toString() ? 'block' : 'none'};">
    <label for="weeks"><g:message code="transaction.weeks.label" default="Every" /></label>
    <g:select name="weeks" from="${[[name:'1 Week', value:1], [name:'2 Weeks', value:2], [name:'3 Weeks', value:3], [name:'4 Weeks', value:4]]}" 
              optionValue="name" optionKey="value" noSelection="['-1':'--select--']" 
              value="${fieldValue(bean: transaction, field: 'weeks')}" />
</div>
<div id="day-container" data-role="fieldcontain" class="${hasErrors(bean: transaction, field: 'day', 'errors')}"
     style="display: ${fieldValue(bean: transaction, field: 'recursion') == Transaction.RECURSION_WEEKLY.toString() ? 'block' : 'none'};">
    <label for="day"><g:message code="transaction.day.label" default="Day" /></label>
    <g:select name="day" from="${[[name:'Sunday', value:1], [name:'Tuesday', value:2], [name:'Wednesday', value:3], [name:'Thursday', value:4], [name:'Friday', value:5], [name:'Saturday', value:6]]}" 
              optionValue="name" optionKey="value" noSelection="['-1':'--select--']" 
              value="${fieldValue(bean: transaction, field: 'day')}" />
</div>
<div id="startdate-container" data-role="fieldcontain" class="${hasErrors(bean: transaction, field: 'startDate', 'errors')}"
     style="display: ${fieldValue(bean: transaction, field: 'recursion') == Transaction.RECURSION_WEEKLY.toString() ? 'block' : 'none'};">
    <label for="startDate"><g:message code="transaction.startDate.label" default="Starting" /></label>
    <g:field name="startDate" type="date" value="${(transaction.startDate ?: new Date()).format('yyyy-MM-dd') }" />
</div>

<!-- Include the page actions for submitting the form -->
<fieldset class="ui-grid-a">
    <div class="ui-block-a">
        <g:link action="list" params="[type:type]" 
                data-role="button" data-icon="delete" data-theme="a"
                data-transition="slideup" data-direction="reverse">
            Cancel
        </g:link>
    </div>
    <div class="ui-block-b">
        <button type="submit" data-theme="b" data-icon="check"
                data-transition="slideup" data-direction="reverse">
            Submit
        </button>
    </div>
</fieldset>

<!-- 
Include the javascript for showing/hiding the various fields based on the
selected recursion
-->
<g:javascript>
    function handleRecursion(radio) {
        var value = parseInt(radio.value),
            monthly = (value == ${Transaction.RECURSION_MONTHLY}),
            weekly = (value == ${Transaction.RECURSION_WEEKLY});
            
        $('#date-container').toggle(monthly);
        $('#weeks-container').toggle(weekly);
        $('#day-container').toggle(weekly);
        $('#startdate-container').toggle(weekly);
    }
</g:javascript>