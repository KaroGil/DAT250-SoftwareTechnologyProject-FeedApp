plugins {
	java
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "dat250.group22"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.hibernate:hibernate-core:7.0.0.Alpha3")
	implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
	implementation("jakarta.transaction:jakarta.transaction-api")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	annotationProcessor("org.projectlombok:lombok")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("com.h2database:h2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// Dependencies for JWT token
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("io.jsonwebtoken:jjwt:0.9.1")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
