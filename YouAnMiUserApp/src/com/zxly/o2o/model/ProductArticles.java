package com.zxly.o2o.model;

/**
 * Created by dsnx on 2015/12/25.
 */
public class ProductArticles {
    private String content;
    private int enjoyAmount;
    private long id;
    private String imageUrl;
    private String title;
    private long productId;
    private float price;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getEnjoyAmount() {
        return enjoyAmount;
    }

    public void setEnjoyAmount(int enjoyAmount) {
        this.enjoyAmount = enjoyAmount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductArticles that = (ProductArticles) o;

        return id == that.id;

    }


}
