
<%@ page import="ellematica.server.Customer" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'customer.label', default: 'Customer')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'customer.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="code" title="${message(code: 'customer.code.label', default: 'Code')}" />
                        
                            <g:sortableColumn property="name" title="${message(code: 'customer.name.label', default: 'Name')}" />
                        
                            <g:sortableColumn property="address" title="${message(code: 'customer.address.label', default: 'Address')}" />
                        
                            <g:sortableColumn property="factoryAddress" title="${message(code: 'customer.factoryAddress.label', default: 'Factory Address')}" />
                        
                            <g:sortableColumn property="zipCode" title="${message(code: 'customer.zipCode.label', default: 'Zip Code')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${customerInstanceList}" status="i" var="customerInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${customerInstance.id}">${fieldValue(bean: customerInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: customerInstance, field: "code")}</td>
                        
                            <td>${fieldValue(bean: customerInstance, field: "name")}</td>
                        
                            <td>${fieldValue(bean: customerInstance, field: "address")}</td>
                        
                            <td>${fieldValue(bean: customerInstance, field: "factoryAddress")}</td>
                        
                            <td>${fieldValue(bean: customerInstance, field: "zipCode")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${customerInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
