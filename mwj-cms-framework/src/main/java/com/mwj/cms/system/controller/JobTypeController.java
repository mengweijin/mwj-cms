package com.mwj.cms.system.controller;


import com.mwj.cms.framework.web.controller.BaseController;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Meng Wei Jin
 */
@Api(description = "Job类型管理接口")
@Controller
@RequestMapping("/sys/job-type")
public class JobTypeController extends BaseController {

}
