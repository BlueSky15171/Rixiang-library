plugins {
    id("com.android.library")
    `maven-publish`
}
android {
    namespace = "com.itskylin.devices.rixiang"
    compileSdk = 22

    defaultConfig {
        minSdk = 8
        //noinspection ExpiredTargetSdkVersion
        targetSdk = 22
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    lintOptions {
        isAbortOnError = false
    }

    sourceSets {
        getByName("main") {
            jniLibs.srcDir("libs")
        }
    }
}

dependencies {
    implementation(files("libs/androidnet.jar"))
}

// Because the components are created only during the afterEvaluate phase, you must
// configure your publications using the afterEvaluate() lifecycle method.
afterEvaluate {
    publishing {
        publications {
            // Creates a Maven publication called "release".
            create<MavenPublication>("release") {
                // Applies the component for the release build variant.
                from(components["release"])
                groupId = "io.github.bluesky15171"
                artifactId = "rixiang-library"
                version = "2.1.0"
            }
            // Creates a Maven publication called “debug”.
            create<MavenPublication>("debug") {
                // Applies the component for the debug build variant.
                from(components["debug"])
                groupId = "io.github.bluesky15171"
                artifactId = "rixiang-debug"
                version = "2.1.0"
            }
        }
    }
}