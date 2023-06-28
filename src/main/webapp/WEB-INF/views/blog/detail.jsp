<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
    div {
        border: 1px solid black;
    }
</style>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
</head>
<body>
<!--수정을 위한 모달팝업--------------------------------------------------------------------------------------------------------------------------->
    <div class="container">
        <!-- 모달 자리 -->
        <div class="modal fade" id="replyUpdateModal" tabindex="-1">
            <div class="modal-dialog">
              <div class="modal-content">
                <div class="modal-header">
                  <h5 class="modal-title">댓글 수정하기</h5>
                  <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    댓글내용 : <input type="text" class="form-control" id="modalReplyContent" value="">
                    <input type="hidden" id="modalReplyId" value="">
                </div>
                <div class="modal-footer">
                  <button type="button" class="btn btn-primary" id="replyUpdateBtn" data-bs-dismiss="modal">수정하기</button>
                  <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
                </div>
              </div>
            </div>
        </div>

<!--blog 디테일 페이지-------------------------------------------------------------------------------------------------------------------------------->
        <table class="table table-hover">
            <div class="row">
                <div class="col-1">
                    글번호
                </div>
                <div class="col-1">
                    ${blog.blogId}
                </div>
                <div class="col-2">
                    글제목
                </div>
                <div class="col-4">
                    ${blog.blogTitle}
                </div>
                <div class="col-1">
                    작성자
                </div>
                <div class="col-1">
                    ${blog.writer}
                </div>
                <div class="col-1">
                    조회수
                </div>
                <div class="col-1">
                    ${blog.blogCount}
                </div>
            </div>
            <div class="row">
                <div class="col-1">
                    작성일
                </div>
                <div class="col-5">
                    ${blog.publishedAt}
                </div>
                <div class="col-1">
                    수정일
                </div>
                <div class="col-5">
                    ${blog.updatedAt}
                </div>
            </div>
            <div class="row">
                <div class="col-1">
                    본문
                </div>
                <div class="col-11">
                    ${blog.blogContent}
                </div>
            </div>
        </table>
        <a href="/blog/list" class="btn btn-primary">목록으로 돌아가기</a>
        <form action="/blog/delete" method="POST">
            <input type="hidden" name="blogId" value="${blog.blogId}">
            <input type="submit" class="btn btn-primary" value="삭제">
        </form>
        <form action="/blog/updateform" method="POST">
            <input type="hidden" name="blogId" value="${blog.blogId}">
            <input type="submit" class="btn btn-info" value="수정">
        </form>
<!--reply 전체 목록 및 댓글쓰기 기능 구현-------------------------------------------------------------------------------------------->        
        <div class="row">
            <div id="replies">
                </div>
            <div class="row">
                <!-- 비동기 form의 경우는 목적지로 이동하지 않고 페이지 내에서 처리가 되므로
                action을 가지지 않습니다. 그리고 제출버튼도 제출기능을 막고 fetch 요청만 넣습니다.-->
                    <div class="col-2">
                        <input type="text" class="form-control" id="replyWriter" name="replyWriter">
                    </div>
                    <div class="col-6">
                        <input type="text" class="form-control" id="replyContent" name="replyContent">
                    </div>
                    <div class="col-1">
                        <button class="btn btn-primary" id="replySubmit">댓글쓰기</button>
                    </div>
            </div>
        </div>
    </div>
<script>

