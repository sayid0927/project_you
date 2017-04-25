package com.zxly.o2o.util;

public class UrlManager
{
    private static UrlManager mUrlManager;
    
    public final static String BASE_URL = "http://api.appstore.qmsr55.com/";
    
    public final static int CHANNEL_ID = 888;
    
    private UrlManager()
    {
    }
    
    public static UrlManager getInstance()
    {
        if (mUrlManager == null)
        {
            mUrlManager = new UrlManager();
        }
        return mUrlManager;
    }
    
    public String getRankingListUrl(String cid, int order, int page, int size)
    {
        String url = "";
        url =
            BASE_URL + "?action=getapplist&order=" + order + "&cid=" + cid + "&size=" + size + "&p=" + page
                + "&channelid=" + CHANNEL_ID;
        return url;
    }
    
}
