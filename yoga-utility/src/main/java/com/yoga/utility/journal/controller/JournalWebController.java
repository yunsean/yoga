package com.yoga.utility.journal.controller;

import com.yoga.core.controller.BaseWebController;
import com.yoga.core.data.PageList;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.user.basic.TenantPage;
import com.yoga.utility.journal.dto.ListDto;
import com.yoga.utility.journal.model.Journal;
import com.yoga.utility.journal.service.JournalService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@EnableAutoConfiguration
@RequestMapping("/system/journal")
public class JournalWebController extends BaseWebController {

    @Autowired
    private JournalService journalService;

    @RequiresAuthentication
    @RequestMapping("/list")
    public String list(ModelMap model, TenantPage page, @Valid ListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageList<Journal> journals = journalService.find(dto.getTid(), dto.getBeginTime(), dto.getEndTime(), dto.getUser(), dto.getModule(), dto.getAction(), dto.getMethod(), page.getPageIndex(), page.getPageSize());
        model.put("param", dto.wrapAsMap());
        model.put("page", journals.getPage());
        model.put("journals", journals);
        return "/system/journal/journal";
    }
}