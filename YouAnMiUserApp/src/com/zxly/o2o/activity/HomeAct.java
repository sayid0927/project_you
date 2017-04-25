package com.zxly.o2o.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.RadioButton;

import com.easemob.chat.EMChatManager;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.HXHelper;
import com.easemob.chatuidemo.utils.EaseCallBack;
import com.easemob.chatuidemo.utils.PreferenceManager;
import com.easemob.easeui.ui.EaseHXMainFragment;
import com.easemob.util.EMLog;
import com.lidroid.xutils.exception.DbException;
import com.shyz.downloadutil.DownloadManager;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.dialog.VerifyDialog;
import com.zxly.o2o.fragment.DiscoveryFragment;
import com.zxly.o2o.fragment.HomeFragment;
import com.zxly.o2o.fragment.PersonalCenterFragment;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.IMGetShopContactsRequest;
import com.zxly.o2o.service.RunHeatbeatService;
import com.zxly.o2o.util.ApkInfoUtil;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.FragmentTabHandler;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.RunHeatbeatUtils;
import com.zxly.o2o.util.Util;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.FlowRadioGroup;
import com.zxly.o2o.view.RedPoint;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.zxly.o2o.config.Config.getH5CachePath;
import static com.zxly.o2o.config.Config.shopId;

public class HomeAct extends BasicAct {

    public FlowRadioGroup rgs;
    private DownloadManager downLoadManager;
    private List<Fragment> fragments = new ArrayList<Fragment>();

    private long mExitTime;
    public FragmentTabHandler fragmentContorler;
    private EaseHXMainFragment easeHXMainFragment;

    // 账号在别处登录
    public boolean isConflict = false;
    private boolean isConflictDialogShow = true;
    private boolean isAccountRemovedDialogShow = true;
    private boolean isCurrentAccountRemoved = false;  // 账号被移除
    private android.app.AlertDialog.Builder conflictBuilder;
    private android.app.AlertDialog.Builder accountRemovedBuilder;
    String homeUrl;
    RedPoint redPoint;
    private static HomeAct instance;

    public static HomeAct getIncetance(){
        return instance;
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("id", 1234567890);
    }

    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
       /* if (arg0 != null) {
            finish();
        }*/
        instance =this;
        if (getIntent().getBooleanExtra(HXConstant.ACCOUNT_CONFLICT, false)) {
            showConflictDialog();
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_tab_act);

        Bundle extra_bundle = getIntent().getBundleExtra("extra_bundle");
        if(extra_bundle!=null){
            String sys_msg_url = extra_bundle.getString("sys_msg_url");
            String title = extra_bundle.getString("文章详情");
            boolean isFromNotice = extra_bundle.getBoolean("isFromNotice");
            long id = extra_bundle.getLong("id");
            Intent intent = new Intent(HomeAct.this, H5DetailAct.class);
            intent.putExtra("sys_msg_url", sys_msg_url);
            //需要统计用户是否有点击从通知栏的消息，标记用
            intent.putExtra("isFromNotice",isFromNotice);
            intent.putExtra("id",id);
            intent.putExtra("title", "文章详情");
            GetuiUserH5DetailAct.start(this,sys_msg_url,title,id);
        }

        String userId=Account.user==null ? "" : "&userId="+Account.user.getId();
        if ((PreferUtil.getInstance().getH5VersionCode() > ApkInfoUtil.getDefaultH5Version(this) ||
                PreferUtil.getInstance().getH5StyleId() != ApkInfoUtil.getDefaultH5StyleId(this)) &&
                new File(Constants.H5_PROJECT_PATH).exists()) {

                    homeUrl = "file://" + Constants.H5_PROJECT_PATH + "/index.html?shopId=" + shopId +
                    "&baseUrl=" + DataUtil.encodeBase64(Config.dataBaseUrl)
                    + "&brandName=" + Build.BRAND + "&DeviceID=" + Config.imei + "&DeviceType=1";

        } else {

//            copyH5FileToTemp();
            copyFolderFromAssets(HomeAct.this, "dist", Config.getH5CachePath(this) + "/");

            homeUrl = "file://" + Config.getH5CachePath(this) + "/index.html?shopId=" + shopId +
                    "&baseUrl=" + DataUtil.encodeBase64(Config.dataBaseUrl)
                    + "&brandName=" + Build.BRAND + "&DeviceID=" + Config.imei + "&DeviceType=1";


//                    homeUrl="file:///android_asset/multiTemp/index.html?shopId="+ Config.shopId+"&baseUrl="+DataUtil.encodeBase64(Config.dataBaseUrl)
//                    +"&brandName="+ Build.BRAND+"&DeviceID="+Config.imei+"&DeviceType=1";
             // http://192.168.1.81/app-multi-temp/index.html? shopId=100 &baseUrl=aHR0cDovL3Vpcy55b3Vhbm1pLm5ldA==
             // &brandName=Xiaomi&DeviceID=867451021080988&DeviceType=1&userId=27570&appVersion=2.8
             //  homeUrl ="http://192.168.1.139:8089/app-multi-temp/index.html?shopId=100&baseUrl=aHR0cDovL3Vpcy55b3Vhbm1pLm5ldA==&brandName=Xiaomi&DeviceID=867451021080988&DeviceType=1&userId=&Authorization=null";

     }
        if (Account.user != null) {
//            homeUrl = homeUrl + "&userId=" + Account.user.getId();
            homeUrl = homeUrl + "&userId=" + Account.user.getId()+"&appVersion="+Util.getVersion(this);
        } else {
            homeUrl = homeUrl + "&userId="+"&appVersion="+Util.getVersion(this);
        }

//        String token = PreferUtil.getInstance().getLoginToken();
//        if (!StringUtil.isNull(token)) {
//            homeUrl = homeUrl + "&Authorization=" + token;
//        } else {
//            homeUrl = homeUrl + "&Authorization=";
//        }

