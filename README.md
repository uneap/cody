# cody architecture

![아키텍처](https://github.com/uneap/cody/assets/25525648/5670b4ba-6dea-46e4-b371-e0a59bc77a6c)

# 구동 방법 - intelliJ 기준

## 1. 인프라 실행

### h2

```bash
# https://h2database.com/html/main.html
# All Platforms 설치

# 압축 푼 후, 해당 파일이 저장된 위치로 이동
$ cd ~/h2

# 실행
$ .bin/h2.sh
```
![image](https://github.com/uneap/cody/assets/25525648/c96a8e50-ed31-4d92-ae51-2e2ceb59662c)

ui 창 접속 시 해당 프로젝트의내부 test.mv.db 경로 입력

### kafka

```java
# 프로젝트 내부의 docker-compose.yml가 실행될 수 있도록 프로젝트에서 실행
# 백그라운드로 실행
$ docker compose up -d
```

### redis

```java
# format
$ docker build -t <컨테이너 이미지 이름> .

# 컨테이너 이미지 생성
$ docker build . -t simple-redis

# 컨테이너 생성 및 실행
$ docker run -d --name simple-redis -p 6379:6379 simple-redis
```
## 2. 
## 사용 기술 및 고민 과정

Spring Boot 3.2.3, Java 17, Kafka 3.4, H2, Redis

- DB 부하가 생겨 서버가 다운되지 않게 높은 트래픽을 감당할 수 있는지
    - Kafka, Redis
- 유지보수가 쉽고, 코드 재사용성을 높일 수 있는지
    - 멀티모듈, MSA
- 코드 역할에 대한 보충 설명을 어떻게 할지
    - 테스트 코드를 통해 문서화 (시간이 부족하여 모든 기능에 테스트 코드를 붙이지 못한 점 양해 부탁드립니다.)
## DB 스키마


## branch

- main: 메인 브랜치
- feature: 기능 관련 작업
- chore: 설정 변경
- test: 테스트 추가

## 모듈 구조
- backend: 애플리케이션 비지니스를 가지고 있으며, 모든 모듈을 조합하는 최상위 모듈
    - display: 프론트에 전송될 데이터를 가공하는 모듈
    - full-cache-batch: storage에서 누락된 데이터에 대응하기 위해 전체 DB 데이터를 캐싱하는 어플리케이션
    - storage: DB에 데이터 저장하고 카프카로 데이터 전송하는 어플리케이션
    - cache: 카프카 컨슘 어플리케이션, 컨슘 실패 시 dlt 토픽에 전송해서 재처리 수행
- data: H2 DB에 저장된 데이터를 보관하는 모듈
- domain: 데이터를 직접 처리하며, 비지니스 로직이 담기지 않은 독립적인 모듈
    - store: DB에 데이터 처리하는 작업 수행
    - store-cache: redis에 데이터 처리하는 작업 수행
- frontend: 프론트엔드 관련 모듈
- resource: 인프라 연결을 위한 설정 모듈

### 패키지 구조
- config: 프로젝트 구성 설정
- constant: 공동으로 사용할 상수 값
- steps: chunk 단위 배치 잡을 구성하는 step
- api: controller 위치함
- request: API 요청 DTO
- response: API 응답 DTO
- producer: kafka producer 구성됨
- consumer: kafka consumer 구성됨
- service: 
- util:
- db: DB 데이터 처리하는 로직과, DAO를 구성
- dto:
