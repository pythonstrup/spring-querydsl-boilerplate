package com.onebyte.springboilerplate.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.onebyte.springboilerplate.config.RestDocsConfiguration;
import com.onebyte.springboilerplate.dto.UserDto;
import com.onebyte.springboilerplate.dto.UserSearchCondition;
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
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@AutoConfigureRestDocs
@WebMvcTest(UserController.class)
@Import(RestDocsConfiguration.class)
class UserControllerTest {

  @Autowired
  MockMvc mvc;

  @MockBean
  UserService userService;

  final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  public void testFindUser() throws Exception {
    // given
    UserDto response = UserDto.builder().id(1).username("bell").age(26).build();

    // when
    Mockito.when(userService.findUser(1))
        .thenReturn(response);

    // then
    ResultActions actions = mvc.perform(MockMvcRequestBuilders.get("/v1/users/1")
        .contentType(MediaType.APPLICATION_JSON));

    actions.andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data", equalTo(asParsedJson(response))))
        .andDo(MockMvcRestDocumentation.document("user/findUser"))
        .andDo(print());
  }

  @Test
  void testFindUserAll() throws Exception {
    FieldDescriptor[] reviews = getReviewFieldDescriptors();

    // given
    List<UserDto> list = new ArrayList<>();
    UserDto user = UserDto.builder().id(1).username("bell").age(26).build();
    list.add(user);

    // when
    Mockito.when(userService.findUserAll())
        .thenReturn(list);

    // then
    ResultActions actions = mvc.perform(MockMvcRequestBuilders.get("/v1/users")
        .accept(MediaType.APPLICATION_JSON));
    actions.andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data", equalTo(asParsedJson(list))))
        .andDo(MockMvcRestDocumentation.document("user/findUserAll"))
        .andDo(print());
  }

  @Test
  void testUserSearch() throws Exception {
    // given
    UserSearchCondition search = new UserSearchCondition();
    search.setAge(26);
    UserDto user1 = UserDto.builder().id(1).username("bell").age(26).build();
    UserDto user2 = UserDto.builder().id(3).username("park").age(26).build();
    System.out.println(search);

    // when
    List<UserDto> response = List.of(user1, user2);
    Mockito.when(userService.searchUser(search))
        .thenReturn(response);

    // then
    // 파라미터 넘기기 방법 1
//    ResultActions actions = mvc.perform(MockMvcRequestBuilders.get("/v1/users/search")
//        .param("age", "26")
//        .contentType(MediaType.APPLICATION_JSON));

    // 파라미터 넘기기 방법 2
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add("age", "26");
    ResultActions actions = mvc.perform(MockMvcRequestBuilders.get("/v1/users/search")
        .params(map));
    actions.andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data", equalTo(asParsedJson(response))))
        .andDo(MockMvcRestDocumentation.document("user/searchUser"));
  }

  @Test
  void testUserSave() throws Exception {
    // given
    UserDto request = UserDto.builder().username("bell").age(26).build();
    UserDto response = UserDto.builder().id(1).username("bell").age(26).build();

    // when
    Mockito.when(userService.save(request))
        .thenReturn(response);

    // then
    String content = objectMapper.writeValueAsString(request);
    ResultActions actions = mvc.perform(MockMvcRequestBuilders.post("/v1/users")
        .content(content)
        .contentType(MediaType.APPLICATION_JSON));
    actions.andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(equalTo(asParsedJson(response))))
        .andDo(MockMvcRestDocumentation.document("user/saveUser"))
        .andDo(print());
  }

  @Test
  public void testUpdateUser() throws Exception {
    // given
    UserDto request = UserDto.builder().username("change").age(27).build();
    UserDto response = UserDto.builder().id(1).username("change").age(27).build();

    // when
    Mockito.when(userService.update(1, request))
        .thenReturn(response);

    // then
    String content = objectMapper.writeValueAsString(request);
    ResultActions actions = mvc.perform(MockMvcRequestBuilders.put("/v1/users/1")
        .content(content)
        .contentType(MediaType.APPLICATION_JSON));
    actions.andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data", equalTo(asParsedJson(response))))
        .andDo(MockMvcRestDocumentation.document("user/updateUser"))
        .andDo(print());
  }

  private FieldDescriptor[] getReviewFieldDescriptors() {
    return new FieldDescriptor[]{
        fieldWithPath("username").description("이름"),
        fieldWithPath("age").description("나이")
    };
  }

  private Object asParsedJson(Object obj) throws JsonProcessingException {
    String json = objectMapper.writeValueAsString(obj);
    return JsonPath.read(json, "$");
  }
}