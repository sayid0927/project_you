package com.easemob.chatuidemo.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.HXApplication;
import com.easemob.chatuidemo.domain.InviteMessage;
import com.easemob.chatuidemo.domain.InviteMessage.InviteMesageStatus;
import com.easemob.chatuidemo.domain.RobotUser;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.domain.EaseUser;
import com.easemob.easeui.domain.EaseYAMUser;
import com.easemob.easeui.utils.EaseCommonUtils;
import com.easemob.util.HanziToPinyin;

public class DemoDBManager {
    static private DemoDBManager dbMgr = new DemoDBManager();
    private DbOpenHelper dbHelper;

    private DemoDBManager() {
        dbHelper = DbOpenHelper.getInstance(HXApplication.applicationContext);
    }

    public static synchronized DemoDBManager getInstance() {
        if (dbMgr == null) {
            dbMgr = new DemoDBManager();
        }
        return dbMgr;
    }

    /**
     * 保存好友list
     *
     * @param contactList
     */
    synchronized public void saveContactList(List<EaseUser> contactList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen() && contactList != null && contactList.size() > 0) {
            db.delete(UserDao.TABLE_NAME, null, null);
            for (EaseUser user : contactList) {
                db.replace(UserDao.TABLE_NAME, null, setContact(user));
            }
        }
    }

    synchronized public void deleteContactList() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.execSQL("DELETE FROM " + UserDao.TABLE_NAME);
            db.execSQL("DELETE FROM " + UserDao.UNREGIST_TABLE_NAME);
            //            db.delete(UserDao.TABLE_NAME, null, null);
            //            db.delete(UserDao.UNREGIST_TABLE_NAME, null, null);

        }
    }

    synchronized public void saveUnRegistList(List<EaseUser> contactList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen() && contactList != null && contactList.size() > 0) {
            db.delete(UserDao.UNREGIST_TABLE_NAME, null, null);
            for (EaseUser user : contactList) {
                db.replace(UserDao.UNREGIST_TABLE_NAME, null, setUnRegistContact(user));
            }
        }
    }


    synchronized public void saveYAMContactList(List<EaseYAMUser> contactList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen() && contactList != null && contactList.size() > 0) {
            db.delete(UserDao.TABLE_NAME, null, null);
            for (EaseYAMUser user : contactList) {
                db.replace(UserDao.TABLE_NAME, null, setContact(user));
            }
        }
    }

    /**
     * 获取好友list
     *
     * @return
     */

    synchronized public Map<String, EaseYAMUser> getYAMContactList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Map<String, EaseYAMUser> users = new HashMap<String, EaseYAMUser>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + UserDao.TABLE_NAME /* + " desc" */, null);
            while (cursor.moveToNext()) {
                EaseUser user = new EaseUser("");
                String nickName = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_REMARKNAME));
                if(TextUtils.isEmpty(nickName)) {
                    nickName = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_NICK));
                }
                user.setNickname(nickName == null ? "" : nickName);
                String userName = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_ID));
                user.setUserName(userName == null ? "" : userName);
                if (TextUtils.isEmpty(nickName) && TextUtils.isEmpty(userName)) {
                    continue;
                }

                EaseYAMUser YamUser = new EaseYAMUser();

                String hxId = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_HX_ID));
                user.setId(cursor.getLong(cursor.getColumnIndex(UserDao.COLUMN_NAME_USERID)));
                user.setIsTop(cursor.getInt(cursor.getColumnIndex(UserDao.COLUMN_NAME_ISTOP)));
                user.setThumHeadUrl(cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_AVATAR)));
                user.setCreateTime(cursor.getLong(cursor.getColumnIndex(UserDao.COLUMN_NAME_CREATETIME)));
                user.setGender(cursor.getInt(cursor.getColumnIndex(UserDao.COLUMN_NAME_GENDER)));
                user.setOriginHeadUrl(
                        cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_ORIGINHEADURL)));
                user.setSignature(cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_SIGNATURE)));
                user.setGroupName(
                        cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_GROUPNAME)));
                user.setInitialLetter(
                        cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_INITIAL)));

                YamUser.setIsBlack(cursor.getInt(cursor.getColumnIndex(UserDao.COLUMN_NAME_ISBLACK)));
                YamUser.setRemarkName(
                        cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_REMARKNAME)));
                YamUser.setCreateTime(cursor.getLong(cursor.getColumnIndex(UserDao.COLUMN_NAME_ADDEDTIME)));

                YamUser.setFirendsUserInfo(user);
                users.put(hxId, YamUser);
            }
            cursor.close();
        }
        return users;
    }


    synchronized public List<EaseYAMUser> getUnRegistContactList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<EaseYAMUser> users = new ArrayList<EaseYAMUser>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + UserDao.UNREGIST_TABLE_NAME /* + " desc" */, null);
            while (cursor.moveToNext()) {
                EaseYAMUser YamUser = new EaseYAMUser();
                EaseUser user = new EaseUser("");
                String imei = cursor.getString(cursor.getColumnIndex(UserDao.UNREGIST_IMEI));
                user.setImei(imei);
                YamUser.setFirendsUserInfo(user);

                users.add(YamUser);
            }
            cursor.close();
        }
        return users;
    }

    /**
     * 删除一个联系人
     *
     * @param id
     */
    synchronized public void deleteYAMContactWithId(String id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(UserDao.TABLE_NAME, UserDao.COLUMN_NAME_USERID + " = ?", new String[]{id});
        }
    }

    /**
     * 删除一个联系人
     *
     * @param username
     */
    synchronized public void deleteYAMContact(String username) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(UserDao.TABLE_NAME, UserDao.COLUMN_HX_ID + " = ?", new String[]{username});
        }
    }

    /**
     * 保存一个联系人
     *
     * @param user
     */
    synchronized public void saveYAMContact(EaseYAMUser user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (db.isOpen()) {
            db.insert(UserDao.TABLE_NAME, null, setContact(user));
        }
    }

    /**
     * 更新一个联系人的黑名单状态
     *
     * @param isBlack
     */
    synchronized public void updateYAMContactBlackStatus(int isBlack, String userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (db.isOpen()) {


            ContentValues values = new ContentValues();
            values.put(UserDao.COLUMN_NAME_ISBLACK, isBlack);
            String whereClause = UserDao.COLUMN_NAME_USERID + " = ?";
            String[] whereArgs = new String[]{userId};
            db.update(UserDao.TABLE_NAME, values, whereClause, whereArgs);
        }
    }

    private ContentValues setContact(EaseYAMUser user) {
        ContentValues values = new ContentValues();
        if (user.getFirendsUserInfo().getUserName() != null) {
            values.put(UserDao.COLUMN_NAME_ID, user.getFirendsUserInfo().getUserName());
        }

        if (user.getFirendsUserInfo().getNickname() == null) {
            if (user.getFirendsUserInfo().getName() == null) {
                values.put(UserDao.COLUMN_NAME_NICK, user.getFirendsUserInfo().getUserName());
            } else {
                values.put(UserDao.COLUMN_NAME_NICK, user.getFirendsUserInfo().getName());
            }
        } else {
            values.put(UserDao.COLUMN_NAME_NICK, user.getFirendsUserInfo().getNickname());
        }
        if (user.getFirendsUserInfo().getUserId() == 0) {
            if (user.getFirendsUserInfo().getEid() == null) {
                user.getFirendsUserInfo().setEid(
                        HXApplication.getInstance().parseUserFromID(user.getFirendsUserInfo().getId(),
                                HXConstant.TAG_USER));
            }
            values.put(UserDao.COLUMN_NAME_USERID, user.getFirendsUserInfo().getId());
        } else {
            if (user.getFirendsUserInfo().getEid() == null) {
                user.getFirendsUserInfo().setEid(
                        HXApplication.getInstance().parseUserFromID(user.getFirendsUserInfo().getUserId(),
                                HXConstant.TAG_USER));
            }
            values.put(UserDao.COLUMN_NAME_USERID, user.getFirendsUserInfo().getUserId());
        }
        values.put(UserDao.COLUMN_HX_ID, user.getFirendsUserInfo().getEid());
        values.put(UserDao.COLUMN_NAME_AVATAR, user.getFirendsUserInfo().getThumHeadUrl());
        values.put(UserDao.COLUMN_NAME_CREATETIME, user.getFirendsUserInfo().getCreateTime());
        values.put(UserDao.COLUMN_NAME_GENDER, user.getFirendsUserInfo().getGender());
        values.put(UserDao.COLUMN_NAME_ORIGINHEADURL, user.getFirendsUserInfo().getOriginHeadUrl());

        if(TextUtils.isEmpty(user.getFirendsUserInfo().getSignature())){
            values.put(UserDao.COLUMN_NAME_SIGNATURE, "这个人很懒，还没有个性签名");
        }else {
            values.put(UserDao.COLUMN_NAME_SIGNATURE, user.getFirendsUserInfo().getSignature());
        }


        values.put(UserDao.COLUMN_NAME_ISTOP, user.getFirendsUserInfo().getIsTop());
        values.put(UserDao.COLUMN_NAME_ISBLACK, user.getIsBlack());
        values.put(UserDao.COLUMN_NAME_REMARKNAME, user.getRemarkName());
        values.put(UserDao.COLUMN_NAME_SHOPID, user.getShopId());
        values.put(UserDao.COLUMN_NAME_GROUPNAME, user.getFirendsUserInfo().getGroupName());
        values.put(UserDao.COLUMN_NAME_ADDEDTIME, user.getCreateTime());
        values.put(UserDao.COLUMN_NAME_GROUPID, user.getFirendsUserInfo().getGroupId());
        values.put(UserDao.COLUMN_NAME_INITIAL, EaseCommonUtils.setUserInitialLetter(user
                .getFirendsUserInfo()).getInitialLetter());

        return values;
    }


    private ContentValues setContact(EaseUser user) {
        ContentValues values = new ContentValues();

        if (user.getUserName() != null) {
            values.put(UserDao.COLUMN_NAME_ID, user.getUserName());
        }

        if (user.getNickname() == null) {
            if (user.getName() == null) {
                values.put(UserDao.COLUMN_NAME_NICK, user.getUserName());
            } else {
                values.put(UserDao.COLUMN_NAME_NICK, user.getName());
            }
        } else {
            values.put(UserDao.COLUMN_NAME_NICK, user.getNickname());
        }
        if (user.getUserId() == 0) {
            if (user.getEid() == null) {
                user.setEid(
                        HXApplication.getInstance().parseUserFromID(user.getId(),
                                HXConstant.TAG_USER));
            }
            values.put(UserDao.COLUMN_NAME_USERID, user.getId());
        } else {
            if (user.getEid() == null) {
                user.setEid(
                        HXApplication.getInstance().parseUserFromID(user.getUserId(),
                                HXConstant.TAG_USER));
            }
            values.put(UserDao.COLUMN_NAME_USERID, user.getUserId());
        }

        values.put(UserDao.COLUMN_HX_ID, user.getEid());
        values.put(UserDao.COLUMN_NAME_AVATAR, user.getThumHeadUrl());
        values.put(UserDao.COLUMN_NAME_CREATETIME, user.getCreateTime());
        values.put(UserDao.COLUMN_NAME_GENDER, user.getGender());
        values.put(UserDao.COLUMN_NAME_ORIGINHEADURL, user.getOriginHeadUrl());
        values.put(UserDao.COLUMN_NAME_SIGNATURE, user.getSignature());
        values.put(UserDao.COLUMN_NAME_USERID, user.getId());
        values.put(UserDao.COLUMN_NAME_ADDEDTIME, user.getCreateTime());
        if(user.getUserRemark()!=null)
        values.put(UserDao.COLUMN_NAME_REMARKNAME, user.getUserRemark().getRemarkName());

        values.put(UserDao.COLUMN_NAME_INITIAL, EaseCommonUtils.setUserInitialLetter(user).getInitialLetter());
        return values;
    }

    private ContentValues setUnRegistContact(EaseUser user) {
        ContentValues values = new ContentValues();
        values.put(UserDao.UNREGIST_IMEI, user.getImei());
        return values;
    }


    public void setDisabledGroups(List<String> groups) {
        setList(UserDao.COLUMN_NAME_DISABLED_GROUPS, groups);
    }

    public List<String> getDisabledGroups() {
        return getList(UserDao.COLUMN_NAME_DISABLED_GROUPS);
    }

    public void setDisabledIds(List<String> ids) {
        setList(UserDao.COLUMN_NAME_DISABLED_IDS, ids);
    }

    public List<String> getDisabledIds() {
        return getList(UserDao.COLUMN_NAME_DISABLED_IDS);
    }

    synchronized private void setList(String column, List<String> strList) {
        StringBuilder strBuilder = new StringBuilder();

        for (String hxid : strList) {
            strBuilder.append(hxid).append("$");
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(column, strBuilder.toString());

            db.update(UserDao.PREF_TABLE_NAME, values, null, null);
        }
    }

    synchronized private List<String> getList(String column) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + column + " from " + UserDao.PREF_TABLE_NAME, null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }

        String strVal = cursor.getString(0);
        if (strVal == null || strVal.equals("")) {
            return null;
        }

        cursor.close();

        String[] array = strVal.split("$");

        if (array.length > 0) {
            List<String> list = new ArrayList<String>();
            Collections.addAll(list, array);

            return list;
        }

        return null;
    }

    /**
     * 保存message
     *
     * @param message
     * @return 返回这条messaged在db中的id
     */
    public synchronized Integer saveMessage(InviteMessage message) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int id = -1;
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(InviteMessgeDao.COLUMN_NAME_FROM, message.getFrom());
            values.put(InviteMessgeDao.COLUMN_NAME_GROUP_ID, message.getGroupId());
            values.put(InviteMessgeDao.COLUMN_NAME_GROUP_Name, message.getGroupName());
            values.put(InviteMessgeDao.COLUMN_NAME_REASON, message.getReason());
            values.put(InviteMessgeDao.COLUMN_NAME_TIME, message.getTime());
            values.put(InviteMessgeDao.COLUMN_NAME_STATUS, message.getStatus().ordinal());
            db.insert(InviteMessgeDao.TABLE_NAME, null, values);

            Cursor cursor =
                    db.rawQuery("select last_insert_rowid() from " + InviteMessgeDao.TABLE_NAME, null);
            if (cursor.moveToFirst()) {
                id = cursor.getInt(0);
            }

            cursor.close();
        }
        return id;
    }

    /**
     * 更新message
     *
     * @param msgId
     * @param values
     */
    synchronized public void updateMessage(int msgId, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.update(InviteMessgeDao.TABLE_NAME, values, InviteMessgeDao.COLUMN_NAME_ID + " = ?",
                    new String[]{String.valueOf(msgId)});
        }
    }

    /**
     * 获取messges
     *
     * @return
     */
    synchronized public List<InviteMessage> getMessagesList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<InviteMessage> msgs = new ArrayList<InviteMessage>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + InviteMessgeDao.TABLE_NAME + " desc", null);
            while (cursor.moveToNext()) {
                InviteMessage msg = new InviteMessage();
                int id = cursor.getInt(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_ID));
                String from = cursor.getString(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_FROM));
                String groupid =
                        cursor.getString(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_GROUP_ID));
                String groupname =
                        cursor.getString(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_GROUP_Name));
                String reason = cursor.getString(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_REASON));
                long time = cursor.getLong(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_TIME));
                int status = cursor.getInt(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_STATUS));

                msg.setId(id);
                msg.setFrom(from);
                msg.setGroupId(groupid);
                msg.setGroupName(groupname);
                msg.setReason(reason);
                msg.setTime(time);
                if (status == InviteMesageStatus.BEINVITEED.ordinal()) {
                    msg.setStatus(InviteMesageStatus.BEINVITEED);
                } else if (status == InviteMesageStatus.BEAGREED.ordinal()) {
                    msg.setStatus(InviteMesageStatus.BEAGREED);
                } else if (status == InviteMesageStatus.BEREFUSED.ordinal()) {
                    msg.setStatus(InviteMesageStatus.BEREFUSED);
                } else if (status == InviteMesageStatus.AGREED.ordinal()) {
                    msg.setStatus(InviteMesageStatus.AGREED);
                } else if (status == InviteMesageStatus.REFUSED.ordinal()) {
                    msg.setStatus(InviteMesageStatus.REFUSED);
                } else if (status == InviteMesageStatus.BEAPPLYED.ordinal()) {
                    msg.setStatus(InviteMesageStatus.BEAPPLYED);
                }
                msgs.add(msg);
            }
            cursor.close();
        }
        return msgs;
    }

    /**
     * 删除要求消息
     *
     * @param from
     */
    synchronized public void deleteMessage(String from) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(InviteMessgeDao.TABLE_NAME, InviteMessgeDao.COLUMN_NAME_FROM + " = ?",
                    new String[]{from});
        }
    }

    synchronized int getUnreadNotifyCount() {
        int count = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select " + InviteMessgeDao.COLUMN_NAME_UNREAD_MSG_COUNT + " from " +
                    InviteMessgeDao.TABLE_NAME, null);
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        return count;
    }

    synchronized void setUnreadNotifyCount(int count) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(InviteMessgeDao.COLUMN_NAME_UNREAD_MSG_COUNT, count);

            db.update(InviteMessgeDao.TABLE_NAME, values, null, null);
        }
    }

    synchronized public void closeDB() {
        if (dbHelper != null) {
            dbHelper.closeDB();
        }
        dbMgr = null;
    }


    /**
     * Save Robot list
     */
    synchronized public void saveRobotList(List<RobotUser> robotList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(UserDao.ROBOT_TABLE_NAME, null, null);
            for (RobotUser item : robotList) {
                ContentValues values = new ContentValues();
                values.put(UserDao.ROBOT_COLUMN_NAME_ID, item.getUsername());
                if (item.getNick() != null) {
                    values.put(UserDao.ROBOT_COLUMN_NAME_NICK, item.getNick());
                }
                if (item.getAvatar() != null) {
                    values.put(UserDao.ROBOT_COLUMN_NAME_AVATAR, item.getAvatar());
                }
                db.replace(UserDao.ROBOT_TABLE_NAME, null, values);
            }
        }
    }

    /**
     * load robot list
     */
    synchronized public Map<String, RobotUser> getRobotList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Map<String, RobotUser> users = null;
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + UserDao.ROBOT_TABLE_NAME, null);
            if (cursor.getCount() > 0) {
                users = new HashMap<String, RobotUser>();
            }
            ;
            while (cursor.moveToNext()) {
                String username = cursor.getString(cursor.getColumnIndex(UserDao.ROBOT_COLUMN_NAME_ID));
                String nick = cursor.getString(cursor.getColumnIndex(UserDao.ROBOT_COLUMN_NAME_NICK));
                String avatar = cursor.getString(cursor.getColumnIndex(UserDao.ROBOT_COLUMN_NAME_AVATAR));
                RobotUser user = new RobotUser(username);
                user.setNick(nick);
                user.setAvatar(avatar);
                String headerName = null;
                if (!TextUtils.isEmpty(user.getNick())) {
                    headerName = user.getNick();
                } else {
                    headerName = user.getUsername();
                }
                if (Character.isDigit(headerName.charAt(0))) {
                    user.setInitialLetter("#");
                } else {
                    user.setInitialLetter(
                            HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target
                                    .substring(0, 1).toUpperCase());
                    char header = user.getInitialLetter().toLowerCase().charAt(0);
                    if (header < 'a' || header > 'z') {
                        user.setInitialLetter("#");
                    }
                }

                users.put(username, user);
            }
            cursor.close();
        }
        return users;
    }


}