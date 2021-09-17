# mini-WAS

#### 본 프로그래밍 과제는 Java 로 간단한 WAS(Web Application Server)를 만드는 것입니다.

개발 환경
---
|분야  |stack  |
|--|--|
| 언어 | Java (JRE 1.8) |
| HTTP Version | HTTP/1.1 |  
| 빌드 툴 | Maven  |
| Persistence 프레임워크 | JPA/Hibernate |  
| 단위 테스트 라이브러리| Junit 4(AssertJ 3)|  
| 로깅 프레임워크| Logback 1.2.3 |
| IDE | IntelliJ|


프로젝트 스펙
---
- HTTP/1.1 의 Host 헤더를 해석
  - RequestProcessor 클래스에서 개별 인스턴스를 풀에 등록 후 처리
  - 쿼리 스트링, 쿠키값 파싱 유틸 생성
  - HTTP Request (Header, Body, Line) 클래스별 분리
  - Request 데이터 읽는 유틸 생성 (Strings, maps 컬렉션 Request 파싱 시 사용, key value 형식)
  - HTTP Response (Header, Body, StatusLine, statusCode) 클래스별 분리, 버전 명시, get, write, new 메서드 구현
  - Response notFound, error, processFile, addHeader 메서드 구현

- 설정 파일 관리 (JSON)
  - PORT, ROOT, HTTP_STATUS_CODE_LIST JSON 파일에서 읽어서 처리
  - 403, 404, 500 오류 시 HTML 파일 이름 JSON 파일에서 읽어서 처리
  - HttpResponse 에 HttpStatusCode 3글자로 JSON에 저장되어있는 value 값을 찾아 makeHtml 메서드에서 작성

- 보안 규칙
  - 상위 디렉터리 접근 시 403 에러 처리
  - 확장자가 .exe인 파일 요청 시 403 에러 처리
  - 추후 규칙 추가 확장성 고려
  - HttpStatusCode Enum 클래스로 관리
  - JSON 파일의 HTTP_STATUS_CODE_LIST 값을 통해 HTML 타이틀 및 바디 생성

- Logback 로깅 작업 추가
  - 로그 파일을 하루 단위로 분리
  - 로그 내용에 따라 적절한 로그 레벨 적용 (error, info 분리)
  - 오류 발생 시, StackTrace 전체를 로그 파일 쓰기

- 간단한 WAS 구현
  - RequestHandler 에서 checkUpperDirectoryFileExte 함수 분리
  - HandlerMapping 에서 URL 에 따른 컨트롤러를 Dispatcher에서 생성하게끔 작성
  - Dispatcher에서 Hander에서 작성한 URL을 찾아서 Controller의 method에 따라 실행
  - Controller 인터페이스 작성 및 추상 구현체에 Content-Type 작성
  - Default, Home, Hello, Time, serivce.Hello, service.Time Controller 작성

- JUnit4 테스트 코드 작성
  - makeHtmlByCode null 조건 추가
  - HttpRequestTest Get header, method, path, version 테스트 작성
  - HttpResponseTest Redirect, forbidden, notfound, error, process, body 메서드 테스트 작성
  - HttpServerTest settingJson() 시 PORT, ROOT 정보 테스트 작성
 