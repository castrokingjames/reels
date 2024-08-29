plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.compose)
  alias(libs.plugins.compose.compiler)
}

kotlin {
  androidTarget()
  jvm()
  sourceSets {
    commonMain {
      dependencies {
        implementation(libs.decompose)
        implementation(libs.essenty.lifecycle)
        implementation(libs.essenty.statekeeper)
        implementation(libs.essenty.instancekeeper)
        implementation(libs.essenty.backhandler)
        implementation(libs.essenty.lifecycle.coroutines)
        implementation(libs.kotlin.coroutines)
        implementation(libs.coil)
        implementation(projects.timber)
        implementation(projects.common)
        implementation(projects.ui.common)
        implementation(projects.ui.compose)
        implementation(projects.model)
        implementation(projects.domain)
      }
    }
    androidMain {
      dependencies {
      }
    }
    jvmMain {
      dependencies {
      }
    }
  }

  // https://youtrack.jetbrains.com/issue/KT-61573
  compilerOptions {
    freeCompilerArgs.add("-Xexpect-actual-classes")
  }
}

android {
  namespace = "io.github.castrokingjames.reels.feature.stories"
  compileSdk = 34
  defaultConfig {
    minSdk = 24
  }

  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}
