package com.example.helloworld;

import java.io.Serializable;
import org.vaadin.jonatan.contexthelp.ContextHelp;
import com.vaadin.Application;
import com.vaadin.service.ApplicationContext.TransactionListener;
import com.vaadin.ui.Window;

/** Holds data for one user session. */
public class AppData implements TransactionListener, Serializable {
	private boolean firstInit = true;
    private DataSourceContainers dataSource = new DataSourceContainers();
    private ContextHelp contextHelp = new ContextHelp();
    
    private Application app; 
    
    // For distinguishing between apps
    private static ThreadLocal<AppData> instance = new ThreadLocal<AppData>();
    
    public AppData(Application app) {
    	this.app = app;
    	
        // It's usable from now on in the current request
        instance.set(this);
    }

    @Override
    public void transactionStart(Application application, Object transactionData) {
        // Set this data instance of this application
        // as the one active in the current thread. 
        if (this.app == application)
            instance.set(this);
    }

    @Override
    public void transactionEnd(Application application, Object transactionData) {
        // Clear the reference to avoid potential problems
        if (this.app == application)
            instance.set(null);
    }
    
    public static DataSourceContainers getDataSource() {
		return instance.get().dataSource;
	}
    
    public static ContextHelp getContextHelp() {
		return instance.get().contextHelp;
	}
    
    public static Window getAppWindow() {
    	return instance.get().app.getMainWindow();
    }
    
    public static void setFirstInit(boolean value) {
    	instance.get().firstInit = value;
    }
    
    public static boolean getFirstInit() {
    	return instance.get().firstInit;
    }
    
}
