FROM java:8
MAINTAINER Meng Wei Jin mengweijin.work@foxmail.com

ARG JAR_ARTIFACT_ID='mwj-cms-admin'
ARG JAR_VERSION='1.0'
ARG JAR_NAME=${JAR_ARTIFACT_ID}-${JAR_VERSION}.jar

# 设置镜像时区为东八区时区，上海
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone && mkdir /app

COPY target/${JAR_NAME} /app/application.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/application.jar"]
EXPOSE 80

# docker build -t registry.cn-hangzhou.aliyuncs.com/mengweijin/mwj-cms:latest .

# docker login --username=***@**.com registry.cn-hangzhou.aliyuncs.com
# docker push registry.cn-hangzhou.aliyuncs.com/mengweijin/mwj-cms:latest

# docker run --name mwj-cms -d -p 9004:80 registry.cn-hangzhou.aliyuncs.com/mengweijin/mwj-cms:latest
