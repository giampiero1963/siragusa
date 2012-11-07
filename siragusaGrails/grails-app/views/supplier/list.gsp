
<%@ page import="ellematica.server.Supplier" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'supplier.label', default: 'Supplier')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'supplier.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="code" title="${message(code: 'supplier.code.label', default: 'Code')}" />
                        
                            <g:sortableColumn property="name" title="${message(code: 'supplier.name.label', default: 'Name')}" />
                        
                            <g:sortableColumn property="address" title="${message(code: 'supplier.address.label', default: 'Address')}" />
                        
                            <g:sortableColumn property="factoryAddress" title="${message(code: 'supplier.factoryAddress.label', default: 'Factory Address')}" />
                        
                            <g:sortableColumn property="zipCode" title="${message(code: 'supplier.zipCode.label', default: 'Zip Code')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${supplierInstanceList}" status="i" var="supplierInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${supplierInstance.id}">${fieldValue(bean: supplierInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: supplierInstance, field: "code")}</td>
                        
                            <td>${fieldValue(bean: supplierInstance, field: "name")}</td>
                        
                            <td>${fieldValue(bean: supplierInstance, field: "address")}</td>
                        
                            <td>${fieldValue(bean: supplierInstance, field: "factoryAddress")}</td>
                        
                            <td>${fieldValue(bean: supplierInstance, field: "zipCode")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${supplierInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
