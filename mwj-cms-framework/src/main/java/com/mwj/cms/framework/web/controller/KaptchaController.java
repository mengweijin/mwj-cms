package com.mwj.cms.framework.web.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.mwj.cms.common.exception.KaptchaException;
import com.mwj.cms.framework.util.MessageSourceUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author Meng Wei Jin
 * @description
 **/
@Api(description = "验证码接口")
@Controller
@RequestMapping("/kaptcha")
public class KaptchaController extends BaseController {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @ApiOperation(value = "生成验证码图片")
    @GetMapping("renderCode")
    public void getCaptchaCode(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();

        response.setDateHeader(HttpHeaders.EXPIRES, 0L);
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-store, no-cache, must-revalidate");
        response.addHeader(HttpHeaders.CACHE_CONTROL, "post-check=0, pre-check=0");
        response.setHeader(HttpHeaders.PRAGMA, "no-cache");
        response.setContentType("image/jpeg");

        // 生成验证码文本
        String capText = defaultKaptcha.createText();
        try(ServletOutputStream out = response.getOutputStream()){
            session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
            session.setAttribute(Constants.KAPTCHA_SESSION_DATE, System.currentTimeMillis());
            // 利用生成的字符串构建图片
            BufferedImage bufferedImage = defaultKaptcha.createImage(capText);
            ImageIO.write(bufferedImage, "jpg", out);
            out.flush();
        } catch (IOException e){
            logger.error(e.getMessage(), e);
            throw new KaptchaException(MessageSourceUtils.message("captcha.render.failed"));
        }
    }



}
