# DealGuard Backend

DealGuard는 중고거래 과정에서 발생할 수 있는 분쟁을 예방하기 위한 백엔드 프로젝트입니다.
상품 게시글과 거래 채팅 메시지를 저장하고, 거래 조건을 추출한 뒤 누락된 조건, 모호한 표현, 조건 변경, 게시글과 채팅 내용의 불일치, 위험 결제 패턴을 탐지합니다.
분쟁 상황에 대비할 수 있도록 분석 결과와 원본 메시지를 PDF 증거 패키지로 생성하는 기능도 제공합니다.

## 핵심 기능

- JWT 기반 회원가입, 로그인, 내 정보 조회
- 상품 게시글 등록, 조회, 수정, 삭제
- 거래 대화방 생성, 조회, 삭제
- 채팅 메시지 단건 등록, bulk 등록, transcript 파싱 등록
- rule-based 거래 조건 분석
- 누락 조건, 모호한 표현, 조건 변경, 게시글-채팅 불일치, 위험 결제 패턴 탐지
- Mock AI 분석 클라이언트
- 향후 Gemini API 연동을 위한 skeleton 구조
- PDF 증거 패키지 생성 및 다운로드
- Swagger/OpenAPI 문서 제공
- Flyway 기반 DB schema 관리

## 기술 스택

- Java 21 이상
- Spring Boot 3
- Gradle
- PostgreSQL
- Spring Web
- Spring Security
- Spring Data JPA
- JWT
- Bean Validation
- Flyway
- springdoc-openapi
- OpenPDF
- Docker Compose

## 로컬 실행 준비

필요한 프로그램:

- Java 21 이상
- Docker Desktop
- Gradle Wrapper

이 저장소에는 Gradle Wrapper가 포함되어 있으므로 별도 Gradle 설치 없이 실행할 수 있습니다.

환경변수:

```bash
JWT_SECRET=replace-with-at-least-32-byte-secret
GEMINI_API_KEY=
```

로컬 개발 환경에서는 `src/main/resources/application.yml`에 기본값이 설정되어 있어 별도 환경변수 없이도 실행할 수 있습니다.
단, 실제 배포 환경에서는 반드시 안전한 `JWT_SECRET` 값을 설정해야 합니다.

## PostgreSQL 실행

Docker Compose로 로컬 PostgreSQL을 실행합니다.

```bash
docker compose up -d
```

기본 DB 설정:

- database: `dealguard`
- username: `dealguard`
- password: `dealguard`
- port: `5432`

## 애플리케이션 실행

macOS/Linux:

```bash
./gradlew bootRun
```

Windows PowerShell:

```powershell
.\gradlew.bat bootRun
```

기본 실행 주소:

```text
http://localhost:8080
```

## 테스트 실행

macOS/Linux:

```bash
./gradlew test
```

Windows PowerShell:

```powershell
.\gradlew.bat test
```

현재 분석 로직에 대한 단위 테스트가 포함되어 있습니다.

## API 문서

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

Health Check:

```text
http://localhost:8080/actuator/health
```

## 주요 API 흐름

1. `POST /api/auth/signup`으로 회원가입
2. `POST /api/auth/login`으로 로그인
3. 응답으로 받은 `accessToken`을 Swagger 우측 상단 `Authorize` 버튼에 입력
4. `POST /api/product-posts`로 상품 게시글 생성
5. `POST /api/conversations`로 거래 대화방 생성
6. `POST /api/conversations/{conversationId}/messages/bulk`로 채팅 메시지 등록
7. `POST /api/conversations/{conversationId}/analyze`로 거래 조건 분석
8. `GET /api/conversations/{conversationId}/summary`로 분석 summary 조회
9. `GET /api/conversations/{conversationId}/alerts`로 alert 목록 조회
10. `POST /api/conversations/{conversationId}/evidence-packages`로 PDF 증거 패키지 생성
11. `GET /api/evidence-packages/{evidencePackageId}/pdf`로 PDF 다운로드

인증이 필요한 API는 아래 헤더를 사용합니다.

```http
Authorization: Bearer <accessToken>
```

