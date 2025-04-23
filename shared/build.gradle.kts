import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    id("maven-publish")
    id("com.vanniktech.maven.publish") version "0.30.0"
}

group = "io.github.zaiddev1996"
version = "1.0.0"

kotlin {
    androidTarget {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_1_8)
                }
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.ui)
            implementation(compose.material3)
            implementation(compose.components.resources)
            //put your multiplatform dependencies here
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.zaid.kmm_tooltip"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}


mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates(group.toString(), "kmm-material-tooltip", version.toString())

    pom {
        name = "KMM Material Tooltip"
        description = "KMM Material Tooltip"
        url = "https://github.com/zaiddev1996/kmm-material-tooltip/"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "zaiddev1996"
                name = "Zaid Abbasi"
                url = "https://github.com/zaiddev1996/"
            }
        }
        scm {
            url = "https://github.com/zaiddev1996/kmm-material-tooltip/"
            connection = "scm:git:git://github.com/zaiddev1996/kmm-material-tooltip.git"
            developerConnection = "scm:git:ssh://git@github.com/zaiddev1996/kmm-material-tooltip.git"
        }
    }
}

//signing {
//    val signingKey: String? = project.findProperty("signing.key") as String? ?: System.getenv("SIGNING_KEY")
//    val signingPassword: String? = project.findProperty("signing.password") as String? ?: System.getenv("SIGNING_PASSWORD")
//
//    useInMemoryPgpKeys(signingKey, signingPassword)
//    sign(publishing.publications["mavenJava"])
//}