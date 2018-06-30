package com.yoga.content.property.controller;

import com.yoga.content.property.dto.ListDto;
import com.yoga.content.property.model.Property;
import com.yoga.content.property.service.PropertyService;
import com.yoga.core.controller.BaseWebController;
import com.yoga.core.utils.StrUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cms")
public class PropertyWebController extends BaseWebController {
    @Autowired
    private PropertyService propertyService = null;

    @RequiresAuthentication
    @RequestMapping("/property")
    public String property(HttpServletRequest request, ListDto dto, ModelMap model) {
        Map<String, Property> allOptionens = propertyService.propertyMap(dto.getTid());
        List<Property> values = new ArrayList<>();
        if (StrUtil.isBlank(dto.getCode()) && allOptionens.size() > 0) {
            dto.setCode((String)allOptionens.keySet().toArray()[0]);
        }
        if (StrUtil.isBlank(dto.getCode())) dto.setCode("");
        if (allOptionens.get(dto.getCode()) != null) {
            values.add(allOptionens.get(dto.getCode()));
        }
        model.put("properies", allOptionens.values());
        model.put("selected", dto.getCode());
        model.put("values", values);
        return "/property/property";
    }

}
