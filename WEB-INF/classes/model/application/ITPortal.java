package model.application;

import model.application.storage.StorageException;
import model.application.storage.StorageImplementation;

import java.sql.SQLException;

public class ITPortal {

    private volatile static ITPortal instance;
    private IssueManager issueManager;
    private UserManager userManager;
    private boolean initialised;

    public ITPortal() {
        issueManager = null;
        userManager = null;
        initialised = false;
    }

    public static ITPortal getInstance() {
        ITPortal localRef = instance;
        if (localRef == null) {
            synchronized (ITPortal.class) {
                localRef = instance;
                if (localRef == null) {
                    instance = localRef = new ITPortal();
                }
            }
        }
        return localRef;
    }

    public void setStorageImplementation(StorageImplementation implementation) {
        try {
            implementation.initialise();
            initialised = true;
        } catch (StorageException e) {
            System.out.println("----oO Storage Initialization Exception Oo---");
            e.printStackTrace();
            return;
        }
        this.issueManager = new IssueManager(implementation);
        this.userManager = new UserManager(implementation);
    }

    public boolean isInitialised() {
        return initialised;
    }

    public IssueManager getIssueManager() {
        if(this.issueManager==null)
            throw new IllegalStateException("Storage not initialised");
        return issueManager;
    }

    public UserManager getUserManager() {
        if(this.userManager==null)
            throw new IllegalStateException("Storage not initialised");
        return userManager;
    }
}
