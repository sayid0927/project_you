package com.zxly.o2o.model;

/**
 * Created by hejun on 2016/10/8.
 */
public class TargetTypeNameConstants {
    private String targetType;
    public TargetTypeNameConstants(String targetType) {
        setTargetType(targetType);
    }

    private void setTargetType(String targetType) {
       if(targetType.equals("Article")){
            this.targetType="文章";
       }else if(targetType.equals("Product")){
           this.targetType="商品";
       }else if(targetType.equals("Post")){
           this.targetType="帖子";
       }else if(targetType.equals("Insurance")){
           this.targetType="延保";
       }else if(targetType.equals("Mobile_phone")){
           this.targetType="手机";
       }else if(targetType.equals("Tablet")){
           this.targetType="平板";
       }else if(targetType.equals("Parts")){
           this.targetType="配件";
       }else if(targetType.equals("activity")){
           this.targetType="活动";
       }else if(targetType.equals("flow")){
           this.targetType="流量";
       }else if(targetType.equals("cmcc")){
           this.targetType="中国移动";
       }else if(targetType.equals("unicom")){
           this.targetType="中国联通";
       }else if(targetType.equals("telecom")){
           this.targetType="中国电信";
       }else {
           this.targetType=targetType;
       }
    }

    public String getTargetType(){
        return targetType;
    }
}
