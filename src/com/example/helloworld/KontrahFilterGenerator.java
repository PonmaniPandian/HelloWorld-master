package com.example.helloworld;

import org.tepi.filtertable.FilterGenerator;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;

public class KontrahFilterGenerator implements FilterGenerator {

	@Override
	public Filter generateFilter(Object propertyId, Object value) {
        if ("ID".equals(propertyId) || "WIEK".equals(propertyId)) {
            /* Create an 'equals' filter for the ID field */
            if (value != null && value instanceof String) {
                try {
                    return new Compare.Equal(propertyId,
                            Integer.parseInt((String) value));
                } catch (NumberFormatException ignored) {
                    // If no integer was entered, just generate default filter
                }
            }
        }
        
        if ("ID_FIRMA".equals(propertyId)) {
        	if (value != null) {
        		return new Compare.Equal(propertyId, Integer.parseInt(value.toString()));
        	}
        }
		return null;
	}

	@Override
	public AbstractField getCustomFilterComponent(Object propertyId) {
		if ("ID_FIRMA".equals(propertyId)) {
			ComboBox cbFirmy = new ComboBox();
			cbFirmy.setContainerDataSource(AppData.getDataSource().getFirmyContainer());
			cbFirmy.setItemCaptionPropertyId("NAZWA");
			cbFirmy.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);	    
			return cbFirmy;
		}
		return null;
	}

	@Override
	public void filterRemoved(Object propertyId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void filterAdded(Object propertyId,
			Class<? extends Filter> filterType, Object value) {
		// TODO Auto-generated method stub

	}

}
