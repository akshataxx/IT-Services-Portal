package model.domain;

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

	public UserBean(UserRole role, String firstName, String surname) {
		Objects.requireNonNull(role);
		Objects.requireNonNull(firstName);
		Objects.requireNonNull(surname);

		this.uniqueId = UUID.randomUUID();
		this.firstName = firstName.toLowerCase(Locale.ROOT);
		this.surname = surname.toLowerCase(Locale.ROOT);
		this.role = role;
		this.email = null;
		this.username = null;
		this.password = null;
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
		this.firstName = firstName.toLowerCase(Locale.ROOT);
	}

	public void setSurname(String surname) {
		this.surname = surname.toLowerCase(Locale.ROOT);
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setContactNo(String contactNo) {
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