# 사용할 기본 이미지 지정
FROM openjdk:17-jdk-alpine

# 애플리케이션 파일을 컨테이너 내부로 복사
COPY build/libs/climingo-api-0.0.1-SNAPSHOT.jar app.jar

ENV JASYPT_PASSWORD climingo-will-be-nice
# 컨테이너가 시작될 때 실행될 명령어
ENTRYPOINT ["java","-jar","/app.jar"]