        Log.d("downLoad", "homeUrl-->" + homeUrl);

        redPoint = (RedPoint) findViewById(R.id.view_redPoint);
        fragments.add(HomeFragment.newInstance(homeUrl));
//     fragments.add(new CircleMainPageFragment());
        fragments.add(new DiscoveryFragment());
        fragments.add(initHXMainFragment());
        fragments.add(new PersonalCenterFragment());
        rgs = (FlowRadioGroup) findViewById(R.id.main_act_radio_group);
        fragmentContorler = new FragmentTabHandler(this, fragments, R.id.main_act_tabcontent);
        rgs.setOnCheckedChangeListener(new FlowRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(FlowRadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.btn_homePage:
                        fragmentContorler.showTab(0);
                        break;

//                    case R.id.btn_circlePage:
//                        fragmentContorler.showTab(1);
//                        break;

                    case R.id.btn_discoveryPage:
                        fragmentContorler.showTab(1);
                        break;

                    //消息界面
                    case R.id.btn_messagePage:
                        easeHXMainFragment.refreshContainFragment();  //刷新fragment数据
                        fragmentContorler.showTab(2);
                        if (Account.user != null && (HXHelper.getInstance().getYAMContactList().size() ==
                                0 || System.currentTimeMillis()
                                - PreferenceManager.getInstance().getMistiming() >
                                (1000 * 60 * 5))) {
                            Log.e("reload contact", "reload contact...");
                            PreferenceManager.getInstance().setMistiming(System.currentTimeMillis());
                            AppController.getInstance().checkIsNeedUpdateContact(false);
                        }
                        break;

                    case R.id.btn_persionnalPage:
                        fragmentContorler.showTab(3);
                        break;

                    default:
                        break;

                }
            }
        });


        ((RadioButton) rgs.findViewById(R.id.btn_homePage)).setChecked(true);
        downLoadManager = DownloadManager.createDownloadManager(this);
        if(PreferUtil.getInstance().getDeviceAdminActive()==0)
        {
            VerifyDialog verDialog = new VerifyDialog();
            verDialog.cancelable(false);
            ViewUtils.setGone(verDialog.getCancleButton());
            verDialog.show(new CallBack() {
                @Override
                public void onCall() {
                    ViewUtils.startActivityDeviceManager(HomeAct.this);
                }
            }, getResources().getString(R.string.device_manager_activate_msg));

            PreferUtil.getInstance().setDeviceAdminActive(1);
        }

    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra(HXConstant.ACCOUNT_CONFLICT, false)) {
            showConflictDialog();
        } else if (intent.getBooleanExtra(HXConstant.ACCOUNT_REMOVED,
                false) && !isAccountRemovedDialogShow) {
            //            showAccountRemovedDialog();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isConflict && !isCurrentAccountRemoved) {
            EMChatManager.getInstance().activityResumed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                ViewUtils.showToast("再按一次退出程序");
                mExitTime = System.currentTimeMillis();
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        RunHeatbeatUtils.stopPollingService(this, RunHeatbeatService.class);
        try {
            downLoadManager.stopAllDownload();
        } catch (DbException e) {
            e.printStackTrace();
        }


        if (conflictBuilder != null) {
            conflictBuilder.create().dismiss();
            conflictBuilder = null;
        }

        HXHelper.getInstance().cleanReceivedListeners();

    }

    /**
     * 显示帐号在别处登录dialog
     */
    private void showConflictDialog() {
        isConflictDialogShow = true;
        HXHelper.getInstance().logout(true, null);
        String st = getResources().getString(com.easemob.chatuidemo.R.string.Logoff_notification);
        if (!HomeAct.this.isFinishing()) {
            // clear up global variables
            try {
                if (conflictBuilder == null) {
                    conflictBuilder = new android.app.AlertDialog.Builder(HomeAct.this);
                }
                conflictBuilder.setTitle(st);
                conflictBuilder.setMessage(com.easemob.chatuidemo.R.string.connect_conflict);
                conflictBuilder
                        .setPositiveButton(com.easemob.chatuidemo.R.string.ok,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        AppController.getInstance().deleteFile("user");
                                        Account.user = null;
                                        PreferUtil.getInstance().setLoginToken("");
                                        conflictBuilder = null;
                                        AppController.getInstance().exitAndGoToMainPage();
                                        LoginAct.start(AppController.getInstance().getTopAct());
                                    }
                                });
                conflictBuilder.setCancelable(false);
                conflictBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                EMLog.e("", "---------color conflictBuilder error" + e.getMessage());
            }
        }
    }


    /**
     * 把H5项目拷贝到应用程序缓存目录
     */
    private void copyH5FileToTemp() {
        try {
            String[] fileList = getAssets().list("dist");
            for (int i = 0; i < fileList.length; i++) {
                copyFile("dist/" + fileList[i], getH5CachePath(this) + "/" + fileList[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从assets目录下拷贝整个文件夹，不管是文件夹还是文件都能拷贝
     *
     * @param context
     *            上下文
     * @param rootDirFullPath
     *            文件目录，要拷贝的目录如assets目录下有一个SBClock文件夹：SBClock
     * @param targetDirFullPath
     *            目标文件夹位置如：/sdcrad/SBClock
     */
    public void copyFolderFromAssets(Context context, String rootDirFullPath, String targetDirFullPath) {
        try {
            String[] listFiles = context.getAssets().list(rootDirFullPath);// 遍历该目录下的文件和文件夹
            for (String string : listFiles) {   // 看起子目录是文件还是文件夹，这里只好用.做区分了
                if (isFileByName(string)) {   // 文件
//                    copyFileFromAssets(context, rootDirFullPath + "/" + string, targetDirFullPath + "/" + string);
                    copyFile(rootDirFullPath + "/" + string, targetDirFullPath + "/" + string);
                } else {// 文件夹
                    String childRootDirFullPath = rootDirFullPath + "/" + string;
                    String childTargetDirFullPath = targetDirFullPath + "/" + string;
                    new File(childTargetDirFullPath).mkdirs();
                    copyFolderFromAssets(context, childRootDirFullPath, childTargetDirFullPath);
                }
            }
        } catch (IOException e) {
            Log.e("copy", "copyFolderFromAssets " + "IOException-" + e.getMessage());
            Log.e("copy", "copyFolderFromAssets " + "IOException-" + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    private static boolean isFileByName(String string) {
        if (string.contains(".") && !"js.lib".equals(string)) {
            return true;
        }
        return false;
    }

    private boolean copyFile(String sourceFileName, String destFileName) {
        AssetManager assetManager = getAssets();

        File destFile = new File(destFileName);

        File destParentDir = destFile.getParentFile();
        destParentDir.mkdirs();

        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(sourceFileName);
            out = new FileOutputStream(destFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private Fragment initHXMainFragment() {
        easeHXMainFragment = new EaseHXMainFragment();
        easeHXMainFragment.setCallBack(new EaseCallBack() {
            @Override
            public void onCall() {
                //set system time
                PreferenceManager.getInstance().setMistiming(System.currentTimeMillis());
                IMGetShopContactsRequest imGetShopContactsRequest = new IMGetShopContactsRequest(false);
                imGetShopContactsRequest.setOnResponseStateListener(
                        new BaseRequest.ResponseStateListener() {
                            @Override
                            public void onOK() {
                                mMainHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        easeHXMainFragment.getContactListFragment().contactListLayout
                                                .refresh(null);
                                        easeHXMainFragment.getConversationListFragment().refresh();
                                    }
                                }, 1000);
                            }

                            @Override
                            public void onFail(int code) {
                            }
                        });
                imGetShopContactsRequest.start();
            }
        });
        easeHXMainFragment.setNewMsgCallBack(new EaseCallBack() {
            @Override
            public void onCall() {
                mMainHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        redPoint.showRedPoint();
                    }
                },200);

            }
        });
        return easeHXMainFragment;
    }

}
