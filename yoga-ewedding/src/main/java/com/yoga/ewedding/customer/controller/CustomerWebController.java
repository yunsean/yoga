package com.yoga.ewedding.customer.controller;

import com.yoga.core.controller.BaseWebController;
import com.yoga.ewedding.counselor.service.SettingService;
import com.yoga.ewedding.customer.service.CustomerService;
import com.yoga.tenant.setting.Settable;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("eweddingCustomerWebController")
@EnableAutoConfiguration
@RequestMapping("/ewedding/customer")
@Settable(module = CustomerService.Module_Name, key = CustomerService.DepartmentId_Key, type = SettingService.class, name = "客户所属部门")
public class CustomerWebController extends BaseWebController {

}