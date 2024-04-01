import io.izzel.taboolib.gradle.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.21"
    id("io.izzel.taboolib") version "2.0.11"

}

group = "org.example"
version = "1.0-SNAPSHOT"

val taboolibVersion: String by project

repositories {
    mavenLocal ()
    mavenCentral()
    //taboolib
    maven {
        url = uri("https://repo.tabooproject.org/repository/releases/")
        isAllowInsecureProtocol = true
    }
    //物品库
    maven {
        url = uri("https://r.irepo.space/maven/")
        isAllowInsecureProtocol = true
    }
    //papi
    maven {
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }

}

taboolib {
    env {
        install(
            BUKKIT_ALL,
            UNIVERSAL,
            UI,
            NMS,
            LANG,
            CONFIGURATION,
            CHAT
        )
    }

    description {
        contributors {
            name("facered")
        }
        dependencies {
            bukkitApi("1.13")
            name("PlaceholderAPI").optional(true)
            name("MythicMobs").optional(true)
            name("NeigeItems").optional (true)
            name ("Vault").optional (true)
        }

    }

    // relocate("com.google.gson", "com.google.gson2_9_1")
    classifier = null
//    version = taboolibVersion
}


dependencies {
    compileOnly("ink.ptms.core:v12002:12002:mapped")
    compileOnly("ink.ptms.core:v12002:12002:universal")
    compileOnly (fileTree(baseDir = "src/libs"))
    //papi 插件
    compileOnly("me.clip:placeholderapi:2.10.9") { isTransitive = false }
    implementation(kotlin("stdlib-jdk8"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}


tasks.withType<Jar> ().configureEach {
    destinationDirectory.set(File ("F:\\mc\\mcPaperServer\\plugins"))
}