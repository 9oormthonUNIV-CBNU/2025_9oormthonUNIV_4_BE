📦 MUSTEP Backend

MUSTEP의 백엔드 레포지토리입니다. 기업 연계 프로젝트 기반의 팀 협업 플랫폼으로, Spring Boot 기반으로 구축되었으며, GitHub Actions + Docker + EC2를 통한 CI/CD, AWS RDS 및 S3를 사용합니다.

⸻

🏗️ 기술 스택
	•	Java 21 / Spring Boot 3.4.5
	•	Gradle
	•	Spring Security + OAuth2 (Google Login)
	•	JPA (Hibernate)
	•	MySQL / AWS RDS
	•	AWS S3 (파일 업로드)
	•	Docker / Docker Hub / GitHub Actions
	•	AWS EC2

⸻

⚙️ 프로젝트 구조

📦 backend
 ┣ 📂 domain
 ┃ ┣ 📂 team, user, project, notify ...  // 도메인 별 계층
 ┃ ┗ 📂 s3, auth                         // 외부 연동 or 인증 모듈
 ┣ 📂 global                             // 공통 예외 처리, 공통 응답 등
 ┣ 📜 application.yml                   // 환경 설정
 ┗ 🐳 Dockerfile


⸻

☁️ 아키텍처
![image](https://github.com/user-attachments/assets/97bc90c8-f779-4b06-9a30-878de82717d7)


⸻

🔐 인증 & 보안
	•	Google OAuth2를 사용하여 사용자 인증
	•	JWT를 이용한 세션 관리
	•	리프레시 토큰 로직 준비 중 (추후 업데이트 예정)

⸻

📌 API 명세

👉 Swagger UI 바로가기 (http://localhost:8080/swagger-ui/index.html)

⸻

✨ 개발자



⸻
