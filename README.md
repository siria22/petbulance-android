### **Siria22's Android Template**

**Last Update:** 2025-12-05

---

## 1. 프로젝트 구조 (Clean Architecture)

```
root
 ├─ app            # EntryPoint / DI / Navigation
 ├─ data           # API, DB, Repository 구현
 ├─ domain         # UseCase, Business Logic (Pure Kotlin)
 └─ presentation   # UI, ViewModel, Compose Screen (MVI)
```

---

## 2. Presentation Layer 구조 (MVI 기반)

| 파일                        | 역할                                      |
|---------------------------|-----------------------------------------|
| **${NAME}Argument.kt**    | `State`, `Intent`, `Event` 정의           |
| **${NAME}ViewModel.kt**   | Intent 수신 → 비즈니스 처리 → State 변경/Event 발생 |
| **${NAME}Data.kt**        | UI 전용 데이터 (List, Paging 등)              |
| **${NAME}Screen.kt**      | UI 렌더링, 사용자 이벤트 → ViewModel 전달          |
| **${NAME}Destination.kt** | Navigation Graph 연결, DI 주입 구성           |

### MVI 요소 설명

* **State**: UI가 표현해야 하는 상태. Data와 Screen 단위로 나누어둠.   
  예) `Init`, `Loading`
* **Intent**: 사용자 입력/이벤트
* **Event**: 일회성 처리. Custom으로 지정하지 않을 시 공용 이벤트 핸들러가 처리함  
  예) `DataFetch.Error`

---

## 3. Libs & Dependencies

> 모든 버전 및 라이브러리 관리는 `gradle/libs.versions.toml`에서 통합 관리

### Android & Jetpack Core

| 라이브러리            | 버전     | 모듈                    | 설명                         |
| ---------------- | ------ | --------------------- | -------------------------- |
| Core KTX         | 1.17.0 | app,data,presentation | Kotlin 확장 API              |
| Material         | 1.13.0 | app,presentation      | 머티리얼 디자인 컴포넌트              |
| Lifecycle        | 2.10.0 | app,presentation      | ViewModel & LifecycleOwner |
| Activity Compose | 1.12.0 | app                   | Compose Activity 지원        |

### Jetpack Compose

| 라이브러리              | 버전         | 모듈               | 설명                 |
| ------------------ | ---------- | ---------------- | ------------------ |
| Compose BOM        | 2025.11.01 | app,presentation | Compose 버전 정합성 관리  |
| Material 3         | 1.4.0      | app,presentation | 최신 Material3 UI    |
| Navigation Compose | 2.9.6      | app,presentation | Compose Navigation |
| Coil               | 2.7.0      | presentation     | 이미지 로드             |
| Accompanist        | 0.37.3     | presentation     | 권한/시스템 UI 보완       |

### DI - Hilt

| 라이브러리           | 버전     | 모듈                    | 설명                        |
| --------------- | ------ | --------------------- | ------------------------- |
| Hilt Android    | 2.57.2 | app,data,presentation | DI 생성/주입 관리               |
| Hilt Navigation | 1.3.0  | presentation          | Navigation + ViewModel 주입 |
| Hilt Core       | 2.57.2 | domain                | KMP 환경 Core               |

### Data / Networking

| 라이브러리           | 버전    | 모듈   | 설명                  |
| --------------- | ----- | ---- | ------------------- |
| Ktor            | 3.3.3 | data | API 통신              |
| Room            | 2.8.4 | data | Local DB ORM        |
| DataStore       | 1.2.0 | data | 저장소 (SharedPref 대체) |
| Security Crypto | 1.1.0 | data | 암호화 SharedPref 지원   |

### Test

| 라이브러리               | 버전     | 모듈       |
| ------------------- | ------ | -------- |
| JUnit               | 4.13.2 | all      |
| Espresso            | 3.7.0  | app,data |
| Compose UI Test BOM | -      | app      |

---

## 4. TODO Checklist

* [ ] **Manifest 검토**

    * 권한 `<uses-permission>`
    * API Key/metadata 확인
* [ ] **프로젝트명 Refactor**

    * `namespace`, `Theme`, `ColorScheme`, `strings.xml(app_name)`
    * `settings.gradle.kts`의 `rootProject.name` 수정
* [ ] **libs.versions.toml 의존성 버전 점검 및 업데이트**
* [ ] **보안 로직 구현**

    * `AppKeyProvider`, `CryptoManager` 설계/구현
