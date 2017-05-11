package com.example;


import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class MyClass {
    public static void main(String[] args) {
        Schema schema = new Schema(1, "com.Shop.entity");
        addStudent(schema);
        schema.setDefaultJavaPackageDao("com.Shop.dao");
        try {
          //  new DaoGenerator().generateAll(schema, "E:\\test\\Partner\\app\\src\\main\\java-gen");

            new DaoGenerator().generateAll(schema, "/Users/shirleyhyy/Desktop/wengmengfan/androidStudioProject/gitYanmi/" +
                    "project_you/YouAnMiShopApp/src/com/java-gen");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void addStudent(Schema schema) {
        Entity entity = schema.addEntity("Shop");
        entity.addIdProperty();
        entity.addStringProperty("clickid");
        entity.addBooleanProperty("isclick");
    }
}