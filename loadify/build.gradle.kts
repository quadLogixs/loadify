import java.util.Properties
group="com.lib.quad.logixs"
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.plugin.compose)
    id("maven-publish")
}

android {
    namespace = "com.lib.quad.logixs.loadify"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
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
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
//Compose
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.activity.compose)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.lifecycle)
    api(libs.androidx.compose.view.model)
    api(libs.androidx.compose.hilt.navigation)
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.graphics)
    api(libs.androidx.compose.ui.material)
    api(libs.androidx.compose.preview)
    api(libs.androidx.navigation.compose)

    //Others
    api(libs.coil.compose)
    api(libs.lottie.compose)
}
val githubProperties = Properties().apply {
    file("github.properties").takeIf { it.exists() }?.inputStream()?.use { load(it) }
}
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = "com.lib.quad.logixs"
                group = "com.lib.quad.logixs"
                artifactId = "loadify"
                version = "1.0.0"
            }
        }
        repositories {

            maven {
                name = "loadify"
                /** Configure path of your package repository on Github
                 *  Replace GITHUB_USERID with your/organisation Github userID and REPOSITORY with the repository name on GitHub
                 */
                url = uri("https://maven.pkg.github.com/quadLogixs/loadify") // Github Package
                credentials {
                    //Fetch these details from the properties file or from Environment variables
                    username =
                        githubProperties.get("gpr.usr") as String? ?: System.getenv("GPR_USER")
                    password =
                        githubProperties.get("gpr.key") as String? ?: System.getenv("GPR_API_KEY")
                }
            }
        }

    }
 }