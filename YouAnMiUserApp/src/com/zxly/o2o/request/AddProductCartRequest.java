package com.zxly.o2o.request;
import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.ProductInfoAct;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.NewProduct;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dsnx on 2015/5/25.
 */
public class AddProductCartRequest extends BaseRequest implements BaseRequest.ResponseStateListener{

    private int count;
    private CallBack succeedCallBack;

    public AddProductCartRequest(NewProduct product,CallBack succeedCallBack)
    {
        this.succeedCallBack=succeedCallBack;
        addParams("userId", Account.user.getId());
        addParams("productId",product.getId());
        addParams("shopId", Config.shopId);
        addParams("pcs",product.getPcs());
        addParams("skuId",product.getSkuId());
        setOnResponseStateListener(this);
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject jsonObject=new JSONObject(data);
            count=jsonObject.getInt("count");
        } catch (JSONException e) {
            throw JSONException(e);
        }
    }

    @Override
    protected String method() {
        return "order/shoppingCart/add";

    }
    

    @Override
	protected boolean isShowLoadingDialog() {
		
		return true;
	}

	@Override
    public void onOK() {
        Account.orderCount=count;
        ProductInfoAct.refreshCartCount(1);
        ViewUtils.showToast("添加成功，在购物车等亲!");
        if(succeedCallBack!=null)
        {
            succeedCallBack.onCall();
        }
    }

    @Override
    public void onFail(int code) {
    	ViewUtils.showToast("添加失败");
    }
}
