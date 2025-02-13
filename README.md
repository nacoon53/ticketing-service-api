# 마일스톤
[마일스톤](https://github.com/nacoon53/ticketing-service-api/milestones)

---
# 요구사항 분석 및 시스템 설계
# WIKI

## 요구사항 분석 및 시스템 설계
[요구사항 분석](https://github.com/nacoon53/ticketing-service-api/wiki/1.-%EC%9A%94%EA%B5%AC%EC%82%AC%ED%95%AD-%EB%B6%84%EC%84%9D)

[시퀀스 다이어그램](https://github.com/nacoon53/ticketing-service-api/wiki/2.-%EC%8B%9C%ED%80%80%EC%8A%A4-%EB%8B%A4%EC%9D%B4%EC%96%B4%EA%B7%B8%EB%9E%A8)

[ERD](https://github.com/nacoon53/ticketing-service-api/wiki/3.-ERD)

## 시스템 개선

[동시성 제어 방식 선택 과정](https://github.com/nacoon53/ticketing-service-api/wiki/5.-%EB%8F%99%EC%8B%9C%EC%84%B1-%EC%A0%9C%EC%96%B4-%EB%B0%A9%EC%8B%9D%EC%97%90-%EB%8C%80%ED%95%9C-%EA%B3%A0%EC%B0%B0)

[Redis 활용 방안](https://github.com/nacoon53/ticketing-service-api/wiki/6.-%EC%BD%98%EC%84%9C%ED%8A%B8-%EC%98%88%EC%95%BD-%EC%8B%9C%EC%8A%A4%ED%85%9C%EC%9D%98-Redis-%ED%99%9C%EC%9A%A9-%EB%B0%A9%EC%95%88)

[쿼리 수행 속도 개선(Feat. Index)](https://github.com/nacoon53/ticketing-service-api/wiki/7.-%EC%BF%BC%EB%A6%AC-%EC%88%98%ED%96%89-%EC%86%8D%EB%8F%84-%EA%B0%9C%EC%84%A0(Feat.-Index))

[트랜잭션 분리 설계(Feat. MSA 설계)](https://www.notion.so/8-Feat-MSA-1996db2e3ea180c1a02ce52888f2761f?showMoveTo=true&saveParent=true)

## API 명세서
[API 명세서 - SwaggerHub 링크](https://app.swaggerhub.com/apis-docs/nakyoungoh/concert_reservation/1.0.0#/%EC%BD%98%EC%84%9C%ED%8A%B8%20API/getAvailableSeats)
![img.png](/docs/APISpec_v1.png)


---
# 프로젝트

## Getting Started

### Prerequisites

#### Running Docker Containers

`local` profile 로 실행하기 위하여 인프라가 설정되어 있는 Docker 컨테이너를 실행해주셔야 합니다.

```bash
docker-compose up -d
```

## 패키지 구조
애플리케이션의 각 도메인(auth, concert, payment, user)을 중심으로 관련된 컨트롤러, 도메인, DTO, 레포지토리, 서비스 등 컴포넌트를 분리하여 구조화하였습니다.
```
├─auth
│  ├─controller
│  ├─domain
│  ├─dto
│  ├─repository
│  ├─service
│  └─usecase
├─concert
│  ├─controller
│  ├─domain
│  ├─dto
│  ├─repository
│  ├─service
│  └─usecase
├─config
│  └─jpa
├─payment
│  ├─controller
│  ├─domain
│  ├─dto
│  ├─repository
│  ├─service
│  └─usecase
└─user
    ├─controller
    ├─domain
    ├─dto
    ├─repository
    ├─service
    └─usecase


```
