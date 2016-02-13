/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.butler.data;

import java.io.Serializable;


/**
 *
 * @author arsh
 */
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private String name;
    private String displayName;
    private String sizeSpecification;
    private Float marketPrice;
    private Float sellingPrice;
    private String image;
    private String type;
    private boolean visible;
    private boolean bookingOnly;
    private int displayPosition;
    public Product() {
    }

    public Product(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }





    public String getPic() {
        return "api/product/image?name="+this.name;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Product)) {
            return false;
        }
        Product other = (Product) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.butler.data.Product[ id=" + id + " ]";
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return the sizeSpecification
     */
    public String getSizeSpecification() {
        return sizeSpecification;
    }

    /**
     * @param sizeSpecification the sizeSpecification to set
     */
    public void setSizeSpecification(String sizeSpecification) {
        this.sizeSpecification = sizeSpecification;
    }

    /**
     * @return the actualPrice
     */
    public Float getMarketPrice() {
        return marketPrice;
    }

    /**
     * @param actualPrice the actualPrice to set
     */
    public void setMarketPrice(Float actualPrice) {
        this.marketPrice = actualPrice;
    }

    /**
     * @return the offerPrice
     */
    public Float getSellingPrice() {
        return sellingPrice;
    }

    /**
     * @param offerPrice the offerPrice to set
     */
    public void setSellingPrice(Float offerPrice) {
        this.sellingPrice = offerPrice;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * @param visible the visible to set
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * @return the bookingOnly
     */
    public boolean isBookingOnly() {
        return bookingOnly;
    }

    /**
     * @param bookingOnly the bookingOnly to set
     */
    public void setBookingOnly(boolean bookingOnly) {
        this.bookingOnly = bookingOnly;
    }

    /**
     * @return the displayPosition
     */
    public int getDisplayPosition() {
        return displayPosition;
    }

    /**
     * @param displayPosition the displayPosition to set
     */
    public void setDisplayPosition(int displayPosition) {
        this.displayPosition = displayPosition;
    }

    
}
