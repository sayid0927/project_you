package com.zxly.o2o.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.zxly.o2o.activity.PersonalSystemMsgDetailAct;
import com.zxly.o2o.model.SystemMessage;
import com.zxly.o2o.o2o_user.R;

/**
 * @author fengrongjian
 * 
 */
public class NotifyUtil {

	public static void notify(Context context, SystemMessage sysMsg) throws Exception {
		int defaultType = 0;
		defaultType |= Notification.DEFAULT_LIGHTS;
		defaultType |= Notification.DEFAULT_SOUND;
		defaultType |= Notification.DEFAULT_VIBRATE;
		Intent i = new Intent(context, PersonalSystemMsgDetailAct.class);
		i.putExtra("msg", sysMsg);
		i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 100,
				i, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				context);
		builder.setDefaults(defaultType);
		builder.setContentTitle(sysMsg.getTitle());
		builder.setContentText(sysMsg.getContent());
		builder.setSmallIcon(R.drawable.logo);
		builder.setTicker("收到一条新消息");
		builder.setStyle(new NotificationCompat.BigTextStyle()
				.setBigContentTitle(sysMsg.getTitle())
				.bigText(sysMsg.getContent()));
		builder.setContentIntent(pendingIntent);
		Notification notification = builder.build();
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		NotificationManager manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(1, notification);
	}
	
	@SuppressWarnings("deprecation")
	public static void showNotification(Context context,
			SystemMessage sysMsg) {
		Notification n = new Notification();
		n.icon = R.drawable.logo;
		n.tickerText = "收到一条新消息";
		n.when = System.currentTimeMillis();

		n.flags |= Notification.FLAG_SHOW_LIGHTS;
		n.flags |= Notification.FLAG_AUTO_CANCEL;
		n.defaults = Notification.DEFAULT_SOUND;

		Intent i = new Intent(context, PersonalSystemMsgDetailAct.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		i.putExtra("msg", sysMsg);
		int id = (int)sysMsg.getId();
		PendingIntent pendingIntent = PendingIntent.getActivity(context, id,
				i, PendingIntent.FLAG_UPDATE_CURRENT);
		n.setLatestEventInfo(context, sysMsg.getTypeName(), sysMsg.getTitle(), pendingIntent);
//		n.setLatestEventInfo(context, sysMsg.getTitle(), sysMsg.getContent(), pendingIntent);
		NotificationManager manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(id, n);
	}

}
