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

<br>

![image](https://user-images.githubusercontent.com/87354210/182513122-1c2a370c-5f5d-48ad-838b-f29d808bd91e.png)

구글링을 해보니 `@RequestParam` 이나 기타 Prameter 값을 받아올 때 null이거나 Type이 맞지 않는 경우 이 에러가 발생한다고 되어 있었다.
이에 대한 해결 방법은 `@RequestParam(value="name", required=false)` 즉, required 속성의 값을 false로 하면서 해당 Parameter를 반드시 받지 않아도
동작하게끔 만들었다.

그러나, 다시 오류가 발생하는데...

<br>

![image](https://user-images.githubusercontent.com/87354210/182514039-cd1c77fa-7135-4c57-995f-55786fa02b18.png)

이번엔 500번 에러이다. 서버 로그를 살펴보았다.

<br>

![image](https://user-images.githubusercontent.com/87354210/182514149-93ad8d64-cb8d-46cc-9431-965cc75a4d89.png)

그러고 보니 `name`과 `amount`로 된 뷰가 존재하지 않으니, 현재 int로 선언된 amount 객체에는 `null` 값이 들어갈테고 
기본 자료형 int는 null로 받을 수 없다는 것을 기억했다. 따라서 null 값이 가능한 Integer 객체로 amount를 재선언했다.

<br>

![image](https://user-images.githubusercontent.com/87354210/182514589-58bf637c-ff35-470a-b210-1b01297dd2b0.png)

 `http://localhost:8080/hello/dto` 접속에 성공한 모습

<br>

### 해결방안

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

<br>

### 2) h2 console에서 `jdbc:h2:mem:testdb` 로 접속이 안되는 오류
![제목 없음](https://user-images.githubusercontent.com/87354210/183019314-03ed1ba5-e431-4e4b-8351-81b1acac934a.png)

<br> 

책에서 설명한대로 JDBC URL에 입력한 경로인 `mem:testdb` 라는 데이터베이스를 찾을 수 없다고 뜨면서 오류가 발생했다.
또는 데이터베이스를 미리 생성할 수도 없다. h2 기본 설정 자체에서 보안상의 이유로 생성을 못하게 해놓은 것으로 보인다.
구글링 해본 결과, h2가 `1.4.197` 과 `1.4.198` 버전 사이에 대규모 업데이트가 되면서 데이터베이스를 미리 생성하는 것을 
방지하도록 설정되었다는 것을 알 수 있었다.  

<br>

### 해결방안

h2의 버전을 1.4.198보다 낮은 버전으로 설정

`build.gradle`

```
runtimeOnly('com.h2database:h2:1.4.197')
```

<br>

![image](https://user-images.githubusercontent.com/87354210/183020582-10e63d70-950f-49f3-a464-3fc13525d90d.png)  

제대로 접속이 잘 되는 모습.

