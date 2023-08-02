package com.onebyte.springboilerplate.controller;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import com.onebyte.springboilerplate.config.RestDocsConfiguration;
import com.onebyte.springboilerplate.dto.UserDto;
import com.onebyte.springboilerplate.service.UserService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureRestDocs
@WebMvcTest(UserController.class)
@Import(RestDocsConfiguration.class)
class UserControllerTest {

  @Autowired
  MockMvc mvc;

  @MockBean
  UserService userService;

  @Test
  void test() throws Exception {
    FieldDescriptor[] reviews = getReviewFieldDescriptors();

    List<UserDto> list = new ArrayList<>();
    UserDto user = UserDto.builder().id(1).username("bell").age(26).build();
    list.add(user);

    // when
    Mockito.when(userService.findUserAll())
        .thenReturn(list);

    ResultActions actions = mvc.perform(MockMvcRequestBuilders.get("/v1/users")
        .accept(MediaType.APPLICATION_JSON));
    actions.andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value("bell"))
        .andDo(MockMvcRestDocumentation.document("user"));
  }

  private FieldDescriptor[] getReviewFieldDescriptors() {
    return new FieldDescriptor[]{
        fieldWithPath("username").description("이름"),
        fieldWithPath("age").description("나이")
    };
  }
}