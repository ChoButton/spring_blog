package com.spring.blog.controller;

import com.spring.blog.dto.BmiDTO;
import com.spring.blog.dto.PersonDTO;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Controller // 컨트롤러 지정
//@ResponseBody // REST형식 전환, 메서드 위에 붙으면 해당 메서드만 REST 형식으로
@RestController // 위 2개 어노테이션을 한번에 지정해줌
@RequestMapping("/resttest")
@CrossOrigin(origins = "http://127.0.0.1:5500") // 해당 출처의 비동기 요청 허용
public class RESTApiController {

    // REST 컨트롤러는 크게 json을 리턴하거나, String을 리턴하거나 만들 수 있습니다.

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello(){
        return "안녕하세요";
    }

    // 문자 배열도 리턴할 수 있습니다.
    @RequestMapping(value = "/foods", method = RequestMethod.GET)
    public List<String> foods(){
        List<String> foodList = List.of("탕수육", "똠양꿍", "돈가츠");
        return foodList;
    }

    @RequestMapping(value = "/person", method = RequestMethod.GET)
    public PersonDTO person(){
        PersonDTO p = new PersonDTO(1L, "좋코더", 20);
        return p;
    }

    // 상태코드까지 함께 리턴할 수 있는 ReponseEntitu<>를 리턴자료형으로 지정.
    @GetMapping("/person-list")
    public ResponseEntity<?> personList(){
        PersonDTO p = PersonDTO.builder().id(1).name("ㅁ").age(10).build();
        PersonDTO p2 = PersonDTO.builder().id(2).name("ㄴ").age(11).build();
        PersonDTO p3 = PersonDTO.builder().id(3).name("ㅇ").age(12).build();
        List<PersonDTO> personList = List.of(p,p2, p3);

        // .ok()는 200코드를 반환하고, 뒤에 연달아 붙은 body()에 실제 리턴자료를 입력합니다.
        return ResponseEntity.ok().body(personList);
    }

    // 200이 아닌 예외코드를 리턴해보겠습니다.
    @RequestMapping(value = "/bmi", method = RequestMethod.GET)
    public ResponseEntity<?> bmi(BmiDTO bmi){ // 커맨드 객체 형식으로 사용됨

        // 예외처리
        if(bmi.getHeight() == 0){
            return ResponseEntity
                    .badRequest() // 400
                    .body("키에 0을 넣을수 없습니다");
        }


        double result = bmi.getWeight() / ((bmi.getHeight()/100) * (bmi.getHeight()/100));

        // 헤더 추가해보기
        HttpHeaders headers = new HttpHeaders();
        headers.add("fruits", "melon");
        headers.add("lunch", "pizza");

        return ResponseEntity
                .ok() // 200
                .headers(headers) // 헤더 추가
                .body(result); // 사용자에게 보여질 데이터

    }

    // Request 역직렬화, Response 직렬화

    // @RequestBody JSON으로 보내준 데이터를 역직렬화 해주는 어노테이션
    // Postman 을 활용한 json 데이터 파라미터로 전송해 요청 넣기
    @RequestMapping(value = "/bmi2", method = RequestMethod.GET)
    public ResponseEntity<?> bmi2(@RequestBody BmiDTO bmi){ // 커맨드 객체 형식으로 사용됨

        // 예외처리
        if(bmi.getHeight() == 0){
            return ResponseEntity
                    .badRequest() // 400
                    .body("키에 0을 넣을수 없습니다");
        }


        double result = bmi.getWeight() / ((bmi.getHeight()/100) * (bmi.getHeight()/100));

        // 헤더 추가해보기
        HttpHeaders headers = new HttpHeaders();
        headers.add("fruits", "melon");
        headers.add("lunch", "pizza");

        return ResponseEntity
                .ok() // 200
                .headers(headers) // 헤더 추가
                .body(result); // 사용자에게 보여질 데이터

    }

}
