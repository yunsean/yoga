package com.yoga.utility.journal.controller;

import com.yoga.core.annotation.Explain;
import com.yoga.core.controller.BaseApiController;
import com.yoga.core.data.CommonResult;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.utility.journal.dto.DetailDto;
import com.yoga.utility.journal.model.Journal;
import com.yoga.utility.journal.service.JournalService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Explain(exclude = true)
@RestController
@EnableAutoConfiguration
@RequestMapping("/api/system/journal")
public class JournalApiController extends BaseApiController {

    @Autowired
    private JournalService journalService;

    @RequiresAuthentication
    @RequestMapping("/detail")
    public CommonResult get(@Valid DetailDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Journal journal = journalService.get(dto.getTid(), dto.getId());
        return new CommonResult(journal);
    }
}
