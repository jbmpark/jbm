package org.book.springboot.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.book.springboot.domain.posts.Posts;
import org.book.springboot.domain.posts.PostsRepository;
import org.book.springboot.web.dto.PostsSaveRequestDto;
import org.book.springboot.web.dto.PostsUpdateRequestDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @After
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
    }

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Before
    public void setup(){
        mvc= MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles="USER")
    public void post_Posts() throws Exception{
        //given
        String title = "test title";
        String content = "test content";
        String author = "jbm.park";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();

        String url = "http://localhost:"+port+"/api/v1/posts";

        //when
        //ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());



        //then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);

        System.out.println(all.get(0).getTitle());
        System.out.println(all.get(0).getContent());
    }

    @Test
    @WithMockUser(roles="USER")
    public void update_Posts() throws Exception{
        //given
        String title = "test title";
        String content = "test content";
        String author = "jbm.park";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();

        String url = "http://localhost:"+port+"/api/v1/posts";

        //when
        //ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto))
        )
                .andExpect(status().isOk());

        //then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);



        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);

        System.out.println(all.get(0).getTitle());
        System.out.println(all.get(0).getContent());

        // update

        String title2 = "updated title";
        String content2 = "updated content";
        PostsUpdateRequestDto updateRequestDto = PostsUpdateRequestDto.builder()
                .title(title2).content(content2)
                .build();
        HttpEntity<PostsUpdateRequestDto> requestDtoHttpEntity = new HttpEntity<>(updateRequestDto);

        final Long updateId = all.get(0).getId();
        System.out.println("update Id : "+updateId);


        String url2 = "http://localhost:"+port+"/api/v1/posts/"+updateId;
//        ResponseEntity<Long> responseEntity1 = restTemplate.exchange(url2, HttpMethod.PUT, requestDtoHttpEntity, Long.class);
//        assertThat(responseEntity1.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity1.getBody()).isGreaterThan(0L);
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.put(url2)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(new ObjectMapper().writeValueAsString(updateRequestDto));
        mvc.perform(builder)
                .andExpect(status().isOk());


        all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title2);
        assertThat(all.get(0).getContent()).isEqualTo(content2);

        System.out.println(all.get(0).getId());
        System.out.println(all.get(0).getTitle());
        System.out.println(all.get(0).getContent());
//        Optional<Posts> posts= postsRepository.findById(updateId);
//        assertThat(posts.getTitle()).isEqualTo(title2);
//        assertThat(posts.getContent()).isEqualTo(content2);
//
//        System.out.println(posts.getTitle());
//        System.out.println(posts.getContent());
    }


    @Test
    @WithMockUser(roles="USER")
    public void time_Posts() throws Exception{
        //given
        LocalDateTime now = LocalDateTime.of(2022,4,25,0,0,0);
        String title = "test title";
        String content = "test content";
        String author = "jbm.park";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();

        String url = "http://localhost:"+port+"/api/v1/posts";

        //when
//        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);
//
//        //then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(new ObjectMapper().writeValueAsString(requestDto))
                )
                .andExpect(status().isOk());

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);

        System.out.println(">>>>>>>>> createDate="+all.get(0).getCreatedDate());
        System.out.println(">>>>>>>>> modifiedDate="+all.get(0).getModifiedDate());
        System.out.println(all.get(0).getTitle());
        System.out.println(all.get(0).getContent());
    }


}
