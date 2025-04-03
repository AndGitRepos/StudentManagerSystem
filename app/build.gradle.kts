plugins {
    application
    id("com.diffplug.spotless") version "6.23.3"
    id("io.freefair.lombok") version "8.13.1"
    id("org.openjfx.javafxplugin") version "0.1.0"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    testImplementation("org.mockito:mockito-core:5.8.0")
    testImplementation("org.mockito:mockito-inline:5.2.0")
    implementation(libs.guava)
    implementation("com.h2database:h2:2.2.224")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

javafx {
    version = "21.0.1"
    modules = listOf("javafx.controls", "javafx.fxml")
}

application {
    mainClass.set("sms.gradle.AppLauncher")
}

spotless {
    java {
        importOrder()
        removeUnusedImports()
        palantirJavaFormat()
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.named<JavaCompile>("compileJava").configure {
    tasks.named("spotlessApply").get().mustRunAfter(this)
}

tasks.named<JavaExec>("run") {
    jvmArgs = listOf(
        "--add-opens", "java.base/java.lang=ALL-UNNAMED",
        "--add-opens", "javafx.graphics/com.sun.javafx.application=ALL-UNNAMED",
        "--add-exports", "javafx.graphics/com.sun.javafx.application=ALL-UNNAMED"
    )
}

tasks.register<Jar>("fatJar") {
    archiveBaseName.set("StudentManagementSystem")
    archiveClassifier.set("fat")

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        attributes(mapOf(
            "Main-Class" to "sms.gradle.AppLauncher",
            "Class-Path" to configurations.runtimeClasspath.get().joinToString(" ") { it.name }
        ))
    }

    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get()
            .filter { it.name.endsWith("jar") }
            .map { zipTree(it) }
    })

    exclude("META-INF/*.RSA", "META-INF/*.SF", "META-INF/*.DSA")
}