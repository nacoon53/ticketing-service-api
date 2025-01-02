# 마일스톤
[마일스톤](https://github.com/nacoon53/ticketing-service-api/milestones)

---
# 요구사항 분석 및 시스템 설계
## WIKI

[요구사항 분석](https://github.com/nacoon53/ticketing-service-api/wiki/1.-%EC%9A%94%EA%B5%AC%EC%82%AC%ED%95%AD-%EB%B6%84%EC%84%9D)

[시퀀스 다이어그램](https://github.com/nacoon53/ticketing-service-api/wiki/2.-%EC%8B%9C%ED%80%80%EC%8A%A4-%EB%8B%A4%EC%9D%B4%EC%96%B4%EA%B7%B8%EB%9E%A8)

[ERD](https://github.com/nacoon53/ticketing-service-api/wiki/3.-ERD)

[API 명세서](https://github.com/nacoon53/ticketing-service-api/wiki/4.-API-%EB%AA%85%EC%84%B8%EC%84%9C)


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
