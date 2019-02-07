package com.mwj.cms.system.controller;


import com.mwj.cms.framework.web.controller.BaseController;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Meng Wei Jin
 */
@Api(description = "Job执行Cron表达式接口")
@Controller
@RequestMapping("/sys/jobCron")
public class JobCronController extends BaseController {

}
