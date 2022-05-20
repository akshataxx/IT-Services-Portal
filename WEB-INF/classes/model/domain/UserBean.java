package model.domain;

import util.Preconditions;

import java.util.*;

/**
 * Represents a user in the system. A user can either be an {@link UserRole#USER} or {@link UserRole#IT_STAFF}. The user has several
 * attributes. Notify a user with {@link #addUnreadNotification(NotificationBean)}. Each user has several issues {@link #getIssues()}. Users
 * can open, comment and accept solutions to their issues.
 */
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
	private final Set<IssueBean> issues;
	private int unreadNotifications;

	//create user
	public UserBean(UserRole role, String firstName, String lastName, String username, String password) {
		//make sure database contraints are met
		Preconditions.validateNotNull(role);
		Preconditions.validateNotNull(username);
		Preconditions.validateNotNull(password);
		Preconditions.validateNotNull(firstName);
		Preconditions.validateNotNull(lastName);

		//new unique id
		this.uniqueId = UUID.randomUUID();
		setUsername(username);
		setPassword(password);
		setFirstName(firstName);
		setSurname(surname);
		this.role = role;
		this.email = null;
		this.contactNo = null;

		this.notifications = new ArrayList<>();
		this.issues = new HashSet<>();
		unreadNotifications = 0;
	}

	//serialize user from database
	private UserBean(UUID uniqueId, String firstName, String surname, String email, String username, String password, String contactNo, int unreadNotifications, UserRole role) {
		this.uniqueId = uniqueId;
		this.firstName = firstName;
		this.surname = surname;
		this.password = password;
		this.username = username;
		this.contactNo = contactNo;
		this.role = role;
		this.email = email;
		this.unreadNotifications = unreadNotifications;
		this.notifications = new ArrayList<>();
		this.issues = new HashSet<>();
	}

	//user getters
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

	//user setters, make sure database preconditions are met
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

	//add a notification for the user
	public void addNotification(NotificationBean notification) {
		Objects.requireNonNull(notification);
		this.notifications.add(notification);
	}

	//add a notification, set it as unread.
	public void addUnreadNotification(NotificationBean notificationBean) {
		addNotification(notificationBean);
		unreadNotifications++;
	}

	public int getUnreadNotifications() {
		return unreadNotifications;
	}

	//sets unread notifications to 0
	public void readNotifications() {
		unreadNotifications = 0;
	}

	//return a notification a user has
	public NotificationBean getNotification(UUID uuid) {
		for(NotificationBean notification : notifications) {
			if(notification.getUniqueId().equals(uuid))
				return notification;
		}
		return null;
	}

	public Collection<IssueBean> getIssues() {
		return Collections.unmodifiableSet(issues);
	}

	public void addIssue(IssueBean issueBean) {
		this.issues.add(issueBean);
	}

	//Serialize a user from database attributes.
	public static UserBean serialize(String uniqueId, String firstName, String surname, String email, String username, String password, String contactNo, int unreadNotifications, String role) throws SerializationException {
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

		return new UserBean(uuid,firstName,surname,email,username,password,contactNo,unreadNotifications,userRole);
	}

	@Override
	public String toString() {
		return "User[name="+firstName + ",surname="+surname+",id="+uniqueId+"]";
	}
}