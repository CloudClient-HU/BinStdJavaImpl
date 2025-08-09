plugins {
    id("java")
}

repositories {
}

dependencies {
}

tasks {
    jar {
        from("LICENSE") {
            rename { "${it}_${project.base.archivesName.get()}" }
        }
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    withType<JavaCompile> {
        options.release = 17
    }
}
