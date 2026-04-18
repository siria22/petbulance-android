@file:Suppress("DEPRECATION")

import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("kotlin-parcelize")
    id("org.jetbrains.kotlin.plugin.serialization")

    alias(libs.plugins.devtoolsKsp)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}

android {

    buildFeatures {
        buildConfig = true
    }

    namespace = "com.petbulance.presentation"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField(
            "String",
            "WEB_CLIENT_ID",
            "\"${localProperties["WEB_CLIENT_ID"]}\""
        )
        buildConfigField(
            "String",
            "NAVER_CLIENT_ID",
            "\"${localProperties["NAVER_CLIENT_ID"]}\""
        )
        buildConfigField(
            "String",
            "NAVER_CLIENT_SECRET",
            "\"${localProperties["NAVER_CLIENT_SECRET"]}\""
        )
        buildConfigField(
            "String",
            "NAVER_API_CLIENT_ID",
            "\"${localProperties["NAVER_API_CLIENT_ID"]}\""
        )
        buildConfigField(
            "String",
            "NAVER_API_CLIENT_SECRET",
            "\"${localProperties["NAVER_API_CLIENT_SECRET"]}\""
        )
        buildConfigField(
            "String",
            "KAKAO_NATIVE_APP_KEY",
            "\"${(localProperties["KAKAO_NATIVE_APP_KEY"] as String? ?: "").lowercase()}\""
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        freeCompilerArgs = listOf("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
}

val generateTokensClasspath by configurations.creating

dependencies {
    implementation(project(":domain"))

    implementation("androidx.core:core-ktx:1.15.0")

    // UI - Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.activity.compose) // for 'collectAsStateWithLifecycle()'

    // UI - Material
    implementation(libs.material)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.compose.material.icons.extended)

    // UI - Image
    implementation(platform(libs.coil.bom))
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    implementation(libs.coil.svg)
    implementation(libs.coil.video)

    // Lifecycle & Navigation
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.androidx.paging.compose)
    
    // DI
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.compose.foundation)
    ksp(libs.hilt.android.compiler)

    // Permissions
    implementation(libs.accompanist.permissions)

    // Google Play Services Location
    implementation(libs.play.services.location)

    // Firebase & Auth
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.messaging)
    implementation(libs.credential.manager)
    implementation(libs.credential.manager.google)
    implementation(libs.googleid)
    implementation(libs.play.services.auth)
    implementation(libs.kotlinx.coroutines.play.services)

    // Login
    implementation(libs.kakao.login)
    implementation(libs.naver.login)
    implementation(libs.googleid)

    // Naver Map
    implementation(libs.map.sdk)

    // CameraX
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    val kotlinVersion = libs.versions.kotlin.get()

    generateTokensClasspath("org.jetbrains.kotlin:kotlin-compiler-embeddable:$kotlinVersion")
    generateTokensClasspath("org.jetbrains.kotlin:kotlin-script-runtime:$kotlinVersion")
    generateTokensClasspath("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    generateTokensClasspath("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    generateTokensClasspath("org.jetbrains.kotlin:kotlin-scripting-compiler-embeddable:$kotlinVersion")
    generateTokensClasspath("org.jetbrains.kotlin:kotlin-serialization-compiler-plugin-embeddable:$kotlinVersion")

    generateTokensClasspath(libs.kotlinx.serialization.json)
    generateTokensClasspath(libs.kotlinpoet)

}

tasks.register<JavaExec>("generateDesignTokens") {

    group = "petbulance"
    description = "Generates Primitives.kt from tokens.json using kts script."

    val scriptFile = project.rootProject.file("tokens/generateColors.kts")
    val tokenFile = project.rootProject.file("tokens/tokens.json")
    inputs.file(scriptFile)
    inputs.file(tokenFile)

    val outputDir = file("src/main/java/com/example/presentation/component/theme/color")
    outputs.dir(outputDir)

    val gradleJavaHome = System.getProperty("org.gradle.java.home")
    if (gradleJavaHome != null && File(gradleJavaHome).exists()) {
        setExecutable(File(gradleJavaHome, "bin/java").absolutePath)
    }

    mainClass.set("org.jetbrains.kotlin.cli.jvm.K2JVMCompiler")
    classpath = generateTokensClasspath

    val serializationPluginJar = generateTokensClasspath.files.first {
        it.name.startsWith("kotlin-serialization-compiler-plugin-embeddable")
    }.absolutePath

    args = listOf(
        "-no-stdlib",
        "-no-reflect",
        "-Xplugin=$serializationPluginJar",
        "-classpath",
        generateTokensClasspath.asPath,
        "-script",
        scriptFile.path,
        outputDir.absolutePath,
        tokenFile.absolutePath
    )

    jvmArgs = listOf("-Dfile.encoding=UTF-8")
}

tasks.named("preBuild") {
    dependsOn(tasks.named("generateDesignTokens"))
}