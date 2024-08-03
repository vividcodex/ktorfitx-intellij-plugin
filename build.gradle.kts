import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	alias(libs.plugins.org.jetbrains.kotlin.jvm)
	alias(libs.plugins.intellij)
	id("java")
}

val projectVersion = property("project.version").toString()

group = "cn.vividcode.multiplatform.ktor.client.intellij.plugin"
version = projectVersion

repositories {
	mavenCentral()
}

intellij {
	version = stringProperty("intellij.version")
	type = stringProperty("intellij.type")
	
	plugins.set(listOf("org.jetbrains.kotlin"))
}

java {
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
	jvmToolchain(17)
	
	compilerOptions {
		jvmTarget = JvmTarget.JVM_17
	}
}

tasks {
	patchPluginXml {
		sinceBuild = stringProperty("patch.since")
		untilBuild = stringProperty("patch.until")
	}
	signPlugin {
		certificateChain = stringProperty("sign.certificateChain")
		privateKey = stringProperty("sign.privateKey")
		password = stringProperty("sign.password")
	}
	publishPlugin {
		token = stringProperty("publish.token")
	}
}

fun stringProperty(propertyName: String): String = property(propertyName).toString()