//1. 특정 게시글의 reply를 다 받아오는 기능---------------------------------------------------------------------------------------------------------------------------
        // 글 구성에 필요한 글번호를 자바스크립트 변수에 저장
        let blogId = "${blog.blogId}"

        // blogId를 받아 전체 데이터를 JS내부로 가져오는 함수
        function getAllReplies(id){
            // `` 내부에서는 $ 앞에 \를 붙여야함 (.jsp 파일 내에서)
            let url = `/reply/\${id}/all`;
            let str = ""; // 받아온 json을 표현할 html코드를 저장할 문자열 str 선언
            // get방식으로 위 주소에 요청넣어서 얻은 데이터값을 res에 대입
            fetch(url, {method:'get'}) 
            // 응답받은 요소중 json만 뽑아서 data에 대입
            .then((res) => res.json()) 
            // 뽑아온 json으로 처리작업하기
            .then(replies => { 
                console.log(replies);
                //for(reply of replies){
                //    console.log(reply);
                //    console.log("--------");
                //    str += `<h3>글쓴이: \${reply.replyWriter},
                //    댓글내용: \${reply.replyContent}</h3>`;
                //}

                // .map()을 이용한 간결한 반복문 처리
                replies.map((reply, i) => { // 첫 파라미터 : 반복문대상자료,
                                            // 두번째 파라미터 : 순번
                    str += 
                        `<h3>
                        \${i+1}번째 댓글 || 글쓴이: 
                            <span id="replyWriter\${reply.replyId}">\${reply.replyWriter}</span>,
                        댓글내용: 
                            <span id="replyContent\${reply.replyId}">\${reply.replyContent}</span>
                            <span class="deleteReplyBtn" data-replyId="\${reply.replyId}">
                                [삭제]
                            </span>
                            <span class="updateReplyBtn" data-replyId="\${reply.replyId}"
                            data-bs-toggle="modal" data-bs-target="#replyUpdateModal">
                                [수정]
                            </span>
                        </h3>`;
                }

                )


                console.log(str); // 저장된 태그 확인
                // #replies 요소를 변수에 저장해주세요
                const $replies = document.getElementById("replies");
                // 저장된 #replies의 innserHTML에 str을 대입해 실제 화면에 출력되게 해주세요
                $replies.innerHTML = str;
            });
        }
        //함수 호출 (함수를 자동 호출시 댓글이 바로 나옴 만일 비동기식으로
        //하고 싶은경우 버튼을 만들고 action으로 함수 호출하면됨)
        getAllReplies(blogId);

//2. reply를 추가하는 기능-----------------------------------------------------------------------------------------------------

        //해당 함수 실행시 비동기 폼에 작성된 글쓴이, 내용으로 댓글 입력
        function insertReply(){
            let url = `/reply`;

            // 요소가 다 채워졌는지 확인
            if(document.getElementById("replyWriter").value.trim() === ""){
                alert("글쓴이를 채워주셔야 합니다.");
                return;
            }
            if(document.getElementById("replyContent").value.trim() === ""){
                alert("본문을 채워주셔야 합니다.");
                return;
            }

            fetch(url, {
                method:'post',
                // header에는 보내는 데이터의 자료형에 대해서 기술
                // json 데이터를 요청과 함께 전달, @RequestBody를 입력받는 로직에 추가
                headers: {"Content-Type": "application/json"},
                // SON.stringify 내부에 실질적으로 요청과 함께 보낼 json정보를 기술함
                body: JSON.stringify({
                    replyWriter: document.getElementById("replyWriter").value,
                    replyContent: document.getElementById("replyContent").value,
                    blogId: "${blog.blogId}"
                }),
                // insert로직이기 때문에 response에 실제 화면에서 사용할 데이터 전송X
            }).then(() => {
                // 댓글 작성 후 폼에 작성되었던 내용 소거
                document.getElementById("replyWriter").value = "";
                document.getElementById("replyContent").value = "";
                alert("댓글 작성이 완료되었습니다!");
                // 댓글 갱신 추가로 호출
                getAllReplies(blogId);
            });
        }

        // 제출 버튼에 이벤트 연결하기
        $replySubmit = document.getElementById("replySubmit");
        // 버튼 클릭시 insertReply 내부 로직 실행
        $replySubmit.addEventListener("click", insertReply);

