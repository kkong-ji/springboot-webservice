# springboot-webservice
스프링 부트와 AWS로 혼자 구현하는 웹 서비스 
<br>
저) 이동욱


![스프링부트와aws책표지](https://user-images.githubusercontent.com/87354210/182511398-c9aad782-ae26-41ad-addd-57ec354fd55b.png)

## Trouble Shooting

### 1) `HelloControllerTest` 를 실행시켰을 때, `404 error` 발생

404 error는 보통 디렉토리 오류이거나 매핑되는 파일을 찾지 못했을 때 혹은 서버 설정에 문제가 생긴 경우가 많은데
책의 초반부터 이러한 오류가 발생해서 애를 먹었다. 일단은 서버 로그를 보기 위해 애플리케이션을 실행시키고 http://localhost:8080/hello/dto
로 접속하였더니 다음과 같은 로그가 떴다.

![image](https://user-images.githubusercontent.com/87354210/182513122-1c2a370c-5f5d-48ad-838b-f29d808bd91e.png)

구글링을 해보니 `@RequestParam` 이나 기타 Prameter 값을 받아올 때 null이거나 Type이 맞지 않는 경우 이 에러가 발생한다고 되어 있었다.
이에 대한 해결 방법은 `@RequestParam(value="name", required=false)` 즉, required 속성의 값을 false로 하면서 해당 Parameter를 반드시 받지 않아도
동작하게끔 만들었다.

그러나, 다시 오류가 발생하는데...
<br>
![image](https://user-images.githubusercontent.com/87354210/182514039-cd1c77fa-7135-4c57-995f-55786fa02b18.png)

이번엔 500번 에러이다. 서버 로그를 살펴보니,

![image](https://user-images.githubusercontent.com/87354210/182514149-93ad8d64-cb8d-46cc-9431-965cc75a4d89.png)

이러한 에러가 떳다. 그러고 보니 `name`과 `amount`로 된 뷰가 존재하지 않으니, 현재 int로 선언된 amount 객체에는 `null` 값이 들어갈테고 
기본 자료형 int는 null로 받을 수 없다는 것을 기억했다. 따라서 null 값이 가능한 Integer 객체로 amount를 재선언했다.

![image](https://user-images.githubusercontent.com/87354210/182514589-58bf637c-ff35-470a-b210-1b01297dd2b0.png)

 `http://localhost:8080/hello/dto` 접속에 성공한 모습

-> 해결

기존: `HelloController`
```
    @GetMapping("/hello/dto")       
    public HelloResponseDto helloDto(@RequestParam("name") String name,
                                     @RequestParam("amount") int amount) {
        return new HelloResponseDto(name, amount);
    }
```

<br>

수정: `HelloController`
```
    @GetMapping("/hello/dto")       
    public HelloResponseDto helloDto(@RequestParam(value="name", required=false) String name,
                                     @RequestParam(value="amount", required=false) Integer amount) {
        return new HelloResponseDto(name, amount);
    }
```
