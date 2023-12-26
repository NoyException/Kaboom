plugins {
    id("java")
}

group = "cn.noy"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:31.1-jre")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

task<JavaExec>("run") {
    mainClass.set("cn.noy.kaboom.app.KaboomLauncher")
    classpath = sourceSets["main"].runtimeClasspath
    standardInput = System.`in`
    jvmArgs("-Dfile.encoding=UTF-8")
}

tasks.jar{
    // 启用jar任务将所有依赖包含进去
    from(configurations.runtimeClasspath.get().map {
        if (it.isDirectory) it else zipTree(it)
    })
    // 排除可能导致冲突的META-INF文件
    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
    archiveFileName.set("Kaboom.jar")
    manifest {
        attributes("Main-Class" to "cn.noy.kaboom.app.KaboomLauncher")
        attributes("Class-Path" to configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }.joinToString(" "))
        charset("UTF-8")
        manifestContentCharset = "UTF-8"
    }
}