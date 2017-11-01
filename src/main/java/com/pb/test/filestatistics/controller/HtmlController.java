package com.pb.test.filestatistics.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for html pages
 */
@Controller
public class HtmlController {

  /**
   * Return main page
   * @return main page
   */
  @RequestMapping("/")
  public String getMainPage(){
    return "index";
  }
}
