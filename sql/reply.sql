##
CREATE TABLE reply(
	reply_id INT PRIMARY KEY AUTO_INCREMENT,
    blog_id int NOT NULL,
    reply_writer VARCHAR(40) NOT NULL,
    reply_content VARCHAR(200) NOT NULL,
    published_at DATETIME DEFAULT NOW(),
    updated_at DATETIME DEFAULT NOW()
);

# 외래키 설정
# blog_id 에는 기존에 존재하는 글의 blog_id만 들어가야 한다
ALTER TABLE reply ADD CONSTRAINT fk_reply FOREIGN KEY (blog_id) REFERENCES blog(blog_id);

# 더미 데이터 입력(테스트 DB에서만 사용)
INSERT INTO reply VALUES
	(null, 2, "댓글쓴사람", "1빠댓글이다", now(), now()),
    (null, 2, "댓글쓴사람2", "강아지 귀여워", now(), now()),
    (null, 2, "댓글쓴사람3", "구름이 귀여워", now(), now()),
    (null, 2, "댓글쓴사람4", "단추 귀여워", now(), now()),
    (null, 3, "댓글쓴사람5", "네로도 너무 귀여워", now(), now());