# Petbulance (펫뷸런스) Android

특수동물(개/고양이 외의 반려동물)을 진료하는 병원을 찾고, 영수증으로 방문을 인증한 후기를 남기고, 보호자끼리 정보를 나누는 Android 앱입니다.

<!-- TODO: 스크린샷 -->

- 8명이 참여한 팀 프로젝트 Petbulance의 Android 앱입니다. Android는 1명이 전담했습니다.
- 원본 저장소: [Petbulance/Petbulance-android-rework](https://github.com/Petbulance/Petbulance-android-rework). 이 저장소는 원본을 개인 계정으로 옮긴 사본입니다.
- Google Play에 출시했고, 현재는 서버 종료로 운영이 중단되었습니다. <!-- TODO(확인): 출시 시기, Play에 배포된 마지막 버전 -->

## 프로젝트 정보

| 항목 | 내용 |
|---|---|
| 기간 | 2025.09 ~ 2026.05 |
| 팀 | PM 2, 디자인 1, Android 1, Web 2, Backend 2 |
| 담당 | Android 앱 전체: 구조 설계, 전 화면 구현, CI/디자인 토큰 파이프라인 구성.|
| 규모 | 화면 34개, UseCase 92개, Kotlin 파일 704개 |
| 버전 | v1.2 (versionCode 13), minSdk 26 / targetSdk 36 |

## 주요 기능

| 영역 | 기능 |
|---|---|
| 병원 검색 | 동물 종류 선택, 네이버 지도에서 위치보기 vs 리스트 보기 전환, 지역/진료 중/동물종 별 필터, 현재 위치 기준 검색, 병원 상세(전화 연결·길 찾기·공유) |
| 영수증 인증 후기 | 영수증 촬영 → 서버가 분석한 병원,방문 일시,금액,진료 항목으로 후기 작성. 수정/삭제/검색/신고 기능 포함 |
| 커뮤니티 | 게시글 목록 및 필터/검색, 글 작성/수정, 댓글, 좋아요, 게시글/댓글 신고 |
| 알림 | 공지사항/내 활동 탭, 전체 읽음/삭제, 알림을 누르면 해당 게시글/후기로 이동, 알림 설정 |
| 계정 | Google/Kakao/Naver 로그인, 약관 동의, 계정 연동/해제, 회원 탈퇴, 비로그인 둘러보기 |
| 마이페이지 | 프로필, 내 후기/게시글/댓글 관리, 공지사항, 1:1 문의, 광고/제휴 문의, 약관, 권한 설정 |

## 기술적 판단

### 1. 다시 만들기(리워크)

첫 버전은 팀 조직 저장소 `Petbulance/Petbulance-Android`에서 2025.10 ~ 11에 개발했습니다(커밋 15개). 
그러나 초반에 프로젝트 구조를 잘못 잡고 들어가는 바람에 기술 부채가 감당할 수 없을 규모로 쌓여, 2025.12에 이 저장소에서 구조를 새로 잡고 다시 시작했습니다. 

### 2. 4-모듈 Clean Architecture

```
app -> presentation -> domain <- data
```

| 모듈 | 역할 |
|---|---|
| `app` | 진입점, Hilt 설정, 내비게이션 그래프 |
| `presentation` | Compose UI, ViewModel(MVI), Analytics |
| `domain` | UseCase, Repository 인터페이스, 모델 |
| `data` | Ktor API, Room, DataStore, Repository 구현, DTO ↔ 모델 Mapper |

`domain`은 Android 라이브러리 모듈 대신 Kotlin/JVM 모듈(`kotlin("jvm")`)로 만들었습니다. (현재 `domain`의 `android.*` import는 0건)

### 3. MVI와 화면 단위 파일 규칙

화면마다 다섯 파일을 둡니다.

| 파일 | 역할 |
|---|---|
| `{Name}Argument.kt` | State, Intent, Event 정의 |
| `{Name}ViewModel.kt` | Intent 처리 -> State 갱신, Event 발행 |
| `{Name}Data.kt` | UI 전용 데이터(Paging 등) |
| `{Name}Screen.kt` | State를 그리고 사용자 입력을 Intent로 전달 |
| `{Name}Destination.kt` | 내비게이션 등록, `hiltViewModel()` 주입 |

- State는 화면당 data class 하나를 `StateFlow`로 노출하고, 토스트·화면 이동 같은 일회성 동작은 `SharedFlow` Event로 보냅니다.
- 섹션별 로딩은 `SectionLoadState`(Init / Loading / Success / Error)로, 공통 에러 다이얼로그는 `BaseViewModel`과 `CommonScreenWrapper`가 처리합니다.

### 4. 네트워크와 인증

- Ktor `Auth` 플러그인으로 요청에 Bearer 토큰을 붙이고, 401 응답을 받으면 refresh token으로 토큰을 갱신한 뒤 요청을 다시 보냅니다. 로그인/토큰 갱신 경로에는 토큰을 미리 붙이지 않습니다.
- 모든 API 호출은 `safeApiCall`로 `Result<T>`에 담고, HTTP 상태를 domain 예외로 바꿉니다. UI 레이어에서는 필요한 데이터(혹은 오류 정보)만 전달받습니다.
- 토큰은 Android Keystore의 AES 키(CBC / PKCS7)로 암호화해 DataStore에 저장합니다.

### 5. Mock Repository 및 데이터

domain의 Repository 인터페이스 18개에 실제 구현(`*RepositoryImpl`)과 Mock 구현(`Mock*Repository`)을 함께 두고, Hilt `@Binds`에서 주입할 쪽을 고릅니다. 어느 쪽을 쓰든 UseCase와 ViewModel 코드는 그대로입니다.

현재는 서버가 내려간 상태라, 화면을 채워넣을 용도로 MockRepository가 사용되고 있습니다.

### 6. 디자인 토큰에서 색상 코드 생성

- 디자이너가 Figma(Tokens Studio)에서 관리하는 색상 토큰을 `tokens/tokens.json`으로 받습니다.
- Gradle 태스크 `generateDesignTokens`가 이 파일을 읽어 KotlinPoet로 Compose `Color` 객체(`PetbulancePrimitives`)를 생성합니다. 코드에서는 `PetbulancePrimitives.Gray.p50`처럼 씁니다.
- `figma-tokens-update` 브랜치에 토큰 변경이 올라오면 `main`으로 PR을 자동 생성하는 워크플로우(`design-create_pr.yml`)를 두었습니다.

### 7. CI

키 값은 Github Actions를 통해 push나 PR마다 주입되도록 하였습니다.
프로젝트 내에 존재하는 나머지 정보들은 공개되어도 문제가 없거나 숨길 수 없는 정보들입니다.

### 그 밖에

- Analytics: PM이 정의한 GA4 이벤트 11개(우선순위 P0 5개, P1 4개, P2 2개), NavController 리스너로 화면 조회 자동 기록, Crashlytics, Meta App Events.
- 비로그인 사용자: 커뮤니티는 3페이지까지 보여주고, 후기 상세는 로그인 안내 다이얼로그를 띄웁니다.

## 기술 스택

| 분류 | 사용 기술 |
|---|---|
| 언어·UI | Kotlin 2.2, Jetpack Compose (Material 3), Navigation Compose, Paging 3 |
| 구조·DI | Clean Architecture, MVI, Hilt (KSP) |
| 데이터 | Ktor Client 3 (CIO), kotlinx.serialization, Room, DataStore, WorkManager |
| 인증·보안 | Firebase Auth, Google Identity (AuthorizationClient), Kakao SDK, Naver Login SDK, Android Keystore |
| 지도·카메라·이미지 | Naver Map SDK, Play Services Location, CameraX, Coil |
| 분석 | Firebase Analytics·Crashlytics, Meta SDK |
| 빌드·CI | Gradle Version Catalog, KotlinPoet, GitHub Actions |


## 주요 연혁

| 시기 | 내용 |
|---|---|
| 2025.09 | 팀 프로젝트 시작 |
| 2025.09 ~ 10 | 필요 기술 조사 및 테스트 |
| 2025.10 ~ 11 | 첫 버전 개발 (`Petbulance/Petbulance-Android`) |
| 2025.12 | 이 저장소에서 다시 시작 |
| 2026.04 | v1.0.0, Analytics·Crashlytics 적용 |
| 2026.05 | v1.2 (versionCode 13), 개발 마무리 및 플레이스토어 배포 후 지표 수집 | 
| 이후 | 서버 종료로 운영 중단 |
