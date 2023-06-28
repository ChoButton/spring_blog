package com.spring.blog.repository;

import com.spring.blog.dto.ReplyResponseDTO;
import com.spring.blog.dto.ReplyCreateRequestDTO;
import com.spring.blog.dto.ReplyUpdateRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReplyRepositoryTest {

    @Autowired // 테스트 코드에서는 필드 주입을 써도 무방합니다.
    ReplyRepository replyRepository;


    @Test
    @Transactional
    @DisplayName("2번 글에 연동된 갯글 개수가 4개인지 확인")
    public void replyFindAllByBlogIdTest(){
        long id = 2;

        List<ReplyResponseDTO> reply = replyRepository.findAllByBlogId(id);

        assertEquals(4, reply.size());
    }

    @Test
    @Transactional
    @DisplayName("3번 댓글의 댓글번호가 3번인지, 글쓴이가 댓글쓴사람3인지 체크")
    public void findByReplyIdTest(){
        long id = 3;

        ReplyResponseDTO reply = replyRepository.findByReplyId(id);

        assertEquals(id, reply.getReplyId());
        assertEquals("댓글쓴사람3", reply.getReplyWriter());
    }

    @Test
    @Transactional
    @DisplayName("댓글번호 2번을 삭제한 다음, 전체 데이터 개수가 3개이고, 그리고 2번으로 재조회시 null일것이다.")
    public void deleteByReplyIdTest(){
        long blogId = 2;
        long replyId = 2;

        replyRepository.deleteByReplyId(replyId);

        assertEquals(3, replyRepository.findAllByBlogId(blogId).size());
        assertNull(replyRepository.findByReplyId(replyId));
    }

    @Test
    @Transactional
    @DisplayName("픽스쳐를 이용해 INSERT후, 전체 데이터를 가져와서 마지막 인덱스 번호 요소 얻어와서 입력했던 fixture 와 비교하면 같을것이다.")
    public void saveTest(){
        long blogId = 1;
        String replyWriter = "안녕";
        String replyContent = "하세요";

        ReplyCreateRequestDTO newReply = ReplyCreateRequestDTO.builder()
                                                .blogId(blogId)
                                                .replyWriter(replyWriter)
                                                .replyContent(replyContent)
                                                .build();

        replyRepository.save(newReply);
        // then : blogId번 글의 전체 댓글을 가지고 온 다음 마지막 인덱스 요소만 변수에 저장한 다음
        //        getter를 이용해 위에서 넣은 fixture와 일치하는지 체크
        // 최신자료 가져오기 위해 특정 블로그의 전체 데이터를 가져온후 원하는 리플만 추출해내기
        List<ReplyResponseDTO> replyList = replyRepository.findAllByBlogId(blogId);
        // 리플 리스트의 개수 -1 이 마지막 인덱스 번호 이므로, 리플리스트의 마지막 인덱스 요소만 가져오기
        ReplyResponseDTO reply = replyList.get(replyList.size() - 1);
        // 단언문 작성
        assertEquals(replyWriter, reply.getReplyWriter());
        assertEquals(replyContent, reply.getReplyContent());
    }

    @Test
    @Transactional
    @DisplayName("fixture로 수정할 댓글쓴이, 댓글내용, 3번 replyId를 지정합니다. 수정 후 3번자료를 DB에서 꺼내 fixture비교 및 published_at과 updated_at이 다른지 확인")
    public void replyUpdateTest(){
        long id = 3;
        String replyContent = "수정된 본문";

        ReplyUpdateRequestDTO updateReply = ReplyUpdateRequestDTO.builder()
                                             .replyContent(replyContent)
                                             .replyId(id)
                                             .build();
        replyRepository.update(updateReply);

        ReplyResponseDTO reply = replyRepository.findByReplyId(id);

        assertEquals(id, reply.getReplyId());
        assertEquals(replyContent, reply.getReplyContent());
        // 글쓴시간보다 수정시간이 이후일것이다 라는 구문
        assertTrue(reply.getUpdatedAt().isAfter(reply.getPublishedAt()));
    }

    @Test
    @Transactional
    @DisplayName("blogId가 2인 글을 삭제하면, 삭제한 글의 전체 댓글 조회시 길이가 0일것이다.")
    public void deleteAllReplyByBlogIdTest(){
        long blogId = 2;

        replyRepository.deleteAllReplyByBlogId(blogId);

        assertEquals(0, replyRepository.findAllByBlogId(blogId).size());
    }
}