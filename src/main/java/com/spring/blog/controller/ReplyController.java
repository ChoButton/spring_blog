package com.spring.blog.controller;

import com.spring.blog.dto.ReplyResponseDTO;
import com.spring.blog.dto.ReplyCreateRequestDTO;
import com.spring.blog.dto.ReplyUpdateRequestDTO;
import com.spring.blog.exception.NotFoundReplyByReplyIdException;
import com.spring.blog.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reply")
public class ReplyController {

    // 컨트롤러는 서비스를 호출
    private ReplyService replyService;

    @Autowired
    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    // 글 번호에 맞는 전체 댓들을 가져오는 메서드
    // 어떤 자원에 접근할것인지만 uri에 명시(메서드가 행동을 결정함)
    // http://localhost:8080/reply/{번호}/all
    // http://localhost:8080/reply/300/all => blogId 파라미터에 300이 전달된것으로 간주한다.
    @RequestMapping(value = "/{blogId}/all", method = RequestMethod.GET)
    // rest서버는 응답시 응답코드와 응답객체를 넘기기 떄문에 ResponseEntity<자료형>
    // 을 리턴
    public ResponseEntity<List<ReplyResponseDTO>> findAllReplies(@PathVariable long blogId) {
        // 서비스에서 리플 목록을 들고 옵니다.
        List<ReplyResponseDTO> replies = replyService.findAllByblogId(blogId);


        return ResponseEntity
                .ok()//replies) // 응답코드, 상태 코드와 body에 전송할 데이터를 같이 작성할 수도 있음
                .body(replies); // 응답객체

        /*
            1. http://localhost:8080/reply/2/all 로 사용자가 접속함
            2. blogId 변수에 2가 전달됨
            3. replyService.findAllByBlogId(2); 가 호출됨
            4. replyRepository.findAllByBlogId(2); 가 호출됨
            5. 하기 쿼리문이 호출됨
                    SELECT
                        reply_id as replyId,
                        reply_writer as replyWriter,
                        reply_content as replyContent,
                        published_at as publishedAt,
                        updated_at as updatedAt
                    FROM
                        reply
                    WHERE
                        blog_id = 2
            6. 5번 쿼리문의 결과로 replyRepository.findAllByBlogId(2)가 2번글에 엮인 전체 리플 목록을 리턴함
            7. 6번 호출구문의 결과로 replyService.findAllByBlogId(2)가 2번글에 엮인 전체 리플 목록을 리턴함
            8. 컨트롤러에서 전체 리플 목록을 받아서 변수에 저장함
            9. 저장된 변수를 200코드와 함께 사용자에게 응답해줌.
         */
    }

    // replyId를 주소에 포함시켜서 요청하면 해당 번호 댓글 정보를 json으로 리턴하는 메서드
    @RequestMapping(value = "/{replyId}", method = RequestMethod.GET)
    public ResponseEntity<?> findByReplyId(@PathVariable long replyId) {

        // 서비스에서 특정 번호 리플을 가져옵니다.
        ReplyResponseDTO reply = replyService.findByReplyId(replyId);
        if (reply == null) {
            try {
                throw new NotFoundReplyByReplyIdException("없는 리플 번호를 조회했습니다.");
            } catch (NotFoundReplyByReplyIdException e) {
                e.printStackTrace();
                return new ResponseEntity<>("찾는 댓글 번호가 없습니다.", HttpStatus.NOT_FOUND);
            }
        }
        //return ResponseEntity.ok().body(reply); 아래와 같은 의미의 코드
        return ResponseEntity.ok(reply);
    }

    // post방식으로 /reply 주소로 요청이 들어왔을때 실행되는 메서드 insertReply()를 작성해주세요.
    @RequestMapping(value = "", method = RequestMethod.POST)
    // Rest컨트롤러는 데이터를 json으로 주고받음.
    // 따라서 @RequestBody를 이용해 json으로 들어온 데이터를 역직렬화 하도록 설정
    public ResponseEntity<String> insertReply(@RequestBody ReplyCreateRequestDTO replyCreateRequestDTO) {
        replyService.save(replyCreateRequestDTO);
        return ResponseEntity.ok("댓글등록 완료");
    }

    // delete 방식으로 /reply/{댓글번호} 주소로 요청이 들어왔을때 실행되는 메서드 deleteReply()를 작성해주세요.
    @RequestMapping(value = "/{replyId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteReply(@PathVariable long replyId){
            replyService.deleteByReplyId(replyId);
            return ResponseEntity.ok("댓글삭제 완료");
    }

    // 수정 로직은 put, patch 메서드로 /reply/댓글번호 주소로
    // ReplyUpdateRequestDTO를 repuestBody로 받아 요청처리를 하게 만들어 주세요
    @RequestMapping(value = "/{replyId}", method = RequestMethod.PATCH)
    public ResponseEntity<String> replyUpdate(@PathVariable long replyId,
                                              @RequestBody ReplyUpdateRequestDTO replyUpdateRequestDTO){
        // json 데이터에 replyId를 포함하는 대신 url에 포함시켰으므로 requestBody에 추가해줘야함.
        System.out.println("replyId 주입 전 : " + replyUpdateRequestDTO);
        replyUpdateRequestDTO.setReplyId(replyId);
        System.out.println("replyId 주입 후 : " + replyUpdateRequestDTO);
        replyService.update(replyUpdateRequestDTO);

        return ResponseEntity.ok("댓글수정 완료");
    }

}