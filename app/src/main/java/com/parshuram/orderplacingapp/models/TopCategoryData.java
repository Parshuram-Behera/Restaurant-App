package com.parshuram.orderplacingapp.models;

public class TopCategoryData {
    private String categoryName;
    private int categoryimage;
    private boolean isExpandable;

    public TopCategoryData(String categoryName, int categoryimage) {
        this.categoryName = categoryName;
        this.categoryimage = categoryimage;
        this.isExpandable = false;
    }

    public boolean isExpandable() {
        return isExpandable;
    }

    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryimage() {
        return categoryimage;
    }

    public void setCategoryimage(int categoryimage) {
        this.categoryimage = categoryimage;
    }
}
