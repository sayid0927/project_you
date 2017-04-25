package com.zxly.o2o.util;
import java.util.Iterator;
import java.util.List;
/**
 * Created by dsnx on 2015/8/21.
 */
public class ListUtil {
    /**
     * 在src中删除dest已有的元素
     *
     * 注意如果是protobuf对象，不能调用equals方法(用到反射，代码混淆后出错)
     *
     * @param src
     * @param dest
     */
    public static void deleteRepeat(List src, List dest) {
        for (Iterator it = src.iterator(); it.hasNext();) {
            Object object =  it.next();

            if (dest.contains(object))
                it.remove();
        }
    }

    /**
     * 合并list 将src元素 replace到dest
     *
     * @param <T>
     *
     * @param src
     * @param dest
     */
    public static <T> void merge(List<T> src, List<T> dest) {
        for (int i = 0; i < dest.size(); i++) {
            for (T t : src) {
                if (t.equals(dest.get(i))) {
                    dest.set(i, t);
                    break;
                }
            }
        }
        for (T it : src) {
            if (dest.contains(it))
                continue;
            dest.add(it);
        }
    }

    public static <T> void merge(T t, List<T> dest) {
        if (null == t || null == dest)
            return;
        for (int i = 0; i < dest.size(); i++) {
            if (t.equals(dest.get(i))) {
                dest.set(i, t);
                return;
            }
        }
        dest.add(t);
    }
}
