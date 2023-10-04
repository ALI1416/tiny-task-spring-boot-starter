# Tiny Task SpringBoot Starter 轻量级集群任务SpringBoot启动器

[![License](https://img.shields.io/github/license/ALI1416/tiny-task-spring-boot-starter?label=License)](https://www.apache.org/licenses/LICENSE-2.0.txt)
[![Java Support](https://img.shields.io/badge/Java-8+-green)](https://openjdk.org/)
[![Maven Central](https://img.shields.io/maven-central/v/cn.404z/tiny-task-spring-boot-starter?label=Maven%20Central)](https://mvnrepository.com/artifact/cn.404z/tiny-task-spring-boot-starter)
[![Tag](https://img.shields.io/github/v/tag/ALI1416/tiny-task-spring-boot-starter?label=Tag)](https://github.com/ALI1416/tiny-task-spring-boot-starter/tags)
[![Repo Size](https://img.shields.io/github/repo-size/ALI1416/tiny-task-spring-boot-starter?label=Repo%20Size&color=success)](https://github.com/ALI1416/tiny-task-spring-boot-starter/archive/refs/heads/master.zip)

[![Java CI](https://github.com/ALI1416/tiny-task-spring-boot-starter/actions/workflows/ci.yml/badge.svg)](https://github.com/ALI1416/tiny-task-spring-boot-starter/actions/workflows/ci.yml)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=ALI1416_tiny-task-spring-boot-starter&metric=coverage)
![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=ALI1416_tiny-task-spring-boot-starter&metric=reliability_rating)
![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=ALI1416_tiny-task-spring-boot-starter&metric=sqale_rating)
![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=ALI1416_tiny-task-spring-boot-starter&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=ALI1416_tiny-task-spring-boot-starter)

## 简介

轻量级集群任务SpringBoot实现，使用Redis、RabbitMQ等技术

使用方法请看[项目示例](./test/tiny-task-spring-boot-starter-test)

## 依赖导入

```xml
<dependency>
  <groupId>cn.404z</groupId>
  <artifactId>tiny-task-spring-boot-starter</artifactId>
  <version>1.0.0</version>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter</artifactId>
  <version>2.7.15</version>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-redis</artifactId>
  <version>2.7.15</version>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-amqp</artifactId>
  <version>2.7.15</version>
</dependency>
<dependency>
  <groupId>org.projectlombok</groupId>
  <artifactId>lombok</artifactId>
  <version>1.18.28</version>
  <optional>true</optional>
</dependency>
```

## 更新日志

[点击查看](./CHANGELOG.md)

## 关于

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="https://www.404z.cn/images/about.dark.svg">
  <img alt="About" src="https://www.404z.cn/images/about.light.svg">
</picture>
