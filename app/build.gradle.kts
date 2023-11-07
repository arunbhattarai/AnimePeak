plugins {
    id("com.android.application")
}

android {
    namespace = "np.com.arunb.animepeak"
    compileSdk = 33

    defaultConfig {
        applicationId = "np.com.arunb.animepeak"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "Beta"

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("androidx.preference:preference:1.2.1")
    implementation("com.nex3z:flow-layout:1.3.3")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("androidx.media3:media3-ui:1.1.1")
    implementation ("androidx.sqlite:sqlite:2.4.0")
    implementation ("com.karumi:dexter:6.2.3")
    implementation ("com.google.code.gson:gson:2.10.1")
    implementation ("com.github.mancj:MaterialSearchBar:0.8.5")
    implementation ("androidx.media3:media3-exoplayer:1.1.1")
    implementation ("androidx.media3:media3-exoplayer-hls:1.1.1")
    implementation ("io.github.glailton.expandabletextview:expandabletextview:1.0.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

}