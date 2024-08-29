import java.util.Properties

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.compose)
  alias(libs.plugins.compose.compiler)
}

android {
  namespace = "io.github.castrokingjames.reels.android"
  compileSdk = 34
  defaultConfig {
    applicationId = "io.github.castrokingjames.reels.android"
    minSdk = 24
    targetSdk = 34
    versionCode = 1
    versionName = "0.0.1"
  }

  buildFeatures {
    compose = true
    buildConfig = true
  }

  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }

  signingConfigs {
    getByName("debug") {
      storeFile = rootProject.file("release/debug.keystore")
      storePassword = "android"
      keyAlias = "androiddebugkey"
      keyPassword = "android"
    }

    create("release") {
      if (rootProject.file("release/release.jks").exists()) {
        storeFile = rootProject.file("release/release.jks")
        keyAlias = properties["KEY_ALIAS"]?.toString() ?: ""
        keyPassword = properties["KEY_PASSWORD"]?.toString() ?: ""
        storePassword = properties["STORE_PASSWORD"]?.toString() ?: ""
      }
    }
  }

  buildTypes {
    if (rootProject.file("local.properties").exists()) {
      val file = rootProject.file("local.properties")
      val properties = Properties()
      properties.load(file.inputStream())
    }

    val baseUrl: String = properties["BASE_URL"]?.toString() ?: ""
    val storyTellerApiKey: String = properties["STORYTELLER_API_KEY"]?.toString() ?: ""

    debug {
      buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
      buildConfigField("String", "STORYTELLER_API_KEY", "\"$storyTellerApiKey\"")

      signingConfig = signingConfigs["debug"]
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro",
      )
    }

    release {
      buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
      buildConfigField("String", "STORYTELLER_API_KEY", "\"$storyTellerApiKey\"")

      signingConfig = signingConfigs.findByName("release") ?: signingConfigs["debug"]
      isShrinkResources = true
      isMinifyEnabled = true
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro",
      )
    }
  }

  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_17.toString()
  }
}

dependencies {
  implementation(libs.koin)
  implementation(libs.koin.android)
  implementation(libs.kotlin.coroutines.android)
  implementation(projects.timber)
  implementation(projects.core)
}
