
<!-- 
Company name
	

Address
	

Zip code
	

Bank details
	

Factory address
	

Notes
	
	

 -->
<%@ page import="ellematica.server.Supplier" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'supplier.label', default: 'Supplier')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="supplier.id.label" default="Id" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: supplierInstance, field: "id")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="supplier.code.label" default="Code" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: supplierInstance, field: "code")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="supplier.name.label" default="Company Name" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: supplierInstance, field: "name")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="supplier.address.label" default="Address" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: supplierInstance, field: "address")}</td>
                            
                        </tr>
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="supplier.zipCode.label" default="Zip Code" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: supplierInstance, field: "zipCode")}</td>
                            
                        </tr>
                         <tr class="prop">
                            <td valign="top" class="name"><g:message code="supplier.bankDetails.label" default="Bank Details" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: supplierInstance, field: "bankDetails")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="supplier.factoryAddress.label" default="Factory Address" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: supplierInstance, field: "factoryAddress")}</td>
                            
                        </tr>
                    
                     
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="supplier.note.label" default="Notes" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: supplierInstance, field: "note")}</td>
                            
                        </tr>
                    
                       
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="supplier.priority.label" default="Priority" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: supplierInstance, field: "priority")}</td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${supplierInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
