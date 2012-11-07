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
<div class="dialog">
    <table>
        <tbody>
        
            <tr class="prop">
                <td valign="top" class="name">
                    <label for="code"><g:message code="customer.code.label" default="Code" /></label>
                </td>
                <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'code', 'errors')}">
                    <g:textField name="code" maxlength="3" value="${customerInstance?.code}" />
                </td>
            </tr>
        
            <tr class="prop">
                <td valign="top" class="name">
                    <label for="name"><g:message code="customer.name.label" default="Company Name" /></label>
                </td>
                <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'name', 'errors')}">
                    <g:textField name="name" maxlength="200" value="${customerInstance?.name}" />
                </td>
            </tr>
        
            <tr class="prop">
                <td valign="top" class="name">
                    <label for="address"><g:message code="customer.address.label" default="Address" /></label>
                </td>
                <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'address', 'errors')}">
                    <g:textArea name="address" cols="40" rows="5" value="${customerInstance?.address}" />
                </td>
            </tr>
           <tr class="prop">
                <td valign="top" class="name">
                    <label for="zipCode"><g:message code="customer.zipCode.label" default="Zip Code" /></label>
                </td>
                <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'zipCode', 'errors')}">
                    <g:textField name="zipCode" maxlength="5" value="${customerInstance?.zipCode}" />
                </td>
            </tr>
             <tr class="prop">
                <td valign="top" class="name">
                    <label for="tel"><g:message code="customer.tel.label" default="Tel" /></label>
                </td>
                <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'tel', 'errors')}">
                    <g:textField name="tel" maxlength="25" value="${customerInstance?.tel}" />
                </td>
            </tr>
        
            <tr class="prop">
                <td valign="top" class="name">
                    <label for="fax"><g:message code="customer.fax.label" default="Fax" /></label>
                </td>
                <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'fax', 'errors')}">
                    <g:textField name="fax" maxlength="25" value="${customerInstance?.fax}" />
                </td>
            </tr>
            <tr class="prop">
                <td valign="top" class="name">
                    <label for="eori"><g:message code="customer.eori.label" default="Eori" /></label>
                </td>
                <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'eori', 'errors')}">
                    <g:textField name="eori" maxlength="20" value="${customerInstance?.eori}" />
                </td>
            </tr>
            <tr class="prop">
                <td valign="top" class="name">
                    <label for="expressAirCourier"><g:message code="customer.expressAirCourier.label" default="Express Air Courier" /></label>
                </td>
                <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'expressAirCourier', 'errors')}">
                    <g:textArea name="expressAirCourier" cols="40" rows="5" value="${customerInstance?.expressAirCourier}" />
                </td>
            </tr>
            <tr class="prop">
                <td valign="top" class="name">
                    <label for="note"><g:message code="customer.note.label" default="Notes" /></label>
                </td>
                <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'note', 'errors')}">
                    <g:textArea name="note" cols="40" rows="5" value="${customerInstance?.note}" />
                </td>
            </tr>
        
             <tr class="prop">
                <td valign="top" class="name">
                    <label for="shippingForwarder"><g:message code="customer.shippingForwarder.label" default="Shipping Forwarder" /></label>
                </td>
                <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'shippingForwarder', 'errors')}">
                    <g:textArea name="shippingForwarder" cols="40" rows="5" value="${customerInstance?.shippingForwarder}" />
                </td>
            </tr>
        
            
        
            <tr class="prop">
                <td valign="top" class="name">
                    <label for="shippingMark"><g:message code="customer.shippingMark.label" default="Shipping Mark" /></label>
                </td>
                <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'shippingMark', 'errors')}">
                    <g:textArea name="shippingMark" cols="40" rows="5" value="${customerInstance?.shippingMark}" />
                </td>
            </tr>
         
        
        
        
            <tr class="prop">
                <td valign="top" class="name">
                    <label for="bankDetails"><g:message code="customer.bankDetails.label" default="Bank Details" /></label>
                </td>
                <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'bankDetails', 'errors')}">
                    <g:textArea name="bankDetails" cols="40" rows="5" value="${customerInstance?.bankDetails}" />
                </td>
            </tr>
        
           
        
            <tr class="prop">
                <td valign="top" class="name">
                    <label for="note2"><g:message code="customer.note2.label" default="Notes" /></label>
                </td>
                <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'note2', 'errors')}">
                    <g:textArea name="note2" cols="40" rows="5" value="${customerInstance?.note2}" />
                </td>
            </tr>
        
         
         
        
            <tr class="prop">
                <td valign="top" class="name">
                    <label for="priority"><g:message code="customer.priority.label" default="Priority" /></label>
                </td>
                <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'priority', 'errors')}">
                    <g:textField name="priority" value="${fieldValue(bean: customerInstance, field: 'priority')}" />
                </td>
            </tr>
        
        </tbody>
    </table>
</div>