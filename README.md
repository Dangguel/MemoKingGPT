![memoking_gpt_logo](https://github.com/user-attachments/assets/d92eae31-026e-4e1e-a0bc-33fb586582c3)
📚 메모왕 GPT (MemoKing GPT)

![메모왕 GPT](./images/image1.jpeg)

## ✏️ 프로젝트 소개
메모왕 GPT는 손글씨 또는 이미지 OCR 기능과  
OpenAI GPT-3.5 API를 활용한 메모 요약·정리 기능을 제공하는  
AI 기반 메모 관리 앱입니다.

---

## 🔎 주요 기능
- 폴더 & 노트 CRUD 기능
- 폴더 내 폴더 및 노트 이동 / 계층형 정리
- 이미지 OCR(한국어/영어 지원)
- GPT 요약 기능 (핵심 정리 / 전체 요약 / 목록 변환 / 액션 플랜)
- 요약 결과 및 OCR 결과 BottomSheet 표시 및 적용
- AI 호출 2회마다 전면 광고 노출
- 첫 노트 저장 시 In-App Review 요청
- 앱 실행 시 강제 업데이트 체크 및 스토어 이동
- 다크모드 대응

---

## 🖼️ 스크린샷

| 폴더 관리 화면 | 폴더 내 노트 보기 | 노트 작성 화면 |
|---|---|---|
| <img src="https://github.com/user-attachments/assets/8d373cd5-93aa-4418-aec7-0a019be94dcf" width="300px"> | <img src="https://github.com/user-attachments/assets/ca092d4e-1870-4afc-8c4a-3bb3006e987c" width="300px"> | <img src="https://github.com/user-attachments/assets/c49240ee-f47d-4c50-838c-cd9123a89dce" width="300px"> |

| 이미지 OCR 팝업 | 요약 타입 선택 화면 |
|---|---|
| <img src="https://github.com/user-attachments/assets/03f1781e-59b9-4734-bf3f-b900473c57ed" width="300px"> | <img src="https://github.com/user-attachments/assets/ec924395-ee21-4594-a6d0-d75206a989ec" width="300px"> |


---

## 🔧 기술 스택
- Kotlin
- Jetpack Compose
- MVVM + Clean Architecture
- Room DB
- Google ML Kit (OCR)
- OpenAI GPT-3.5 API (Retrofit2)
- Hilt (DI)
- DataStore / SharedPreferences
- Google AdMob (배너, 전면광고)
- In-App Review API
- Play Core Library (앱 업데이트 감지)

---

## 📁 프로젝트 구조
```
data/
 ├─ local/
 ├─ remote/
 ├─ repository/
di/
domain/
 ├─ model/
 ├─ repository/
 ├─ usecase/
presentation/
 ├─ ads/
 ├─ ui/
 ├─ viewmodel/
util/
```

---

## ✅ 광고 정책
- 하단 배너 광고 상시 노출
- GPT 요약 기능 2회 호출 시 전면 광고 1회 노출
- 하루 최초 앱 실행 시 전면 광고 1회 노출

---

## 💡 향후 개선 예정
- 구글 드라이브 백업 지원
- GPT-4 API 전환
- Pro 버전 결제 시스템 추가
- GPT 프롬프트 세부 커스터마이징 기능 추가

---

## 📲 다운로드
<a href="https://play.google.com/store/apps/details?id=kr.co.dangguel.memokinggpt" target="_blank">
  <img src="https://play.google.com/intl/en_us/badges/images/generic/ko_badge_web_generic.png" height="60">
</a>

