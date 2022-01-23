# 基础镜像
FROM centos:7
# 维护者信息
MAINTAINER Meng Wei Jin mengweijin.work@foxmail.com

USER root

RUN yum -y install wget && \
    wget -c http://mirrors.kernel.org/fedora-epel/epel-release-latest-7.noarch.rpm && \
    rpm -ivh epel-release-latest-7.noarch.rpm && \
    yum -y update && \
    # 安装7z工具
    yum -y install p7zip* && \
    # 安装tesseract OCR
    yum -y install tesseract.x86_64 && \
    yum -y install tesseract-devel.x86_64 tesseract-osd.x86_64 && \
    # 安装语言包。中文包：chi_sim；英文/数字包：eng；方向和脚本检测包：osd
    yum -y install tesseract-langpack-chi_sim.noarch && \
    # 安装open jre
    yum -y install java-1.8.0-openjdk && \
    # 安装open jdk
    yum -y install java-1.8.0-openjdk-devel && \
    # 清理安装数据
    yum clean all && \
    # 清理安装临时目录数据
    rm -rf /tmp/*

ARG JAR_ARTIFACT_ID='mwj-cms-admin'
ARG JAR_VERSION='1.0'
ARG JAR_NAME=${JAR_ARTIFACT_ID}-${JAR_VERSION}.jar

# 设置镜像时区为东八区时区，上海
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone && mkdir /app

COPY ${JAR_NAME} /app/application.jar

RUN bash -c 'touch /app/application.jar'
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/application.jar"]
EXPOSE 80


# docker build -t registry.cn-hangzhou.aliyuncs.com/mengweijin/mwj-cms:latest .

# docker login --username=***@**.com registry.cn-hangzhou.aliyuncs.com
# docker push registry.cn-hangzhou.aliyuncs.com/mengweijin/mwj-cms:latest

# docker run --name mwj-cms -d -p 9004:80 registry.cn-hangzhou.aliyuncs.com/mengweijin/mwj-cms:latest
