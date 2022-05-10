package model.domain;

import util.Preconditions;

import java.util.UUID;

public class CommentBean implements DatabaseSerializable, TextElement {

    private final UUID uniqueId;
    private final UserBean userBean;
    private final long timePosted;
    private String comment;

    public CommentBean(UserBean userBean, String comment) {
        Preconditions.validateNotNull(userBean);
        this.uniqueId = UUID.randomUUID();
        this.userBean = userBean;
        this.timePosted = System.currentTimeMillis();
        setText(comment);
    }

    private CommentBean(UUID uniqueId, UserBean userBean, long timePosted, String comment) {
        this.uniqueId = uniqueId;
        this.userBean = userBean;
        this.timePosted = timePosted;
        this.comment = comment;
    }

    public UserBean getAuthor() {
        return userBean;
    }

    public long getTimePosted() {
        return timePosted;
    }

    public String getText() {
        return comment;
    }

    @Override
    public void setText(String text) {
        Preconditions.validateLength(comment,500);
        Preconditions.validateNotNull(comment);
        this.comment = text;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public static CommentBean serialize(String uniqueId, long timePosted, String comment, UserBean user) throws SerializationException {
        if(comment == null)
            throw new SerializationException("null comment");
        UUID uuid;
        try {
            uuid = UUID.fromString(uniqueId);
        } catch (IllegalArgumentException e) {
            throw new SerializationException("Bad UUID '"+uniqueId+"'",e);
        }

        return new CommentBean(uuid,user,timePosted,comment);
    }


    @Override
    public String toString() {
        return "Comment[id="+uniqueId + ",content="+comment+"]";
    }
}
