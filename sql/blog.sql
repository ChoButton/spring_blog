# 블로그 테이블 생성 구문
CREATE TABLE blog(
	blog_id INT AUTO_INCREMENT PRIMARY KEY,
	writer VARCHAR(16) NOT NULL,
    blog_title VARCHAR(200) NOT NULL,
    blog_content VARCHAR(4000) NOT NULL,
    published_at DATETIME DEFAULT now(),
    updated_at DATETIME DEFAULT now(),
    blog_count INT DEFAULT 0
);

# 더미데이터 입력용 구문 (테스트 DB에서만 사용)
INSERT INTO blog VALUES
            (null, '1번유저', '1번제목', '1번본문', now(), now(), null),
            (null, '2번유저', '2번제목', '2번본문', now(), now(), null),
            (null, '3번유저', '3번제목', '3번본문', now(), now(), null);