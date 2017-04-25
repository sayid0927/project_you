package com.easemob.easeui.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.easemob.chat.EMMessage;
import com.easemob.chatuidemo.HXApplication;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.HXHelper;
import com.easemob.chatuidemo.R;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.controller.EaseUI.EaseUserProfileProvider;
import com.easemob.easeui.domain.EaseUser;
import com.easemob.easeui.domain.EaseYAMUser;
import com.easemob.exceptions.EaseMobException;
import com.squareup.picasso.Picasso;

public class EaseUserUtils {

    static EaseUserProfileProvider userProvider;

    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }

    /**
     * 根据username获取相应user
     *
     * @param username
     * @return
     */
    public static EaseYAMUser getUserInfo(String username) {
        if (userProvider != null) {
            return userProvider.getUser(username);
        }

        return null;
    }

    /**
     * 设置用户头像
     *
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView) {
        EaseYAMUser user = getUserInfo(username);
        if (user != null && user.getFirendsUserInfo().getAvatar() != null) {
            try {
                int avatarResId = Integer.parseInt(user.getFirendsUserInfo().getAvatar());
                Picasso.with(context).load(avatarResId).into(imageView);
            } catch (Exception e) {
                //正常的string路径
                Picasso.with(context).load(user.getFirendsUserInfo().getAvatar())
                        .placeholder(R.drawable.ease_default_avatar).into(imageView);
            }
        } else {
            Picasso.with(context).load(R.drawable.ease_default_avatar).into(imageView);
        }
    }

    /**
     * 设置用户昵称
     */
    public static void setUserNick(String username, TextView textView) {
        if (textView != null) {
            EaseYAMUser user = HXHelper.getInstance().getUserInfo(username);
            //        	EaseYAMUser user = getUserInfo(username);
            if (user != null && user.getFirendsUserInfo().getNickname() != null) {
                textView.setText(user.getFirendsUserInfo().getNickname());
            } else if (user != null && user.getFirendsUserInfo().getUserName() != null) {
                textView.setText(user.getFirendsUserInfo().getUserName());
            } else {
                textView.setText("陌生人");
            }
        }
    }

    /**
     * 获取用户昵称
     */
    public static String getUserNick(String username) {
        EaseYAMUser user = HXHelper.getInstance().getUserInfo(username);
        String nickName;
        if (user != null && user.getFirendsUserInfo().getNickname() != null) {
            nickName = user.getFirendsUserInfo().getNickname();
        } else if (user != null && user.getFirendsUserInfo().getUserName() != null) {
            nickName = user.getFirendsUserInfo().getUserName();
        } else {
            nickName = "陌生人";
        }
        return nickName;
    }

    /**
     * 设置用户昵称和头像 by huangbin
     */
    public static void setUserNickAndAvatar(EMMessage message, TextView textView, NetworkImageView avatar) {

        EaseYAMUser user = HXHelper.getInstance().getUserInfo(message.getUserName());
        if (user != null && user.getFirendsUserInfo().getNickname() != null) {
            EaseConstant.setImage(avatar, user.getFirendsUserInfo().getThumHeadUrl(),
                    R.drawable.ease_default_avatar,
                    null);
            if (textView != null) {
                textView.setText(user.getFirendsUserInfo().getNickname());
            }
        } else if (user != null && user.getFirendsUserInfo().getUserName() != null) {
            EaseConstant.setImage(avatar, user.getFirendsUserInfo().getThumHeadUrl(),
                    R.drawable.ease_default_avatar,
                    null);
            if (textView != null) {
                textView.setText(user.getFirendsUserInfo().getUserName());
            }
        } else {
            if (textView != null) {
                EaseConstant.setImage(avatar, "", R.drawable.ease_default_avatar, null);

                String userName = "陌生人";
                try {
                    userName = message.getStringAttribute("nickname");
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
                textView.setText(userName);
            }
        }
    }


    public static void setUserNickAndAvatar2(EMMessage message, TextView textView, NetworkImageView avatar) {

        EaseYAMUser user = HXHelper.getInstance().getUserInfo(message.getFrom());
        if (user != null && user.getFirendsUserInfo().getNickname() != null) {
            EaseConstant.setImage(avatar, user.getFirendsUserInfo().getThumHeadUrl(),
                    R.drawable.ease_default_avatar,
                    null);
            if (textView != null) {
                textView.setText(user.getFirendsUserInfo().getNickname());
            }
        } else if (user != null && user.getFirendsUserInfo().getUserName() != null) {
            EaseConstant.setImage(avatar, user.getFirendsUserInfo().getThumHeadUrl(),
                    R.drawable.ease_default_avatar,
                    null);
            if (textView != null) {
                textView.setText(user.getFirendsUserInfo().getUserName());
            }
        } else {
            if (textView != null) {
                EaseConstant.setImage(avatar, "", R.drawable.ease_default_avatar, null);

                String userName = "陌生人";
                try {
                    userName = message.getStringAttribute("nickname");
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
                textView.setText(userName);
            }
        }
    }

    public static void setUserNickAndAvatar3(EMMessage message, TextView textView, NetworkImageView avatar,
                                             TextView nameLeft) {

        EaseYAMUser user = HXHelper.getInstance().getUserInfo(message.getUserName());
        if (user != null && user.getFirendsUserInfo().getNickname() != null) {
            EaseConstant.setImage(avatar, user.getFirendsUserInfo().getThumHeadUrl(),
                    R.drawable.ease_default_avatar, null);

            if (textView != null) {
                textView.setText(user.getFirendsUserInfo().getNickname());
            }

            nameLeft.setText(user.getFirendsUserInfo().getGroupName());
        } else if (user != null && user.getFirendsUserInfo().getUserName() != null) {
            EaseConstant.setImage(avatar, user.getFirendsUserInfo().getThumHeadUrl(),
                    R.drawable.ease_default_avatar,
                    null);
            if (textView != null) {
                textView.setText(user.getFirendsUserInfo().getUserName());
            }
            nameLeft.setText(user.getFirendsUserInfo().getGroupName());
        } else {
            if (textView != null) {
                String userName = "陌生人";
                try {
                    userName = message.getStringAttribute("nickname");
                    textView.setText(userName);
                      String headUrl = message.getStringAttribute("headImageUrl");
                    EaseConstant.setImage(avatar, headUrl, R.drawable.ease_default_avatar, null);
                } catch (EaseMobException e) {
                    e.printStackTrace();
                    EaseConstant.setImage(avatar, "", R.drawable.ease_default_avatar, null);

                }

            }

        }

    }


}
