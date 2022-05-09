package model.domain;

public class Category {

    private final CategoryDefinition main;
    private final CategoryDefinition sub; //for sub sub sub categories, just make this a category

    public Category(CategoryDefinition main, CategoryDefinition sub) {
        this.main = main;
        if(!main.getSubCategories().contains(sub))
            throw new IllegalArgumentException("Subcategory '"+sub+"' is not a subcategory of '"+main+"'");

        this.sub = sub;
    }

    public CategoryDefinition getMain() {
        return main;
    }

    public CategoryDefinition getSub() {
        return sub;
    }

    public String toString() {
        return "("+main+", "+sub+")";
    }
}
