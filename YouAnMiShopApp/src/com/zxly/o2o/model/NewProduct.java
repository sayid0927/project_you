package com.zxly.o2o.model;

/**
 * @author     dsnx
 * @version    YIBA-O2O 2014-12-29
 * @since      YIBA-O2O
 */
public class NewProduct extends BaseProduct implements Cloneable 
{

	@Override
	public boolean equals(Object o) {
		if (o instanceof NewProduct) {
			if (((NewProduct) o).id == this.id) {
				return true;
			}
		}
		return false;
	}

	public NewProduct clone(){ 
		 NewProduct o = null; 
		try{ 
		o = ( NewProduct)super.clone(); 
		}catch(CloneNotSupportedException e){ 
		e.printStackTrace(); 
		} 
		return o; 
		} 
    
	
}
