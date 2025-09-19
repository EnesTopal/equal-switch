import java.nio.charset.StandardCharsets

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("maven-publish")
    id("signing")
}

android {
    namespace = "com.tpl.equalswitch"
    compileSdk = 36

    defaultConfig {
        minSdk = 30

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        compose = true
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
//            withJavadocJar()
        }
    }


    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)

    implementation(libs.androidx.compose.material3)

    implementation(libs.androidx.core.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "io.github.enestopal"
            artifactId = "equalswitch"
            version = "0.0.2"

            afterEvaluate {
                from(components["release"])
            }

            pom {
                name.set("EqualSwitch")
                description.set("Equal thumb-size Switch for Jetpack Compose.")
                url.set("https://github.com/EnesTopal/equal-switch")
                licenses {
                    license {
                        name.set("Apache-2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("enestopal")
                        name.set("Enes Topal")
                        url.set("https://github.com/EnesTopal/equal-switch")
                    }
                }
                scm {
                    url.set("https://github.com/EnesTopal/equal-switch")
                    connection.set("scm:git:https://github.com/EnesTopal/equal-switch")
                    developerConnection.set("scm:git:ssh://git@github.com/EnesTopal/equal-switch.git")
                }
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["release"])
}

//signing {
//    val keyFilePath = findProperty("SIGNING_KEY_FILE") as String?
//    val keyId = findProperty("SIGNING_KEYID") as String?   // opsiyonel
//    val rawKey = findProperty("SIGNING_KEY") as String?
//    val pass   = findProperty("SIGNING_PASS") as String?
//    System.out.println("Id: " +keyId)
//    System.out.println("Pass: " +pass)
//
//    System.out.println("Raw Key: " +rawKey)
//    val keya = rawKey
//        ?.toByteArray(StandardCharsets.UTF_8)
//        ?.toString(StandardCharsets.UTF_8)
//        ?.replace("\r\n", "\n")
//        ?.let { if (it.endsWith("\n")) it else it + "\n" }
//    System.out.println("\nKey: " +keya)
//
//    val key: String? = when {
//        !keyFilePath.isNullOrBlank() -> file(keyFilePath).readText(Charsets.UTF_8)
//        !rawKey.isNullOrBlank() -> rawKey
//            .replace("\\r\\n", "\n")
//            .replace("\\n", "\n")
//            .let { if (it.endsWith("\n")) it else it + "\n" }
//        else -> null
//    }
//    System.out.println("\nKey: " +key)
//
//    if (key != null && pass != null) {
//        if (keyId != null) {
//            useInMemoryPgpKeys(keyId, key, pass)
//        } else {
//            useInMemoryPgpKeys(key, pass)
//        }
//        sign(publishing.publications["release"])
//    } else {
//        logger.warn("Signing is not configured (SIGNING_KEY / SIGNING_PASS). Artifacts will be unsigned.")
//    }
//}
