package model.application;

import model.application.storage.StorageImplementation;
import model.domain.UserBean;

import java.util.Collection;
import java.util.UUID;

public class UserManager {

    private final StorageImplementation storage;

    public UserManager(StorageImplementation storage) {
        this.storage = storage;
    }

    public LoginStatus login(String username, String password) {
        UserBean userBean = storage.getUser(username,password);
        if(userBean==null)
            return LoginStatus.failure("Unknown Username or Password");

        return LoginStatus.success(userBean);
    }

    public UserBean getUserById(UUID uniqueId) {
        return storage.getUser(uniqueId);
    }

    public Collection<UserBean> getAllUsers() {
        return storage.getAllUsers();
    }

    public void updateUser(UserBean user) {
        storage.updateUser(user);
    }
}
