plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.org.jetbrains.kotlin.kapt)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.mapkit"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mapkit"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"


        buildConfigField("String", "APPLICATION_ID", "\"com.example.mapkit\"") // test
        buildConfigField("String", "BASE_URL", "\"https://test/\"") // PROD
        buildConfigField ("String", "MAPKIT_API_KEY", "\"522fb9ba-acc3-4c2a-ad64-371448cace44\"")


        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {

        release {
            isMinifyEnabled = false
            proguardFiles (getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            isMinifyEnabled = false
            proguardFiles (getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            isDebuggable = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.play.services.auth.api.phone)
//    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.animated.navigation.bar)
    implementation ("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation ("androidx.navigation:navigation-ui-ktx:2.5.3")
    implementation ("androidx.appcompat:appcompat:1.6.1")

    //Drag Drop Recyclerview
    implementation ("com.ernestoyaquello.dragdropswiperecyclerview:drag-drop-swipe-recyclerview:1.1.1")


    api("com.karumi:dexter:6.2.3")
    api("androidx.security:security-crypto:1.1.0-alpha06")
    implementation ("com.github.kirich1409:viewbindingpropertydelegate-noreflection:1.5.6")
    //PinView
    implementation ("io.github.chaosleung:pinview:1.4.4")

    // hilt
    implementation (libs.hilt.android)
    kapt (libs.hilt.compiler)
    kapt (libs.androidx.hilt.compiler)

    implementation (libs.androidx.hilt.work)
    kapt (libs.hilt.android.compiler)
    kaptTest (libs.hilt.android.compiler)
    kaptAndroidTest (libs.hilt.android.compiler)

    //coil
    implementation(libs.coil)
    implementation(libs.coil.svg)

    // Coil Compose
    implementation ("io.coil-kt:coil-compose:2.6.0")

    //@REST API: Adding retrofit to the mainLayer
    implementation ("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:5.0.0-alpha.7")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.7")
    implementation ("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")

    //kotlinx.coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    implementation ("androidx.work:work-runtime-ktx:2.9.0")
    implementation ("androidx.hilt:hilt-navigation-compose:1.2.0")

    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.31.0-alpha")


    //Chucker
    debugApi("com.github.chuckerteam.chucker:library:3.5.2")
    releaseApi("com.github.chuckerteam.chucker:library:3.5.2")

    // Room
    implementation ("androidx.room:room-runtime:2.6.1")
//    annotationProcessor "androidx.room:room-compiler:2.4.3"
    kapt ("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")

    implementation ("com.yandex.android:maps.mobile:4.5.0-full")

    implementation ("com.airbnb.android:lottie:3.7.0")


}

kapt {
    correctErrorTypes = true
}