plugins {
    id ("com.android.application")
    id ("dagger.hilt.android.plugin")
    id ("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
}

kapt {
    generateStubs = true
}

android {
    namespace = "com.technoabinash.quizapplication"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.technoabinash.quizapplication"
        minSdk = 24
        targetSdk = 33
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
    buildFeatures {
        viewBinding= true
        buildConfig =true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    /*Retrofit library for network call.*/
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation ("com.jakewharton.retrofit:retrofit2-rxjava2-adapter:1.0.0")
    implementation ("com.squareup.okhttp3:okhttp:4.10.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation ("com.jakewharton.timber:timber:4.7.1")


    /*Dagger hilt library goes here.*/
    implementation ("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    /*navigation component*/
    implementation ("androidx.navigation:navigation-fragment:2.5.3")
    implementation( "androidx.navigation:navigation-ui:2.5.3")
    // Room components
    implementation ("androidx.room:room-runtime:2.5.2")
//    annotationProcessor ("androidx.room:room-compiler:2.5.2")
    androidTestImplementation ("androidx.room:room-testing:2.5.2")
    kapt( "androidx.room:room-compiler:2.5.2")
    // Lifecycle components
    implementation ("androidx.lifecycle:lifecycle-viewmodel:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-livedata:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-common-java8:2.6.2")

    //room db
//    implementation ("androidx.room:room-runtime:2.5.2")
//    annotationProcessor( "androidx.room:room-compiler:2.5.2")
//    implementation ("android.arch.persistence.room:runtime:1.0.0")
//    annotationProcessor ("android.arch.persistence.room:compiler:1.0.0")

}