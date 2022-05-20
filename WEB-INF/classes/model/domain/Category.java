package model.domain;

import java.util.*;

public class Category {

    private final CategoryDefinition main;
    private final CategoryDefinition sub; //for sub sub sub categories, just make this a category

    public Category(CategoryDefinition main) {
        this(main, null);
    }

    public Category(CategoryDefinition main, CategoryDefinition sub) {
        this.main = main;
        if(sub!=null && !main.getSubCategories().contains(sub))
            throw new IllegalArgumentException("Subcategory '"+sub+"' is not a subcategory of '"+main+"'");

        this.sub = sub;
    }

    public CategoryDefinition getMain() {
        return main;
    }

    public Category asMain() {
        return new Category(main);
    }

    public CategoryDefinition getSub() {
        return sub;
    }

    public boolean equals(Object o) {
        if(!(o instanceof Category))
            return false;

        Category category = (Category) o;
        return main.equals(category.main) && Objects.equals(sub,category.sub);
    }

    public String toString() {
        if(sub==null)
            return main.toString();

        return "("+main+", "+sub+")";
    }

    public boolean isSubCategory() {
        return sub!=null;
    }

    public static Collection<Category> values() {
        List<Category> values = new ArrayList<>();
        for(CategoryDefinition main : CategoryDefinition.values()) {
            values.add(new Category(main));
            for(CategoryDefinition sub : main.getSubCategories()) {
                values.add(new Category(main,sub));
            }
        }
        return values;
    }

    public static Collection<Category> getValues() {
        return values();
    }


    @Override
    public int hashCode() {
        if(sub==null)
            return Objects.hash(main);

        return Objects.hash(main,sub);
    }
}
