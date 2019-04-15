package io.rainfall.store.controllers;


import io.rainfall.store.RainfallStoreApp;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ContextConfiguration(classes = RainfallStoreApp.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SuppressWarnings("unused")
abstract class ControllerIT {

  static final String DEFAULT_TEXT_HTML = "text/html;charset=UTF-8";

  @Autowired
  private WebApplicationContext context;

  MockMvc mvc;

  @Before
  public void setup() {
    mvc = webAppContextSetup(context).build();
  }

  @SuppressWarnings("unchecked")
  Matcher<String> containsAll(String... patterns) {
    Matcher[] matchers = Stream.of(patterns)
            .map(Matchers::containsString)
            .toArray(Matcher[]::new);
    return Matchers.allOf(matchers);
  }

  void expectErrorPage(RequestBuilder builder, String url) throws Exception {
    mvc.perform(builder)
            .andExpect(status().isOk())
            .andExpect(content().contentType(DEFAULT_TEXT_HTML))
            .andExpect(content().string(containsAll(
                    "Error Page", url)));
  }
}
