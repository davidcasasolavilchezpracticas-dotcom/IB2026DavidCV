plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = "com.iberdrola.practicas2026.davidcv"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.iberdrola.practicas2026.davidcv"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    kotlin {
        jvmToolchain(17)
    }

    buildFeatures {
        compose = true
    }

    room {
        schemaDirectory("$projectDir/schema")
    }

    //? Se usa <Exec> para definir de que toda la tarea es de tipo ejecución
    tasks.register<Exec>("adbReverseMockoon") {
        val adbPath = android.sdkDirectory.resolve("platform-tools/adb.exe").absolutePath
        commandLine(adbPath, "reverse", "tcp:3000", "tcp:3000")
        commandLine(adbPath, "reverse", "tcp:3000", "tcp:3000")
        isIgnoreExitValue = true //? Para que no de error si no hay móvil conectado

        doLast {
            println("ADB Reverse: Puerto 3000 vinculado a Mockoon.")
        }
    }

    tasks.named("preBuild") {
        dependsOn("adbReverseMockoon")
    }
}



dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.compose.material.core)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.foundation.layout)
    implementation(libs.androidx.datastore.core)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.camera.camera2.pipe)
    implementation(libs.firebase.config)
    implementation(libs.androidx.benchmark.common)
    implementation(libs.androidx.compose.ui.text)
    implementation(libs.androidx.work.runtime.ktx)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    //? DataStore
    implementation(libs.androidx.datastore.preferences)

    //? Librerías para Animaciones
    implementation(libs.lottie.compose)

    //? Librerías para Navegación
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.runtime.android)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.accompanist.navigation.animation)

    //? Librerías para Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    ksp(libs.hilt.android.compiler)

    //? Librerías para integrción con ViewModel
    implementation(libs.androidx.hilt.navigation.compose)

    //? Librerías para ROOM
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx) // Extensión Kotlin para Room

    //? Aplicar el compilador de ROOM
    ksp(libs.androidx.room.compiler)


    //? Librerías para Picasso
    implementation(libs.picasso)

    //? Librerías para Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    //? Librerías para Coil
    implementation(libs.coil.compose)

    //? Librerías para firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    //? Librerías para Testing
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit)

    //? Librerías para Android Testing
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.android.compiler)

}
