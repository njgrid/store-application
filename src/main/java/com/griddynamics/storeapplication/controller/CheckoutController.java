package com.griddynamics.storeapplication.controller;

import com.griddynamics.storeapplication.dto.response.CheckoutResponse;
import com.griddynamics.storeapplication.service.CheckoutService;
import com.griddynamics.storeapplication.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(CommonUtils.BASE_API)
public class CheckoutController {

  private static final String HEADER_SESSION_ID = "X-Session-Id";
  private static final String API_CHECKOUT      = "/checkout";


  @Autowired
  private CheckoutService checkoutService;

  @PostMapping(API_CHECKOUT)
  public CheckoutResponse checkout(@RequestHeader(HEADER_SESSION_ID) final String sessionId) {
    return checkoutService.checkout(sessionId);
  }

}

