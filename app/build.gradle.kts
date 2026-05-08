plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.graalvm.native)
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotter.jvm)
    testImplementation(kotlin("test"))
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

application {
    mainClass = "org.example.AppKt"
}

// https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html
graalvmNative {
    val type = System.getProperty("os.name").lowercase().substringBefore(' ')
    val arch = System.getProperty("os.arch").lowercase().substringBefore(' ')
    binaries {
        named("main") {
            imageName.set("${project.name}-$type-$arch")
            buildArgs.add("--enable-native-access=ALL-UNNAMED")
        }
    }
}

tasks.named("test") { outputs.upToDateWhen { false } }
