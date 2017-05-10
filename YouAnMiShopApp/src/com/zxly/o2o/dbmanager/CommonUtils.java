package com.zxly.o2o.dbmanager;

import android.content.Context;
import android.util.Log;

import com.Shop.dao.ShopDao;
import com.Shop.entity.Shop;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * 完成对某一张表的具体操作, ORM 操作的是对象
 */
public class CommonUtils {
    private DaoManager mDaoManager;

    public CommonUtils(Context context) {
        mDaoManager = DaoManager.getInstance();
        mDaoManager.init(context);
    }

    /**
     * 完成对数据库表的插入操作-->并且会检测数据库是否存在,不存在自己创建,
     */
    public boolean insertShop(Shop Shop) {
        boolean flag = false;
        flag = mDaoManager.getSession().insert(Shop) != -1;//不等于-1是true 否则是false
        Log.i("MainActivity", "insertShop: " + flag);
        return flag;
    }

    /**
     * 同时插入多条记录
     */
    public boolean insertMultShop(final List<Shop> Shops) {
        boolean flag = false;
        try {
            mDaoManager.getSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (Shop s : Shops) {
                        mDaoManager.getSession().insertOrReplace(s);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("MainActivity", "insertMultShop: " + flag);
        return false;
    }

    /**
     * 修改指定记录
     */
    public boolean uoDateShop(Shop Shop) {
        boolean flag = false;
        try {
            mDaoManager.getSession().update(Shop);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("MainActivity", "uoDateShop: " + flag);
        return flag;
    }

    /**
     * 删除指定记录
     */
    public boolean deleteShop(Shop Shop) {
        boolean flag = false;
        try {
            mDaoManager.getSession().delete(Shop);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("MainActivity", "deleteShop: " + flag);
        return flag;
    }

    /**
     * 删除所有的记录
     */
    public boolean deleteAll() {
        boolean flag = false;
        try {
            mDaoManager.getSession().deleteAll(Shop.class);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("aaa", "deleteAll: " + flag);
        return flag;
    }

    /**
     * 查询 某一个表 的 所有记录
     */
    public List<Shop> listAll() {
        return mDaoManager.getSession().loadAll(Shop.class);
    }

    /**
     * 按照主键查询某一个 表 中 的单行记录
     */
    public Shop listOneShop(long key) {
        return mDaoManager.getSession().load(Shop.class, key);
    }

    /**
     * 按照sql语句进行查询
     */
    public void queryBySql() {
        List<Shop> list = mDaoManager.getSession().queryRaw(Shop.class, "where name like ? and _id<=?", new String[]{"%jo%", "4"});
        for (Shop s : list) {
            Log.i("MainActivity", s.getId() + "");
        }
    }

    /**
     * 使用查询构建器进行查询
     */
    public List<Shop> queryByBuilder(String clickid) {
        QueryBuilder<Shop> queryBuilder = mDaoManager.getSession().queryBuilder(Shop.class);
        queryBuilder.where(ShopDao.Properties.Clickid.eq(clickid));
        List<Shop> list = queryBuilder.list();
        return  list;
    }
}
