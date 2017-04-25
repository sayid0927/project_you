package com.zxly.o2o.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author     dsnx
 * @version    YIBA-O2O 2014-12-23
 * @since      YIBA-O2O
 */
public class ResultPage
{
    // 总数
    private int total;
    
    // 每页大小
    private short pageSize = 15;
    
    // 当前index
    private int curIndex = 0;
    
    // 数据list
    private List result = new ArrayList();
    
    public int getTotal()
    {
        return total;
    }
    
    public void setTotal(int total)
    {
        this.total = total;
    }
    
    public short getPageSize()
    {
        return pageSize;
    }
    
    public void setPageSize(short pageSize)
    {
        this.pageSize = pageSize;
    }
    
    public int getCurIndex()
    {
        return curIndex;
    }
    
    public void setCurIndex(int curIndex)
    {
        this.curIndex = curIndex;
    }
    
    public List getResult()
    {
        if (result == null)
            return new ArrayList();
        else
            return result;
    }
    
    public void setResult(List result)
    {
        this.result = result;
    }
    
    public void addIndex(int increase)
    {
        this.curIndex += increase;
    }
    
    public boolean isReachEnd()
    {
        return this.curIndex >= total;
    }
    
    public void add(Object o)
    {
        this.result.add(o);
    }
    
    public void clearResult()
    {
        this.result = null;
    }
    
}
