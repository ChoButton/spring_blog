package com.spring.blog.dto;

import com.spring.blog.entity.Reply;
import lombok.*;

@Getter @Setter @ToString @Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyCreateRequestDTO {
    // 글번호, 댓글쓴이, 댓글내용
    private long blogId;
    private String replyWriter;
    private String replyContent;

    public ReplyCreateRequestDTO(Reply reply){
        this.blogId = reply.getBlogId();
        this.replyWriter = reply.getReplyWriter();
        this.replyContent = reply.getReplyContent();
    }
}
