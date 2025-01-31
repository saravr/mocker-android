
plugins {
    id("com.android.library")
    kotlin("android")
    //id("com.google.devtools.ksp")
    id("kotlin-kapt")
    kotlin("plugin.serialization")
    id("dagger.hilt.android.plugin")
    id("maven-publish")
}

configurations.all {
    resolutionStrategy {
        eachDependency {
            if (requested.group == "org.hamcrest") {
                useVersion("2.2")
            }
        }
    }
}

android {
    namespace = "com.sandymist.android.mocker.apimock"
    compileSdk = 34

    defaultConfig {
        minSdk = 25

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = false
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro", "consumer-rules.pro"))

        }

        named("debug") {
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        //jvmTarget = JavaVersion.VERSION_17.toString()
        allWarningsAsErrors = true
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.4"
    }

    packaging {
        resources {
            excludes += listOf("/META-INF/{AL2.0,LGPL2.1}", "META-INF/DEPENDENCIES")
        }
    }
}

kapt {
    correctErrorTypes = true

    arguments {
        //Room migrations testing, this folder was also added in the android,sourceSets closure
        arg("room.schemaLocation", "$projectDir/schemas".toString())
    }
}

dependencies {
    debugImplementation(project(":utils"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // TODO: pack selected icons instead of this whole library
    implementation(libs.androidx.material.icons.extended)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // network
    implementation(libs.okhttp)
    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)
    // http traffic monitoring
    debugImplementation(libs.chucker)
    releaseImplementation(libs.chucker.no.op)

    // serialization
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit2.kotlinx.serialization.converter)

    // firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)

    // room
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // logging
    implementation(libs.timber)

    // testing
    debugImplementation(libs.wiremock.jre8)
    debugImplementation(libs.fuel)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

configure<PublishingExtension> {
    publications.create<MavenPublication>("debug") {
        groupId = "com.sandymist.android.mocker"
        artifactId = "apimock"
        version = rootProject.extra["projectVersion"] as String
        afterEvaluate {
            from(components["debug"])
        }
    }

    publications.create<MavenPublication>("release") {
        groupId = "com.sandymist.android.mocker"
        artifactId = "apimock-no-op"
        version = rootProject.extra["projectVersion"] as String
        afterEvaluate {
            from(components["release"])
        }
    }

    repositories {
        mavenLocal()
    }
}
