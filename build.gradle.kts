plugins {
    java
    application
}

group = "sk.tuke.kpi.oop"
version = "1.0"

val gamelibVersion = "2.6.0"

val backend = if (System.getProperty("os.name").contains("mac", ignoreCase = true)) "lwjgl2" else "lwjgl"

repositories {
    mavenCentral()
    maven(url=uri("https://repo.kpi.fei.tuke.sk/repository/maven-public"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
}
tasks.register<Copy>("copyCompileLibs") {
    from(configurations.compileClasspath)
    into("$projectDir/lib")
}
application {
//    mainClassName = "sk.tuke.kpi.gamelib.framework.Main"
    mainClassName = "sk.tuke.kpi.oop.game.Main"
}
dependencies {
    compileOnly("com.badlogicgames.gdx:gdx:1.12.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("sk.tuke.kpi.gamelib:gamelib-framework:$gamelibVersion")
    implementation("sk.tuke.kpi.gamelib:gamelib-backend-$backend:$gamelibVersion")
}

tasks {
    withType<JavaCompile> {
        options.compilerArgs.plusAssign("-parameters")
        options.compilerArgs.addAll(listOf("-Xlint:all", "-Werror"))
    }
}
