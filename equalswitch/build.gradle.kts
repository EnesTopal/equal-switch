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

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
    from(layout.buildDirectory.dir("empty-javadoc"))
    doFirst {
        val out = layout.buildDirectory.dir("empty-javadoc").get().asFile
        if (!out.exists()) out.mkdirs()
        val stub = out.resolve("README.txt")
        if (!stub.exists()) stub.writeText("Javadoc is not provided for this artifact.")
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

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                artifact(javadocJar.get())

                groupId = project.group.toString()
                artifactId = "equalswitch"
                version = project.version.toString()

                pom {
                    name.set("equalswitch")
                    description.set("A custom Switch Composable with equal width for both states.")
                    url.set("https://github.com/EnesTopal/equal-switch")

                    licenses {
                        license {
                            name.set("The MIT License")
                            url.set("https://opensource.org/licenses/MIT")
                        }
                    }
                    developers {
                        developer {
                            id.set("enestopal")
                            name.set("Enes Topal")
                            email.set("enestopal.053@gmail.com")
                        }
                    }
                    scm {
                        connection.set("scm:git:git://github.com/EnesTopal/equal-switch.git")
                        developerConnection.set("scm:git:ssh://github.com/EnesTopal/equal-switch.git")
                        url.set("https://github.com/EnesTopal/equal-switch")
                    }
                }
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}