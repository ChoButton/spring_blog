package com.spring.blog.service;

import com.spring.blog.dto.ReplyCreateRequestDTO;
import com.spring.blog.dto.ReplyResponseDTO;
import com.spring.blog.dto.ReplyUpdateRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReplyServiceTest {

    @Autowired
    ReplyService replyService;

    @Test
    @Transactional
    @DisplayName("2번 글에 연동된 갯글 개수가 4개인지 확인")
    public void findAllByBlogIdTest(){
        long blogId = 2;

        List<ReplyResponseDTO> reply = replyService.findAllByblogId(blogId);

        assertEquals(4, reply.size());
    }

    @Test
    @Transactional
    @DisplayName("3번 댓글의 댓글번호가 3번인지, 글쓴이가 댓글쓴사람3인지 체크")
    public void findByReplyIdTest(){
        long replyId = 3;
        String replyWriter = "댓글쓴사람3";

        ReplyResponseDTO reply = replyService.findByReplyId(replyId);

        assertEquals(replyId, reply.getReplyId());
        assertEquals(replyWriter, reply.getReplyWriter());
    }

    @Test
    @Transactional
    @DisplayName("댓글번호 3번을 삭제한 다음, 전체 데이터 개수가 3개이고, 그리고 3번으로 재조회시 null일것이다.")
    public void deleteByReplyId(){
        long blogId = 2;
        long replyId = 3;

        replyService.deleteByReplyId(replyId);

        assertEquals(3, replyService.findAllByblogId(blogId).size());
        assertNull(replyService.findByReplyId(replyId));
    }

    @Test
    @Transactional
    @DisplayName("픽스쳐를 이용해 INSERT후, 전체 데이터를 가져와서 마지막 인덱스 번호 요소 얻어와서 입력했던 fixture 와 비교하면 같을것이다.")
    public void saveTest(){
        long blogId = 1;
        String replyWriter = "안녕";
        String replyContest = "하세요";

        ReplyCreateRequestDTO newReply = ReplyCreateRequestDTO.builder()
                .blogId(blogId)
                .replyWriter(replyWriter)
                .replyContent(replyContest)
                .build();
        replyService.save(newReply);

        List<ReplyResponseDTO> replyList = replyService.findAllByblogId(blogId);
        ReplyResponseDTO lastReply = replyList.get(replyList.size()-1);

        assertEquals(1, replyList.size());
        assertEquals(replyWriter, lastReply.getReplyWriter());
        assertEquals(replyContest, lastReply.getReplyContent());
    }

    @Test
    @Transactional
    @DisplayName("fixture로 수정할 댓글내용, 3번 replyId를 지정합니다. 수정 후 3번자료를 DB에서 꺼내 fixture비교 및 published_at과 updated_at이 다른지 확인")
    public void replyUpdateTest(){
        long replyId = 3;
        String replyContent = "수정본문";

        ReplyUpdateRequestDTO replyUpdateDTO = ReplyUpdateRequestDTO.builder()
                .replyId(replyId)
                .replyContent(replyContent)
                .build();
        replyService.update(replyUpdateDTO);

        ReplyResponseDTO reply = replyService.findByReplyId(replyId);

        assertEquals(replyId, reply.getReplyId());
        assertEquals(replyContent, reply.getReplyContent());
        assertTrue(reply.getUpdatedAt().isAfter(reply.getPublishedAt()));

    }


}
