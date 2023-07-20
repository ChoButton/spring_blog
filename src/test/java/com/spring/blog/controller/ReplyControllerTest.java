package com.spring.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.blog.dto.ReplyCreateRequestDTO;
import com.spring.blog.dto.ReplyUpdateRequestDTO;
import com.spring.blog.repository.ReplyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc // MVC테스트는 브라우저를 켜야 원래 테스트가 가능하므로 대체할 객체를 만들어 수행
class ReplyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    // 데이터 직렬화에 사용하는 객체
    @Autowired
    private ObjectMapper objectMapper;

    // 임시적으로 replyRepository를 생성
    // 레포지토리 레이어의 메서드는 쿼리문을 하나만 호출하는것이 보장되지만
    // 서비스 레이어의 메서드는 추후에 쿼리문을 두 개 이상 호출할수도 있고, 그런 변경이 생겼을때 테스트 코드도 같이 수정할 가능성이 생김
    @Autowired
    private ReplyRepository replyRepository;

    // 컨트롤러를 테스트 해야하는데 컨트롤러는 서버에 url만 입력하면 동작하므로 컨트롤러를 따로 생성하지는 않습니다.
    // 각 테스트전에 설정하기
    @BeforeEach
    public void setMockMvc() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @Transactional
    @DisplayName("2번 글에 대한 전체 댓글을 조회했을때 0번째 요소의 replyWriter는 댓글쓴사람, replyId는 1")
    void findAllRepliesTest() throws Exception { // mockMvc의 예외를 던져줄 Exception
        // given : fixture 설정, 접속 주소 설정
        long replyId = 1;
        String replyWriter = "댓글쓴사람";
        String url = "/reply/2/all";

        // when : 위에 설정한 url로 접속 후 json 데이터 리턴받아 저장하기. ResultActions 형 자료로 json 저장하기
        // get() 메서드의 경우 작서 후 alt + enter 눌러서 mockmvc 관련 요소로 import
                                  // fetch(url, {method: 'get'}).then(res => res.json()); 에 대응하는 코드
        final ResultActions result = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));

        // then : 리턴받은 json 목록의 0번째 요소의 replyWriter와 replyId가 예상과 일치하는지 확인
        result
                .andExpect(status().isOk()) // 200코드가 출력되었는지 확인
                                         //  $는 json 전체 데이터 의미 $[0]은 json 전체 데이터중 0번째 데이터를 뜻함
                .andExpect(jsonPath("$[0].replyWriter").value(replyWriter)) // 첫 json의 replyWriter 검사
                .andExpect(jsonPath("$[0].replyId").value(replyId)); // 첫 json의 replyId 검사
    }

    @Test
    @Transactional
    @DisplayName("replyId 2번 조회시 얻어진 json객체의 replyWriter는 댓글쓴사람2 replyContent는 강아지 귀여워")
    public void findByReplyIdTest() throws Exception{
        // given : fixture 세팅 및 요청 주소 세팅
        long replyId = 2;
        String replyWriter = "댓글쓴사람2";
        String replyContent = "강아지 귀여워";

        String url = "/reply/2";

        final ResultActions result = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("replyContent").value(replyContent))
                .andExpect(jsonPath("replyWriter").value(replyWriter));
    }

    @Test
    @Transactional
    @DisplayName("blogId 1번에 replyWriter는 또사라짐 replyContent 가기싫다를 넣고 등록후 전체 댓글 조회시 fixture 일치")
    public void insertReplyTest() throws Exception{
        // given : 픽스처 생성 및 ReplyImsertDTO 객체 생성 후 픽스처 주입
        long blogId = 1;
        String replyWriter = "또사라짐";
        String replyContent = "가기싫다";
        String url = "/reply";
        String url2 = "/reply/1/all";

        ReplyCreateRequestDTO newReply = ReplyCreateRequestDTO.builder()
                .blogId(blogId)
                .replyWriter(replyWriter)
                .replyContent(replyContent)
                .build();

        // json으로 데이터 직렬화
        final String requestBody = objectMapper.writeValueAsString(newReply);

        // when : 직렬화된 데이터를 이용해 post방식으로
        mockMvc.perform(post(url) // /reply 주소에 post방식으로 요청을 넣고
                .contentType(MediaType.APPLICATION_JSON) // 전달 자료는 json이며
                .content(requestBody)); // 위에서 직렬화한 requestBody 변수를 전달할 것이다.

        // then : 위에서 blogId로 지정한 1번글의 전체 데이터를 가져와서,
        //       픽스처와 replyWriter, replyContent가 일치하는지 확인
        final ResultActions result = mockMvc.perform(get(url2)
                                    .accept(MediaType.APPLICATION_JSON));

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].replyWriter").value(replyWriter))
                .andExpect(jsonPath("$[0].replyContent").value(replyContent));
    }

    @Test
    @Transactional
    @DisplayName("댓글번호 3번을 삭제한 경우, 글번호 2번의 댓글수는 3개, 그리고 단일댓글 조회시 null")
    public void deleteReplyTest() throws Exception{
        // given : 픽스처 세팅
        long blogId = 2;
        long replyId = 3;
        String url = "/reply/3";

        // when : 삭제 수행
        // .accept는 리턴데이터가 있는 경우에 해당 데이터를 어떤 형식으로 받아올지 기술
        mockMvc.perform(delete(url).accept(MediaType.TEXT_PLAIN));

        // then : repository를 이용해 전체 데이터를 가져온 후, 개수 비교 및 삭제한 3번 댓글은 null이 리턴되는지 확인
        assertThat(replyRepository.findAllByBlogId(blogId).size()).isEqualTo(3);
        assertThat(replyRepository.findByReplyId(replyId)).isNull();
    }

    @Test
    @Transactional
    @DisplayName("댓글번호 4번을 replyWriter를 수정댓글쓴이로, replyContent를 수정댓글로 바꾼뒤 조회시 픽스처 일치")
    public void replyUpdateTest() throws Exception{
        String replyContent = "수정댓글";
        String url = "/reply/4";

        ReplyUpdateRequestDTO upReply = ReplyUpdateRequestDTO.builder()
                .replyContent(replyContent)
                .build();

        final String upReplyJson = objectMapper.writeValueAsString(upReply);

        mockMvc.perform(patch(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(upReplyJson));

        final ResultActions result = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("replyContent").value(replyContent));
    }
}