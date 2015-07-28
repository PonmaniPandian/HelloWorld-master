package com.example.helloworld;

import org.vaadin.jonatan.contexthelp.HelpFieldWrapper;

import com.vaadin.data.Item;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.TextField;

public class KontrahFieldFactory extends DefaultFieldFactory implements FormFieldFactory {
	public final ComboBox cbFirmy = new ComboBox();
	
	public KontrahFieldFactory() {
		cbFirmy.setCaption("Firma");
		cbFirmy.setNewItemsAllowed(false);
		cbFirmy.setNullSelectionAllowed(false);
		cbFirmy.setContainerDataSource(AppData.getDataSource().getFirmyContainer());
		cbFirmy.setItemCaptionPropertyId("NAZWA");
		cbFirmy.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);
		cbFirmy.setImmediate(true);
		cbFirmy.setWidth("200px");
	}

	@Override
	public Field createField(Item item, Object propertyId, Component uiContext) {
		String pid = (String)propertyId;
		
		if ("ID_FIRMA".equals(pid)) {
			return cbFirmy;
		}
		
		Field field;
		field = super.createField(item, propertyId, uiContext);
		field.setWidth("200px");
		
		if (field instanceof TextField) {
			((TextField) field).setNullRepresentation("");
		}
		
		if ("IMIE".equals(pid)) {
			field.setCaption("Imie");
			AppData.getContextHelp().addHelpForComponent(field, "Fill with your <b><i>first name</i></b>");
			return new HelpFieldWrapper(field, AppData.getContextHelp());
		}
		if ("NAZWISKO".equals(pid)) {
			field.setCaption("Nazwisko");
			field.setRequired(true);
			field.setRequiredError("Field is required");
			field.addValidator(new StringLengthValidator("Nazwisko must have at least 3 characters", 3, 100, false));
			return field;
		}
		if ("WIEK".equals(pid)) {
			field.setCaption("Wiek");
			return field;
		}
		if ("PESEL".equals(pid)) {
			field.setCaption("PESEL");
			field.setRequired(true);
			field.setRequiredError("Field is required");
			return field;
		}
		
		return null;
	}
}
