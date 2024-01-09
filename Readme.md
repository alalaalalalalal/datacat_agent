# ITRM Agent
## 1. 기능
``` 이상 발생시 메일전송 ```  ``` 쉘스크립트 실행 ``` ``` ITRM 점검 메일 발송 ```
## 2. 실행 조건
1. 최대 128MB의 메모리 (안정적인 shell script 실행)
2. 점검할 스크립트에 속한 인스턴스 접근 권한 (ex. DB체크시 agent 설치된 호스트 <-> DB간 통신)
3. 한화메일 통한 전송시 호스트의 IP를 whitelist에 추가 필요(한화시스템/ICT sharedservice팀)   
4. (선택) datacat웹서버 (codecommit의 datacat repository 참조)를 통한 스크립트 등록 필요 

## 3. 제약
1. 다중 연결 (host > ssh로 다른호스트 접속 > 명령어 실행) 불가
2. 스크립트 실행 시간(~ 90초) 제한
3. nohup 명령어이외 비동기실행 불가 

***
작성자 : 한화시스템/ICT SharedService 사업개발팀 유용원
