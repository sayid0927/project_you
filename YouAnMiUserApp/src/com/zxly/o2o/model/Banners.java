package com.zxly.o2o.model;

public class Banners {

	public static final int BANNER_DEFAULT=3;

	public static final int BANNER_PRODUCT=1;

	public static final int BANNER_H5=2;

	public static final int BANNER_TURN_TABLE=4;

	public static final int TYPE_GAME=5;

	public static final int TYPE_EXTERNAL_LINK=6;//外部链接
	public static final int TYPE_ARTICLE_AD=7;//文章广告
	public static final int TYPE_DDYH=8;//到店优惠

	private int id;  //BANNER ID
	private String title; // 标题
	private String imageUrl;
	private Long productId;
	/**  type 1:  2:  3:默认广告**/
    private int type;
	private String url;
	private String desc="";

	private int bannerId; //bannerId
	private Object data;
	private String shareUrl;

	public Banners() {

	}
	
	

	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getUrl() {
		return url;
	}



	public void setUrl(String url) {
		this.url = url;
	}



	public int getType() {
		return type;
	}



	public void setType(int type) {
		this.type = type;
	}



	public Object getData() {
		return data;
	}


	public void setData(Object data) {
		this.data = data;
	}


	public Banners(int bannerId, String title) {
		super();
		this.bannerId = bannerId;
		this.title = title;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	

	public int getBannerId() {
		return bannerId;
	}

	public void setBannerId(int bannerId) {
		this.bannerId = bannerId;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	@Override
	public boolean equals(Object o) {
		if(((Banners)o).productId==productId)
		{
			return true;
		}
		return false;
	}


	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
