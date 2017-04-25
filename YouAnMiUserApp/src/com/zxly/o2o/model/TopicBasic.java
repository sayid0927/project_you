/*
 * 文件名：TopicBasicDTO.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： TopicBasicDTO.java
 * 修改人：Administrator
 * 修改时间：2015年1月21日
 * 修改内容：新增
 */
package com.zxly.o2o.model;
import java.io.File;

/**
 * TODO 添加类的一句话简单描述。
 * <p>
 * TODO 详细描述
 * <p>
 * TODO 示例代码
 * <pre>
 * </pre>
 * 
 * @author     yuanfei
 * @version    YIBA-O2O 2015年1月21日
 * @since      YIBA-O2O
 */
public class TopicBasic
{
    private String content="";
    
    private long shopId;
    
    //private Long createTime;参数暂时不需要
    private File image;
    
    public String getContent()
    {
        return content;
    }
    
    public void setContent(String content)
    {
        this.content = content;
    }
    
    public Long getShopId()
    {
        return shopId;
    }
    
    public void setShopId(Long shopId)
    {
        this.shopId = shopId;
    }
    
    public File getImage()
    {
        return image;
    }
    
    public void setImage(File image)
    {
        this.image = image;
    }
}
