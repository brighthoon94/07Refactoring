package com.model2.mvc.web.purchase;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;

@Controller
public class PurchaseController {

	///Field
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	public PurchaseController() {
		System.out.println(this.getClass());
	}
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;

	@Value("#{commonProperties['pageUnit']}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	int pageSize;
	@RequestMapping("/addPurchaseView.do")
	public ModelAndView addPurchaseView ( HttpSession session, @RequestParam("prod_no") int prodNo)throws Exception {
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/purchase/addPurchaseView.jsp");
		Product product = productService.getProduct(prodNo);
		modelAndView.addObject("product", product);
		modelAndView.addObject("user", session.getAttribute("user"));
		return modelAndView;
	}
	
	@RequestMapping("/addPurchase.do")
	public ModelAndView addPurchase(@ModelAttribute("purchase") Purchase purchase ) throws Exception {
		
		ModelAndView modelAndView = new ModelAndView();
		System.out.println("/addPurchase.do");
		purchaseService.addPurchase(purchase);
		modelAndView.setViewName("/purchase/addPurchase.jsp");
		modelAndView.addObject("purchase", purchase);
		
		return modelAndView;
	}
	
	@RequestMapping("/getPurchase.do")
	public ModelAndView getPurchase(@RequestParam("tranNo") int tranNo) throws Exception{
		
		System.out.println (getClass().getName()+".do");
		Purchase purchase = purchaseService.getPurchase(tranNo);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/purchase/getPurchase.jsp");
		modelAndView.addObject(purchase);
		return modelAndView;
	}
	
	@RequestMapping("/updatePurchaseView.do")
	public ModelAndView updatePurchaseView( @RequestParam("tranNo") int tranNo) throws Exception{
		
		System.out.println (getClass().getName()+".do");
		
		Purchase purchase = purchaseService.getPurchase(tranNo);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/purchase/updatePurchaseView.jsp");
		modelAndView.addObject(purchase);
		return modelAndView;
	}
	
	@RequestMapping("/updatePurchase.do")
	public ModelAndView updatePurchase( @RequestParam("tranNo") int tranNo, @ModelAttribute("purchase") Purchase purchase) throws Exception{
		System.out.println (getClass().getName()+".do");
		
		purchaseService.updatePurchase(purchase);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("forward:/getPurchase.do");
		modelAndView.addObject(purchase);
		return modelAndView;
	}
	
	@RequestMapping("/updateTranCode.do")
	public ModelAndView updateTranCode( @RequestParam("prodNo") int prodNo,
																				@RequestParam("tranCode") String tranCode) throws Exception{
		
		System.out.println (getClass().getName()+".do"+prodNo+tranCode);
		
		Purchase purchase = purchaseService.getPurchase2(prodNo);
		purchase.setTranCode(tranCode);
		purchaseService.updateTranCode(purchase);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("forward:/listPurchase.do");
		purchase = purchaseService.getPurchase2(prodNo);
		modelAndView.addObject(purchase);
		return modelAndView;
	}
	
	@RequestMapping("/updateTranCodeByProd.do")
	public ModelAndView updateTranCodeByProd( @RequestParam("prodNo") int prodNo,
																				@RequestParam("tranCode") String tranCode) throws Exception{
		
		System.out.println (getClass().getName()+".do"+prodNo+"tranCode"+tranCode);
		
		Purchase purchase = purchaseService.getPurchase2(prodNo);
		purchase.setTranCode(tranCode);
		purchaseService.updateTranCode(purchase);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("redirect:/listProduct.do?menu=manage");
		return modelAndView;
	}
	
	@RequestMapping("/listPurchase.do")
	public ModelAndView listPurchase( HttpSession session, @ModelAttribute("search") Search search , Model model , HttpServletRequest request) throws Exception{
		
		ModelAndView modelAndView = new ModelAndView();
		System.out.println("/listPurchase.do");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// Business logic 수행
		User user = (User)session.getAttribute("user");
		String buyerId = user.getUserId();
		System.out.println(buyerId);
		Map<String , Object> map=purchaseService.getPurchaseList(search, buyerId);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model 과 View 연결
		modelAndView.setViewName("/purchase/listPurchase.jsp");
		modelAndView.addObject("list", map.get("list"));
		modelAndView.addObject("resultPage", resultPage);
		modelAndView.addObject("search", search);
		
		return modelAndView;
	}
}