//3. reply 수정 삭제기능-----------------------------------------------------------------------------------------------------

        // 이벤트 객체를 활용해야 이벤트 위임을 구현하기 수월하므로 먼저 객체부터 가져옵니다.
        // 모든 댓글을 포함하고 있으면서 가장 가까운 영역인 #replies에 설정합니다.
    const $replies = document.getElementById("replies");

    $replies.onclick = (e) => {
            // 클릭한 요소가 #replies의 자식태그인 .deleteReplyBtn 인지 검사하기
            // 이벤트 객체 .target.matches는 클릭한 요소가 어떤 태그인지  검사해줍니다.
            // 삭제버튼이 아닌 부분을 클릭시 로직을 멈춤

            // 아래 코드로 작성후 내부에 매서드를 추가 작성하는 방식으로 가도 되나 중첩이 너무 많아 가독성이 떨어짐
            /*if(e.target.matches('#replies .deleteReplyBtn')){
                const replyId = e.target.dataset.replyid
                if(confirm("정말로 삭제하시겠어요?")){
                    console.log("replyId번 삭제")
                }else{
                    return;
                }
                //console.log(e.target.dataset.replyid);
            }*/

            //

            // 삭제버튼 그리고 수정버튼이 아닐경우 리턴으로 기능실행X
            if(!e.target.matches('#replies .deleteReplyBtn')
                && !e.target.matches('#replies .updateReplyBtn')){
                return;
            // 클릭한 요소가 삭제버튼일 경우 아래에 정의한 deleteReply() 호출해서 클릭된 요소 삭제
            }else if(e.target.matches('#replies .deleteReplyBtn')){
                deleteReply();
            // 클릭된 요소가 수정버튼이면 아래에 정의한 openUpdateReplyMadal() 호출
            }else if(e.target.matches('#replies .updateReplyBtn')){
                openUpdateReplyMadal();
            }

            // 수정버튼을 누르면 실행될 함수
            function openUpdateReplyMadal(){
                const replyId = e.target.dataset.replyid;

                // 가져올 id 요소를 문자로 먼저 저장합니다.
                let replyContentId = `#replyContent\${replyId}`;

                // hidden태그에 현재 내가 클릭한 요소의 replyId값을 value 프로퍼티에 저장해주기
                const $modalReplyId = document.querySelector("#modalReplyId");
                $modalReplyId.value = replyId;

                // 위에서 추출한 id번를 이용해 document.getElementById 를 통해 요소를 가져온 다음
                // 해당 요소의 text값을 얻어서 모달창의 폼 양식 내부에 넣어줍니다.
                
                // 위에서 부여한 id를 이용해 span태그를 가지고 오는 코드
                //let oldReplyContent = document.querySelector(replyContentId).innerText;
                // 태그는 제거하고 내부 문자만 가지고 오도록 처리하는 코드
                //let oldReplyContentText = $oldReplyContent.innerText;

                //위의 두 코드를 합친 코드
                let oldReplyContent = document.querySelector(replyContentId).innerText;

                // modal창 내부의 ReplyWriter, ReplyContent를 적을수 있는 폼을 가져옵니다.
                const $modalReplyContent = document.getElementById("modalReplyContent");
                // 가져온 폼에 문자 주입
                $modalReplyContent.value = oldReplyContent;

            }
            
            // 삭제버튼을 누르면 실행될 함수.
            function deleteReply(){
                // 이벤트 객체의 target 속성의 dataset 속성 내부에 댓글번호가 있으므로 확인
                //console.log(e.target.dataset.replyid);
                const replyId = e.target.dataset.replyid;
                // 예, 아니오로 답할수 있는 경고창을 띄웁니다.
                if(confirm("정말로 삭제하시겠어요?")){ 
                    // 예를 선택하면 true, 아니오를 선택하면 false입니다.
                    console.log("replyId번 삭제")

                    let url = `/reply/\${replyId}`;

                    fetch(url, {method: 'delete'})
                    .then(() => {
                        alert("댓글을 삭제했습니다.")
                        getAllReplies(blogId);
                    });
                    
                }
            }
    };

//4. 수정내용 작성후 반영하는 기능--------------------------------------------------------------------------------------------------------------
    
    // 수정창이 열렸고, 댓글 수정 내역을 모두 폼에 입력한 뒤 수정하기 버튼을 누를경우
    // 비동기 요청으로 수정 요청이 들어가도록 처리
    $replyUpdateBtn = document.querySelector('#replyUpdateBtn');

    $replyUpdateBtn.onclick = (e) => {
        //히든으로 숨겨놓은 태그를 가져온다음
        const $modalReplyId = document.querySelector("#modalReplyId");
        // 변수에 해당 글번호를 저장한 다음
        const replyId = $modalReplyId.value;
        // url에 포함시킴
        const url = `/reply/\${replyId}`;

        // 비동기 요청 넣기
        fetch(url, {
            method: 'PATCH',
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                replyContent : document.querySelector("#modalReplyContent").value,
                replyId : replyId // 위에 선언한 replyId 변수에 들어있는값
            }),
        }).then(() => {
            // 폼소거
            document.getElementById("replyContent").value = "";
            getAllReplies(blogId);//목록갱신
        });
    }

</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4" crossorigin="anonymous"></script>
</body>
</html>