//
// JODConverter - Java OpenDocument Converter
// Copyright 2004-2011 Mirko Nasato and contributors
//
// JODConverter is free software: you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public License
// as published by the Free Software Foundation, either version 3 of
// the License, or (at your option) any later version.
//
// JODConverter is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General
// Public License along with JODConverter.  If not, see
// <http://www.gnu.org/licenses/>.
//
package org.artofsolving.jodconverter;

import static org.artofsolving.jodconverter.office.OfficeUtils.cast;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.artofsolving.jodconverter.document.DocumentFamily;
import org.artofsolving.jodconverter.document.DocumentFormat;
import org.artofsolving.jodconverter.office.OfficeException;

import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.container.XIndexAccess;
import com.sun.star.container.XNameAccess;
import com.sun.star.container.XNameContainer;
import com.sun.star.frame.XController;
import com.sun.star.frame.XModel;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheetView;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.style.XStyle;
import com.sun.star.style.XStyleFamiliesSupplier;
import com.sun.star.util.XRefreshable;
import com.sun.star.view.PaperOrientation;
import com.sun.star.view.XPrintable;

public class StandardConversionTask extends AbstractConversionTask {

    private final DocumentFormat outputFormat;

    private Map<String,?> defaultLoadProperties;
    private DocumentFormat inputFormat;

    public StandardConversionTask(File inputFile, File outputFile, DocumentFormat outputFormat) {
        super(inputFile, outputFile);
        this.outputFormat = outputFormat;
    }

    public void setDefaultLoadProperties(Map<String, ?> defaultLoadProperties) {
        this.defaultLoadProperties = defaultLoadProperties;
    }

    public void setInputFormat(DocumentFormat inputFormat) {
        this.inputFormat = inputFormat;
    }

    @Override
    protected void modifyDocument(XComponent document) throws OfficeException {
        XRefreshable refreshable = cast(XRefreshable.class, document);
        if (refreshable != null) {
            refreshable.refresh();
        }
        if(inputFormat.getExtension().equals("xls") && (outputFormat.getExtension().equals("pdf") || outputFormat.getExtension().equals("xls"))){
        	XSpreadsheetDocument    calcDoc_XSpreadsheetDocument    = cast(XSpreadsheetDocument.class, document );
        	XSpreadsheets xp =calcDoc_XSpreadsheetDocument.getSheets(); 
        	XIndexAccess sheets_XIndexAccess = cast( XIndexAccess.class,  xp); 
        	int ns = sheets_XIndexAccess.getCount();
        	String name[]=xp.getElementNames();
        	for(int i=1;i<ns;i++){
        		try {
        			System.out.println(name[i]);
							xp.removeByName(name[i]);
						} catch (NoSuchElementException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (WrappedTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
        	   XStyleFamiliesSupplier xSupplier = cast(XStyleFamiliesSupplier.class, document);
             XNameAccess xFamilies = cast(XNameAccess.class, xSupplier.getStyleFamilies());
             XPrintable xPrintable;
             XSpreadsheet sheet=null;
						try {
							sheet=cast(XSpreadsheet.class, xp.getByName(name[0]));
							
						} catch (NoSuchElementException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (WrappedTargetException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						xPrintable = cast(XPrintable.class, document);
             PropertyValue[] printerDesc = new PropertyValue[2];
             printerDesc[0] = new PropertyValue();
             printerDesc[0].Name = "PaperOrientation";
             printerDesc[0].Value = PaperOrientation.LANDSCAPE;
          
             try{
              xPrintable.setPrinter(printerDesc);
      	      XNameContainer xFamily = cast(XNameContainer.class, xFamilies.getByName("PageStyles"));

     	        
     	       XSpreadsheetDocument spreadsheetDocument = cast(XSpreadsheetDocument.class, document);
             XModel model = cast(XModel.class, spreadsheetDocument);
             XController controller = model.getCurrentController();
             XSpreadsheetView view = cast(XSpreadsheetView.class,controller);
             XPropertySet xps = cast(XPropertySet.class, view.getActiveSheet());
             String pageStyleName = xps.getPropertyValue("PageStyle") + "";
             XStyle xStyle = (XStyle) cast(XStyle.class, xFamily.getByName(pageStyleName));
     	        
     	        
     	        
     	        
     	        XPropertySet xStyleProps = cast(XPropertySet.class, xStyle);
     	        xStyleProps.setPropertyValue("ScaleToPages", new Short((short) 1));
     	        xStyleProps.setPropertyValue("ScaleToPagesX", new Short((short) 1));
     	        xStyleProps.setPropertyValue("ScaleToPagesY", new Short((short) 2));
     	       
             }catch(Exception e){
             	throw new OfficeException("here",e);
             }
             
        		
        	}
        	
        }
    }

    @Override
    protected Map<String,?> getLoadProperties(File inputFile) {
        Map<String,Object> loadProperties = new HashMap<String,Object>();
        if (defaultLoadProperties != null) {
            loadProperties.putAll(defaultLoadProperties);
        }
        if (inputFormat != null && inputFormat.getLoadProperties() != null) {
            loadProperties.putAll(inputFormat.getLoadProperties());
        }
        return loadProperties;
    }

    @Override
    protected Map<String,?> getStoreProperties(File outputFile, XComponent document) {
        DocumentFamily family = OfficeDocumentUtils.getDocumentFamily(document);
        return outputFormat.getStoreProperties(family);
    }

}
