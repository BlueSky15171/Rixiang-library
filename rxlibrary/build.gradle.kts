plugins {
    id("com.android.library")
//    id("com.novoda.bintray-release")
    id("com.github.panpf.bintray-publish")
//    `maven-publish`
//    `signing`
}
android {
    namespace = "com.itskylin.devices.rixiang"
    compileSdk = 22

    defaultConfig {
        minSdk = 8
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

//    // 生成jar包的配置如下：
//    val JAR_PATH = "build/intermediates/runtime_library_classes_jar/release/" // 待打包文件的位置
//    val JAR_NAME = "classes.jar" // 待打包文件的名字
//    val DESTINATION_PATH = "libs" // 生成jar包的位置
//    val NEW_NAME = "rxlibrary.jar" // 生成jar包的名字
//
//    tasks.register<Copy>("makeJar") {
//        delete(DESTINATION_PATH + NEW_NAME)
//        from(JAR_PATH + JAR_NAME)
//        into(DESTINATION_PATH)
//        rename(JAR_NAME, NEW_NAME)
//    }

    lintOptions {
        isAbortOnError = false
    }

//    makeJar.dependsOn(build)
}

dependencies {
    implementation(files("libs/androidnet.jar"))
}

configure<com.github.panpf.bintray.publish.PublishExtension> {
//configure<com.novoda.gradle.release.PublishExtension> {
    userOrg = "BlueSky15171"
    groupId = "io.github.bluesky15171"
    artifactId = "rixiang"
    publishVersion = "2.1.0"
    desc = "rixiang device api"
    website = "https://github.com/BlueSky15171/Rixiang-library"
}