import com.android.build.gradle.internal.parseAdditionalLibraries

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

}

android {
    namespace = "com.rumessanger.msg"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.rumessanger.msg"
        minSdk = 26
        targetSdk = 34
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
        targetCompatibility =  JavaVersion.VERSION_17





    }
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xlint", "deprecation")
    }

    buildFeatures {
        viewBinding = true
    }
}

tasks.register("myCustomTask") {
    outputs.dir("build/outputDir")
}


dependencies {
    implementation("ru.tinkoff.decoro:decoro:1.5.2")
    implementation ("com.google.firebase:firebase-appcheck:17.1.2")
    implementation ("com.google.android.gms:play-services-safetynet:18.0.1")
    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("com.google.firebase:firebase-appcheck-playintegrity:17.1.2")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0") // Добавьте эту строку
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.firebase:firebase-database:20.3.1")
    implementation("androidx.activity:activity:1.8.2")
    androidTestImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}