# 1. Java 17 경량 이미지 사용
FROM bellsoft/liberica-openjdk-alpine:17

# 2. 한국 시간대 설정
ENV TZ=Asia/Seoul
RUN apk add --no-cache tzdata \
    && cp /usr/share/zoneinfo/${TZ} /etc/localtime \
    && echo "${TZ}" > /etc/timezone

# 3. JAR 파일 복사 (Gradle 빌드 결과)
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 4. 앱 실행 (시간대 반영)
ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "/app.jar"]
