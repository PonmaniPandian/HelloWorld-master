package com.example.helloworld;

import java.sql.SQLException;

import com.vaadin.data.Item;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.Reindeer;

public class NormalForm extends Window {
	
	public Form form = new Form();
	public Button btnSave = new Button();
	public Button btnCancel = new Button();
	
	public NormalForm() {
		final KontrahFieldFactory kontrahFieldFactory = new KontrahFieldFactory();
		
        form = new Form() {
        	@Override
        	public void setItemDataSource(Item newDataSource) {
        		if (newDataSource != null) {
        			super.setItemDataSource(newDataSource);
        			if (newDataSource.getItemProperty("ID_FIRMA").getValue() != null) {
        				kontrahFieldFactory.cbFirmy.select(new RowId(new Object[] { newDataSource.getItemProperty("ID_FIRMA").getValue() }));
        			}
        		} else {
        			super.setItemDataSource(null);
        		}
        	}
        };
		
		form.setFormFieldFactory(kontrahFieldFactory);
		form.setWriteThrough(false);
        form.setInvalidCommitted(false);
        
        btnSave.setCaption("Save");
        btnSave.setClickShortcut(KeyCode.ENTER, null);
        btnSave.setStyleName(Reindeer.BUTTON_SMALL);
		btnSave.addListener(new Button.ClickListener() {
					
			@Override
			public void buttonClick(ClickEvent event) {	
				try {
					form.commit();
					
					try {
						AppData.getDataSource().getKontrahContainer().commit();
					} catch (UnsupportedOperationException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					((Window) getWindow().getParent()).removeWindow(getWindow());
				} catch (Exception e) {
				}
			}
		});
		
		btnCancel.setCaption("Cancel");
		btnCancel.setClickShortcut(KeyCode.ESCAPE, null);
		btnCancel.setStyleName(Reindeer.BUTTON_SMALL);
		btnCancel.addListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				form.discard();
				try {
					AppData.getDataSource().getKontrahContainer().rollback();
				} catch (UnsupportedOperationException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				((Window) getWindow().getParent()).removeWindow(getWindow());
			}
		});        
		
		this.addComponent(form);
		HorizontalLayout footer = new HorizontalLayout();
		footer.setSpacing(true);
		footer.addComponent(btnCancel);
		footer.addComponent(btnSave);
		this.addComponent(footer);
		
        this.setWidth("320px");
        this.setResizable(false);
        this.center();
        this.setCaption("Customer edit (normal form)");
        this.setClosable(false);
        this.setModal(true);
	}

}