Swagger UI에서는 우측 상단 `Authorize` 버튼을 누른 뒤 `accessToken` 값만 붙여 넣으면 됩니다.
`Bearer` 문구는 Swagger가 자동으로 붙입니다.

로그인과 회원가입 응답에는 `accessToken`과 `refreshToken`이 함께 내려옵니다.
access token을 새로 발급받고 싶을 때는 아래 API를 사용합니다.

```http
POST /api/auth/refresh
```

```json
{
  "refreshToken": "<refreshToken>"
}
```

## 공통 응답 형식

성공 응답:

```json
{
  "success": true,
  "data": {},
  "message": null
}
```

에러 응답:

```json
{
  "success": false,
  "data": null,
  "message": "error message"
}
```

주요 에러 상태:

- `400 Bad Request`: 요청 값 검증 실패, JSON 형식 오류, 타입 오류
- `401 Unauthorized`: access token 누락, 만료, 유효하지 않은 토큰
- `403 Forbidden`: 인증은 되었지만 접근 권한이 없는 요청
- `404 Not Found`: 요청한 리소스를 찾을 수 없음
- `409 Conflict`: 중복 데이터 또는 DB 제약 조건 위반
- `415 Unsupported Media Type`: 지원하지 않는 Content-Type
- `500 Internal Server Error`: 서버 내부 오류

## 폴더 구조

```text
src/main/java/com/dealguard
  ai              # AI 분석 클라이언트 인터페이스 및 구현체
  analysis        # 거래 조건 추출 및 위험 탐지 로직
  auth            # JWT 인증, 로그인, 회원가입
  conversation    # 거래 대화방
  evidence        # PDF 증거 패키지
  global          # 공통 응답, 예외 처리, 설정
  message         # 채팅 메시지
  product         # 상품 게시글
  user            # 사용자

src/main/resources
  db/migration    # Flyway migration

src/test/java/com/dealguard
  analysis        # 분석 로직 단위 테스트

docs              # 프로젝트 기획 및 설계 문서
```

## 분석 MVP 범위

현재 분석은 외부 AI API 없이 동작하는 rule-based 방식입니다.

지원하는 분석 예시:

- `10만원`, `100,000원`, `5만 5천원`, `무료나눔` 가격 추출
- `직거래`, `택배`, `반값택배`, `퀵` 거래 방식 추출
- `계좌`, `이체`, `안전결제`, `현금` 결제 방식 추출
- `환불 불가`, `환불 가능` 등 환불 정책 추출
- `거의 새거`, `상태 좋아요`, `문제 없어요` 등 모호한 표현 탐지
- `선입금`, `계좌 먼저`, `송장 나중에` 등 위험 결제 패턴 탐지
- 게시글 가격과 채팅 가격 불일치 탐지
- 이전 분석 snapshot과 새 분석 snapshot의 조건 변경 탐지

## AI 연동 방향

현재 기본 설정은 mock 분석입니다.

```yaml
ai:
  provider: mock
```

Gemini 연동을 위한 `GeminiAiAnalysisClient` skeleton은 준비되어 있지만, 실제 운영 분석에는 아직 사용하지 않습니다.
향후 `AiAnalysisClient` 인터페이스 뒤에서 structured JSON 응답 검증을 추가하는 방식으로 확장할 수 있습니다.

## 문서

프로젝트 기획 및 설계 문서는 `/docs` 아래에 정리되어 있습니다.

- `docs/01-problem-and-usp.md`
- `docs/02-service-requirements.md`
- `docs/03-domain-model.md`
- `docs/04-ai-analysis-design.md`
- `docs/05-api-spec-draft.md`
- `docs/06-evidence-package-design.md`
- `docs/07-roadmap-and-presentation.md`

## 주의사항

- 프론트엔드는 이 저장소에서 구현하지 않습니다.
- 실제 중고거래 플랫폼 크롤링은 하지 않습니다.
- private/external platform data를 스크래핑하지 않습니다.
- secret 값은 코드에 하드코딩하지 않습니다.
- 로컬 테스트는 dummy data를 사용합니다.
