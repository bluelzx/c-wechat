package com.lvtu.wechat.common.model.activity.h5coupon;

import com.lvtu.wechat.common.base.BaseModel;

/**
 * 广告产品的信息
 * @author wxlizhi
 *
 */
public class ProductAdvertising extends BaseModel{
    
    /**
     * 
     */
    private static final long serialVersionUID = 831897088448925292L;

    /**
     *产品的ID 
     */
    private String productId;
    
    /**
     * 产品类型
     */
    private String productType;
    
    /**
     * 产品名称
     */
    private String productName;
    
    /**
     * 产品价格
     */
    private Float productPrice;
    
    /**
     * 产品图片的URL
     */
    private String productImgURL;
    
    /**
     * 产品分享链接
     */
    private String productWapURL;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Float getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Float productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImgURL() {
        return productImgURL;
    }

    public void setProductImgURL(String productImgURL) {
        this.productImgURL = productImgURL;
    }

    public String getProductWapURL() {
        return productWapURL;
    }

    public void setProductWapURL(String productWapURL) {
        this.productWapURL = productWapURL;
    }
    
}
