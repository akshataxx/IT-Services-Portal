package model.domain;

import util.Preconditions;

import java.util.Date;
import java.util.UUID;

public class NotificationBean implements DatabaseSerializable {

    private final UUID uniqueId;
    private final String title;
    private final String comment;
    private final long postTime;
    private final IssueBean issueBean;

    public NotificationBean(String title, String comment, IssueBean issueBean) {
        this.uniqueId = UUID.randomUUID();
        Preconditions.validateLength(title,100);
        Preconditions.validateLength(comment,350);
        Preconditions.validateNotNull(title);
        Preconditions.validateNotNull(comment);
        this.title = title;
        this.comment = comment;
        this.postTime = System.currentTimeMillis();
        this.issueBean = issueBean;
    }

    private NotificationBean(String title, String comment, long postTime, UUID uniqueId, IssueBean issueBean) {
        this.title = title;
        this.comment = comment;
        this.uniqueId = uniqueId;
        this.issueBean = issueBean;
        this.postTime = postTime;
    }

    public String getTitle() {
        return title;
    }

    public String getComment() {
        return comment;
    }

    public long getPostTime() {
        return postTime;
    }

    public Date getPostDate() {
        return new Date(postTime);
    }

    public IssueBean getIssueBean() {
        return issueBean;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public static NotificationBean serialize(String uniqueId, String title, long timePosted, String comment, IssueBean issueBean) throws SerializationException {
        if(title==null)
            throw new SerializationException("null title");
        UUID uuid;
        try {
            uuid = UUID.fromString(uniqueId);
        } catch (IllegalArgumentException e) {
            throw new SerializationException("Bad UUID '"+uniqueId+"'",e);
        }
        return new NotificationBean(title,comment,timePosted,uuid,issueBean);
    }

    @Override
    public String toString() {
        return "Notification[id="+uniqueId + ",title="+title+"]";
    }
}
