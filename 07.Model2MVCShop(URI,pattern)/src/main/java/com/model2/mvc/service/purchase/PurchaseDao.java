package com.model2.mvc.service.purchase;

import java.util.List;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;

public interface PurchaseDao {
	
	//INSERT
	public void insertPurchase(Purchase purchase) throws Exception;
	
	//SELECT ONE
	public Purchase findPurchase(int tranNo) throws Exception;
	
	//SELECT ONE 2
	public Purchase findPurchase2(int prodNo) throws Exception;
	
	//SELECT LIST
	public List<Purchase> getPurchaseList(Search search, String buyerId ) throws Exception;
	
	//UPDATE1
	public void updatePurchase(Purchase purchase) throws Exception;
	
	//UPDATE2
	public void updateTranCode(Purchase purchase) throws Exception;
	
	//page Row
	public int getTotalCount(String buyerId) throws Exception;
}
