package com.example.googleanalytics1;

class Item {

    private String  ItemName;

    private int  ItemImage;

    private String specifications;



    public Item(String ItemName, int ItemImage, String specifications) {
        this.ItemName = ItemName;
        this.ItemImage = ItemImage;
        this.specifications = specifications;
    }



    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String name) {
        ItemName = name;
    }

    public int getItemImage() {
        return ItemImage;
    }

    public void setItemImage(int image) {
        this.ItemImage = image;
    }

    public String getSpecifications() {
        return specifications ;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }
}
