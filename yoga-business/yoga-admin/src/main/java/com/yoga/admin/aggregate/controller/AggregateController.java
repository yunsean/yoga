package com.yoga.admin.aggregate.controller;

import com.yoga.core.base.BaseController;
import com.yoga.core.base.BaseDto;
import com.yoga.core.data.ApiResults;
import com.yoga.admin.aggregate.ao.Todo;
import com.yoga.admin.aggregate.service.AggregateService;
import com.yoga.operator.user.model.User;
import com.yoga.setting.annotation.Settable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "信息聚合")
@Settable
@Controller
@EnableAutoConfiguration
@RequestMapping("/admin/office/aggregate")
public class AggregateController extends BaseController {

	@Autowired
	private AggregateService aggregateService;

	@ResponseBody
	@ApiOperation("获取TODO列表")
	@RequiresAuthentication
	@GetMapping("/todos.json")
	public ApiResults<Todo> todos(BaseDto dto) {
		User user = User.getLoginUser();
		List<Todo> todos = aggregateService.getTodos(dto.getTid(), user.getId());
		return new ApiResults<>(todos);
	}
}
