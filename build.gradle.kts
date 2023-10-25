plugins {
	java
	id("org.springframework.boot") version "2.7.17"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_11
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:2021.0.5")
		mavenBom("org.springframework.cloud:spring-cloud-sleuth-otel-dependencies:1.1.2")
		mavenBom("io.opentelemetry:opentelemetry-bom:1.31.0")

	}
}
repositories {
	mavenCentral()
}
configurations {
	all {
		exclude("org.springframework.boot", "spring-boot-starter-logging")
	}
}
dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-log4j2")
	implementation("org.springframework.cloud:spring-cloud-starter-sleuth") {
		exclude("org.springframework.cloud", "spring-cloud-sleuth-brave")
	}
	implementation("org.apache.logging.log4j:log4j-layout-template-json:2.17.2")

	implementation("org.springframework.cloud:spring-cloud-sleuth-otel-autoconfigure")
	implementation("io.opentelemetry:opentelemetry-exporter-otlp")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
