package model.domain;

import util.Preconditions;

import java.util.*;

public class UserBean implements DatabaseSerializable {

	private final UUID uniqueId;
	private String firstName;
	private String surname;
	private String email;
	private String username;
	private String password;
	private String contactNo;
	private final UserRole role;
	private final List<NotificationBean> notifications;
	private final List<IssueBean> issues;

	public UserBean(UserRole role, String firstName, String lastName, String username, String password) {
		Preconditions.validateNotNull(role);
		Preconditions.validateNotNull(username);
		Preconditions.validateNotNull(password);
		Preconditions.validateNotNull(firstName);
		Preconditions.validateNotNull(lastName);

		this.uniqueId = UUID.randomUUID();
		setUsername(username);
		setPassword(password);
		setFirstName(firstName);
		setSurname(surname);
		this.role = role;
		this.email = null;
		this.contactNo = null;

		this.notifications = new ArrayList<>();
		this.issues = new ArrayList<>();
	}

	private UserBean(UUID uniqueId, String firstName, String surname, String email, String username, String password, String contactNo, UserRole role) {
		this.uniqueId = uniqueId;
		this.firstName = firstName;
		this.surname = surname;
		this.password = password;
		this.username = username;
		this.contactNo = contactNo;
		this.role = role;
		this.email = email;
		this.notifications = new ArrayList<>();
		this.issues = new ArrayList<>();
	}

	public String getFirstName() {
		return firstName;
	}

	public String getSurname() {
		return surname;
	}

	public String getEmail() {
		return email;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setFirstName(String firstName) {
		Preconditions.validateLength(firstName,35);
		Preconditions.validateNotNull(firstName);
		this.firstName = firstName.toLowerCase(Locale.ROOT);
	}

	public void setSurname(String surname) {
		Preconditions.validateLength(surname,35);
		Preconditions.validateNotNull(surname);
		this.surname = surname.toLowerCase(Locale.ROOT);
	}

	public void setEmail(String email) {
		Preconditions.validateLength(email,320);
		this.email = email;
	}

	public void setUsername(String username) {
		Preconditions.validateLength(username,20);
		Preconditions.validateNotNull(username);
		this.username = username;
	}

	public void setPassword(String password) {
		Preconditions.validateLength(password,20);
		Preconditions.validateNotNull(password);
		this.password = password;
	}

	public void setContactNo(String contactNo) {
		Preconditions.validateLength(contactNo,35);
		this.contactNo = contactNo;
	}

	public UserRole getRole() {
		return role;
	}

	public UUID getUniqueId() {
		return uniqueId;
	}

	public Collection<NotificationBean> getNotifications() {
		return Collections.unmodifiableList(notifications);
	}

	public void addNotification(NotificationBean notification) {
		Objects.requireNonNull(notification);
		this.notifications.add(notification);
	}

	public NotificationBean getNotification(UUID uuid) {
		for(NotificationBean notification : notifications) {
			if(notification.getUniqueId().equals(uuid))
				return notification;
		}
		return null;
	}

	public Collection<IssueBean> getIssues() {
		return Collections.unmodifiableList(issues);
	}

	public void addIssue(IssueBean issueBean) {
		this.issues.add(issueBean);
	}

	public static UserBean serialize(String uniqueId, String firstName, String surname, String email, String username, String password, String contactNo, String role) throws SerializationException {
		UUID uuid;
		try {
			uuid = UUID.fromString(uniqueId);
		} catch (IllegalArgumentException e) {
			throw new SerializationException("Invalid UUId",e);
		}
		if(firstName==null || surname == null)
			throw new SerializationException("null name");

		UserRole userRole;
		try {
			userRole = UserRole.valueOf(role);
		} catch (IllegalArgumentException e) {
			throw new SerializationException("Unknown user role",e);
		}

		return new UserBean(uuid,firstName,surname,email,username,password,contactNo,userRole);
	}

	@Override
	public String toString() {
		return "User[name="+firstName + ",surname="+surname+",id="+uniqueId+"]";
	}
}