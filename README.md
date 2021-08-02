# springboot-rest-api
Self-Describtive Message와 HATEOAS를 만족하는 REST API입니다.

## Self-descriptive message 
* 메시지 스스로 메시지에 대한 설명이 가능해야 한다. 
* 서버가 변해서 메시지가 변해도 클라이언트는 그 메시지를 보고 해석이 가능하다. 

## HATEOAS 
* 하이퍼미디어(링크)를 통해 애플리케이션 상태 변화가 가능해야 한다. 
* 링크 정보를 동적으로 바꿀 수 있다. 


## 구현한 REST API 목록
1. 이벤트 목록 조회    
   GET /api/events 
2. 이벤트 생성    
  POST /api/events
3. 이벤트 하나 조회    
  GET /api/events/{id} 
4. 이벤트 하나 수정
  PUT /api/events/{id}
  
## 사용 기술
* 스프링 프레임워크  
* 스프링 부트  
* 스프링 데이터 JPA  
* 스프링 HATEOAS  
* 스프링 REST Docs  
* 스프링 시큐리티 OAuth2  

## Spring Boot + Rest API에 대해서 스터디한 내용입니다.
