plugins {
    application
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.graalvm.native)
}

repositories {
    mavenCentral()
    maven("https://central.sonatype.com/repository/maven-snapshots/") { //TODO !
        mavenContent {
            includeGroup("com.varabyte.kotter")
            snapshotsOnly()
        }
    }
}

dependencies {
    implementation(libs.kotter.jvm)
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
    binaries {
        named("main") {
            imageName.set(project.name)
            buildArgs.add("--enable-native-access=ALL-UNNAMED")
        }
    }
}
