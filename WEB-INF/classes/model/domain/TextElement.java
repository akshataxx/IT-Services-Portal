package model.domain;

import java.util.Date;

public interface TextElement {

    public String getText();

    void setText(String text);

    long getDateTime();

    Date getDate();

    public UserBean getAuthor();
}
