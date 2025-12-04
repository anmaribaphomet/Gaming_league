plugins {
    id("java")
    id("application")
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://jaspersoft.jfrog.io/jaspersoft/third-party-ce-artifacts/") }
dependencies {
    // DRIVER POSTGRESQL JDBC
    implementation("org.postgresql:postgresql:42.3.8")
    implementation("net.sourceforge.dynamicreports:dynamicreports-core:6.12.1")
    implementation("net.sf.jasperreports:jasperreports:6.20.6")
    implementation("net.sf.jasperreports:jasperreports-fonts:6.20.6")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("org.glassfish.jaxb:jaxb-runtime:2.3.1")
}

tasks.test {
    useJUnitPlatform()
}

// CLASE PRINCIPAL DEL PROYECTO
application {
    mainClass.set("com.example.Main")
}
}
