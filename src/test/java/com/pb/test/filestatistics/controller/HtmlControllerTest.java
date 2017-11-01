package com.pb.test.filestatistics.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@WebMvcTest(HtmlController.class)
public class HtmlControllerTest {

  @Autowired
  protected WebApplicationContext wac;
  @Autowired
  private MockMvc mockMvc;

  @Test
  public void getMainPage_rootUrl_returnIndex()
      throws Exception {
    //Arrange
    final String TESTED_URL = "/";

    //Act && Assert
    mockMvc.perform(get(TESTED_URL)
        .contentType(MediaType.TEXT_HTML))
        .andExpect(status().isOk())
        .andExpect(view().name("index"));
  }

  @Test
  public void getMainPage_fakeUrl_returnNotFoundStatus()
      throws Exception {
    //Arrange
    final String TESTED_URL = "/fake";
    final int EXPECTED_HTTP_STATUS = 404;

    //Act && Assert
    mockMvc.perform(get(TESTED_URL)
        .contentType(MediaType.TEXT_HTML))
        .andExpect(status().is(EXPECTED_HTTP_STATUS));
  }
}
