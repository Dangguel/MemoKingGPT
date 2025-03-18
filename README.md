
# 📚 메모왕 GPT (MemoKing GPT)

## ✏️ 프로젝트 소개
메모왕 GPT는 손글씨 또는 이미지 OCR 기능과  
OpenAI GPT-3.5 API를 활용한 메모 요약·정리 기능을 제공하는  
AI 기반 메모 관리 앱입니다.

---

## 🔎 주요 기능
- 폴더 & 노트 CRUD 기능
- 폴더 내 폴더 및 노트 이동 / 계층형 정리
- 이미지 OCR(한국어/영어 지원)
- GPT 요약 기능 (4가지 요약 타입)
- 요약 결과 & OCR 결과 BottomSheet 표시 및 적용
- AI 호출 2회마다 전면 광고 노출
- 첫 노트 저장 시 In-App Review 요청
- 앱 실행 시 강제 업데이트 체크 및 스토어 이동
- 노트 및 폴더 백업/복원 기능 (JSON)
- 다크모드 대응

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
domain/
 ├─ model/
 ├─ usecase/
presentation/
 ├─ ui/
 ├─ viewmodel/
 ├─ navigation/
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
