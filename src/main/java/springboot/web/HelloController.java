package springboot.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springboot.web.dto.HelloResponseDto;

@RestController                 // 컨트롤러를 JSON으로 반환하는 컨트롤러로 만들어줌
public class HelloController {

    @GetMapping("/hello/dto")       // HTTP Method인 Get 요청을 받을 수 있는 API를 만들어줌
    public HelloResponseDto helloDto(@RequestParam("name") String name,
                                     @RequestParam("amount") int amount) {
        return new HelloResponseDto(name, amount);
    }
}
