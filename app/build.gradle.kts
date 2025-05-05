import java.io.FileInputStream
import java.io.InputStream
import java.util.Properties


plugins {
    id("com.google.gms.google-services")
    alias(libs.plugins.android.application)
}

var localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if(localPropertiesFile.exists()) {
        load(localPropertiesFile.inputStream())
    }
}

android {
    namespace = "com.example.echatbotproject"
    compileSdk = 35

    defaultConfig {

        applicationId = "com.example.echatbotproject"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


        // Hidden Properties added
        val apiKey = localProperties.getProperty("API_KEY")
        val modelName = localProperties.getProperty("MODEL_NAME")

        buildConfigField("String", "API_KEY", apiKey)
        buildConfigField("String", "MODEL_NAME", modelName)




    }


    buildFeatures{
        buildConfig = true
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Import the Firebase BoM
    implementation(platform(libs.firebase.bom))

    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation(libs.firebase.analytics)

    // Firebase Authentication
    implementation(libs.firebase.auth)
    // Latest : 23.2.0

    // Firebase Firestore
    implementation(libs.firebase.firestore)
    // Latest : 25.1.4

    implementation("com.google.ai.client.generativeai:generativeai:0.1.0")

    implementation("com.google.guava:guava:31.0.1-android")
    implementation("org.reactivestreams:reactive-streams:1.0.4")

}