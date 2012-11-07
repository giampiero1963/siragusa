<!-- 

ID

Code
	

Company name
	

Address
	

Zip Code
	

Tel
	

Fax
	

Eori
	

Express air courier
	

Notes
	

Shipping forwarder
	

Shipping mark
	

Bank details
	

Notes
	

Priority
	




 -->
<%@ page import="ellematica.server.Customer" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'customer.label', default: 'Customer')}" />
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
                            <td valign="top" class="name"><g:message code="customer.id.label" default="Id" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: customerInstance, field: "id")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="customer.code.label" default="Code" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: customerInstance, field: "code")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="customer.name.label" default="Company Name" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: customerInstance, field: "name")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="customer.address.label" default="Address" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: customerInstance, field: "address")}</td>
                            
                        </tr>
                    
                       
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="customer.zipCode.label" default="Zip Code" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: customerInstance, field: "zipCode")}</td>
                            
                        </tr>
                    	<tr class="prop">
                            <td valign="top" class="name"><g:message code="customer.tel.label" default="Tel" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: customerInstance, field: "tel")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="customer.fax.label" default="Fax" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: customerInstance, field: "fax")}</td>
                            
                        </tr>
                         <tr class="prop">
                            <td valign="top" class="name"><g:message code="customer.eori.label" default="Eori" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: customerInstance, field: "eori")}</td>
                            
                        </tr>
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="customer.expressAirCourier.label" default="Express Air Courier" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: customerInstance, field: "expressAirCourier")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="customer.note.label" default="Notes" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: customerInstance, field: "note")}</td>
                            
                        </tr>
                    
 
 
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="customer.shippingForwarder.label" default="Shipping Forwarder" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: customerInstance, field: "shippingForwarder")}</td>
                            
                        </tr>
                    
                       
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="customer.shippingMark.label" default="Shipping Mark" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: customerInstance, field: "shippingMark")}</td>
                            
                        </tr>
                    
                                           <tr class="prop">
                            <td valign="top" class="name"><g:message code="customer.bankDetails.label" default="Bank Details" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: customerInstance, field: "bankDetails")}</td>
                            
                        </tr>
                    
                        
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="customer.note2.label" default="Notes" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: customerInstance, field: "note2")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="customer.priority.label" default="Priority" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: customerInstance, field: "priority")}</td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${customerInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
