package com.spring.blog.service;

import com.spring.blog.dto.ReplyResponseDTO;
import com.spring.blog.dto.ReplyCreateRequestDTO;
import com.spring.blog.dto.ReplyUpdateRequestDTO;
import com.spring.blog.entity.Reply;

import java.util.List;

public interface ReplyService {
    // 댓글 번호 입력시 전체 조회해서 리던해 주는 findByblogId() 메서드 정의
    List<Reply> findAllByblogId(long blogId);

    // 단일 댓글 번호 입력시, 댓글 정보를 리턴해주는 findByReplyId() 메서드 정의
    Reply findByReplyId(long replyId);

    // 댓글번호 입력시 삭제되도록 해 주는 deleteByReplyId() 메서드 정의
    void deleteByReplyId(long replyId);

    // insert 용도로 정의한 DTO를 넘겨서 save() 메서드 정의
    void save(Reply reply);

    void update(Reply reply);
}
