package com.sadev.sharebook.book;

public class Category {
    private String label;

    public Category() {
    }

    public Category(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "Category{" +
                "label='" + label + '\'' +
                '}';
    }
}
