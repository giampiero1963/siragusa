<!-- 
Company name
	

Address
	

Zip code
	

Bank details
	

Factory address
	

Notes
	
	

 -->


     <div class="dialog">
         <table>
             <tbody>
             
                 <tr class="prop">
                     <td valign="top" class="name">
                         <label for="code"><g:message code="supplier.code.label" default="Code" /></label>
                     </td>
                     <td valign="top" class="value ${hasErrors(bean: supplierInstance, field: 'code', 'errors')}">
                         <g:textField name="code" maxlength="3" value="${supplierInstance?.code}" />
                     </td>
                 </tr>
             
                 <tr class="prop">
                     <td valign="top" class="name">
                         <label for="name"><g:message code="supplier.name.label" default="Company Name" /></label>
                     </td>
                     <td valign="top" class="value ${hasErrors(bean: supplierInstance, field: 'name', 'errors')}">
                         <g:textField name="name" maxlength="200" value="${supplierInstance?.name}" />
                     </td>
                 </tr>
             
                 <tr class="prop">
                     <td valign="top" class="name">
                         <label for="address"><g:message code="supplier.address.label" default="Address" /></label>
                     </td>
                     <td valign="top" class="value ${hasErrors(bean: supplierInstance, field: 'address', 'errors')}">
                         <g:textArea name="address" cols="40" rows="5" value="${supplierInstance?.address}" />
                     </td>
                 </tr>
                 
                  <tr class="prop">
                     <td valign="top" class="name">
                         <label for="zipCode"><g:message code="supplier.zipCode.label" default="Zip Code" /></label>
                     </td>
                     <td valign="top" class="value ${hasErrors(bean: supplierInstance, field: 'zipCode', 'errors')}">
                         <g:textField name="zipCode" maxlength="5" value="${supplierInstance?.zipCode}" />
                     </td>
                 </tr>
                 
             	 <tr class="prop">
                     <td valign="top" class="name">
                         <label for="bankDetails"><g:message code="supplier.bankDetails.label" default="Bank Details" /></label>
                     </td>
                     <td valign="top" class="value ${hasErrors(bean: supplierInstance, field: 'bankDetails', 'errors')}">
                         <g:textArea name="bankDetails" cols="40" rows="5" value="${supplierInstance?.bankDetails}" />
                     </td>
                 </tr>
             
             
                 <tr class="prop">
                     <td valign="top" class="name">
                         <label for="factoryAddress"><g:message code="supplier.factoryAddress.label" default="Factory Address" /></label>
                     </td>
                     <td valign="top" class="value ${hasErrors(bean: supplierInstance, field: 'factoryAddress', 'errors')}">
                         <g:textArea name="factoryAddress" cols="40" rows="5" value="${supplierInstance?.factoryAddress}" />
                     </td>
                 </tr>
             
                
             
                 <tr class="prop">
                     <td valign="top" class="name">
                         <label for="note"><g:message code="supplier.note.label" default="Notes" /></label>
                     </td>
                     <td valign="top" class="value ${hasErrors(bean: supplierInstance, field: 'note', 'errors')}">
                         <g:textArea name="note" cols="40" rows="5" value="${supplierInstance?.note}" />
                     </td>
                 </tr>
             
                
                 <tr class="prop">
                     <td valign="top" class="name">
                         <label for="priority"><g:message code="supplier.priority.label" default="Priority" /></label>
                     </td>
                     <td valign="top" class="value ${hasErrors(bean: supplierInstance, field: 'priority', 'errors')}">
                         <g:textField name="priority" value="${fieldValue(bean: supplierInstance, field: 'priority')}" />
                     </td>
                 </tr>
             
             </tbody>
         </table>
     </div>
   