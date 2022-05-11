package model.domain;

import java.util.*;

public class CategoryDefinition {

    private final String name;
    public final String displayName;
    private final Set<CategoryDefinition> subCategories;

    public CategoryDefinition(String name, String displayName , CategoryDefinition[] subCategories) {
        this.name = name;
        this.displayName = displayName;
        if(subCategories!=null) {
            this.subCategories = new HashSet<>(Arrays.asList(subCategories));
        } else {
            this.subCategories = new HashSet<>();
        }
    }

    public CategoryDefinition(String name, String displayName) {
        this(name,displayName,null);
    }

    public Collection<CategoryDefinition> getSubCategories() {
        return Collections.unmodifiableSet(subCategories);
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }


    public boolean equals(Object o) {
        if(!(o instanceof CategoryDefinition))
            return false;

        CategoryDefinition def = (CategoryDefinition) o;
        return def.name.equals(this.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String toString() {
        return name;
    }

    public static CategoryDefinition[] values() {
        return new CategoryDefinition[]{NETWORK,SOFTWARE,HARDWARE,EMAIL,ACCOUNT};
    }

    public static CategoryDefinition valueOf(String name) {
        if(name==null)
            throw new IllegalArgumentException("null");
        name = name.toUpperCase(Locale.ROOT);
        for(CategoryDefinition main : values()) {
            if(name.equalsIgnoreCase(main.name))
                return main;

            for(CategoryDefinition sub : main.subCategories) {
                if(name.equalsIgnoreCase(sub.name))
                    return sub;
            }
        }
        throw new IllegalArgumentException("Unknown Category Definition");
    }

    public static final CategoryDefinition CANT_CONNECT = new CategoryDefinition("CANT_CONNECT","Can't connect ");

    public static final CategoryDefinition SPEED = new CategoryDefinition("SPEED","Speed");

    public static final CategoryDefinition CONSTANT_DROPOUTS = new CategoryDefinition("CONSTANT_DROPOUTS","Constant dropouts");

    public static final CategoryDefinition NETWORK = new CategoryDefinition("NETWORK","Network", new CategoryDefinition[]{CANT_CONNECT, SPEED, CONSTANT_DROPOUTS});

    public static final CategoryDefinition SLOW_TO_LOAD = new CategoryDefinition("SLOW_TO_LOAD","Slow to load");

    public static final CategoryDefinition WONT_LOAD = new CategoryDefinition("WONT_LOAD","Won't load at all");

    public static final CategoryDefinition SOFTWARE = new CategoryDefinition("SOFTWARE","Software",new CategoryDefinition[]{SLOW_TO_LOAD,WONT_LOAD});

    public static final CategoryDefinition COMP_WONT_TURN_ON = new CategoryDefinition("COMP_WONT_TURN_ON","Computer won't turn on");

    public static final CategoryDefinition COMP_BLUE_SCREEN = new CategoryDefinition("COMP_BLUE_SCREEN","Computer \"blue screens\"");

    public static final CategoryDefinition DISK_DRIVE = new CategoryDefinition("DISK_DRIVE","Disk drive");

    public static final CategoryDefinition PERIPHERALS = new CategoryDefinition("PERIPHERALS","Peripherals");

    public static final CategoryDefinition HARDWARE = new CategoryDefinition("HARDWARE","Hardware",new CategoryDefinition[]{COMP_WONT_TURN_ON,COMP_BLUE_SCREEN,DISK_DRIVE,PERIPHERALS});

    public static final CategoryDefinition CANT_SEND = new CategoryDefinition("CANT_SEND","Can't send");

    public static final CategoryDefinition CANT_RECEIVE = new CategoryDefinition("CANT_RECEIVE","Can't receive");

    public static final CategoryDefinition SPAM = new CategoryDefinition("SPAM","SPAM/Phishing");

    public static final CategoryDefinition EMAIL = new CategoryDefinition("EMAIL","Email",new CategoryDefinition[]{CANT_RECEIVE,CANT_SEND,SPAM});

    public static final CategoryDefinition PASSWORD_RESET = new CategoryDefinition("PASSWORD_RESET","Password reset");

    public static final CategoryDefinition WRONG_DETAILS = new CategoryDefinition("WRONG_DETAILS","Wrong details");

    public static final CategoryDefinition ACCOUNT = new CategoryDefinition("ACCOUNT","Account",new CategoryDefinition[]{PASSWORD_RESET,WRONG_DETAILS});
}
