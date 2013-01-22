<!-- 
Define the shared template for displaying error messages.
-->

<g:if test="${flash.message}">
    <ul data-role="listview" data-theme="d" style="height:50px;">
        <li style="text-align: center;">${flash.message}</li>
    </ul>            
</g:if>
<g:hasErrors bean="${transaction}">
    <ul data-role="listview" data-theme="e" style="height:50px;">
        <g:eachError bean="${transaction}">
            <li style="text-align: center;">${message(error:it)}</li>
        </g:eachError>
    </ul>
</g:hasErrors>