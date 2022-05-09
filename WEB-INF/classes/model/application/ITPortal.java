package model.application;

import model.application.storage.StorageImplementation;

public class ITPortal {

    private volatile static ITPortal instance;
    private IssueManager issueManager;
    private UserManager userManager;
    private StorageImplementation storageImplementation;

    public ITPortal() {
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
        this.storageImplementation = implementation;
        this.issueManager = new IssueManager(storageImplementation);
        this.userManager = new UserManager(storageImplementation);
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
