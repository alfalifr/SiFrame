// Top-level build file where you can add configuration options common to all sub-projects/modules.
import org.gradle.api.plugins.*
import java.net.URI

//import org.gradle.script.lang.kotlin.*
/*
val ScriptHandlerScope.kotlin_version
    get()= findProperty("kotlin_version")
 */

buildscript {
    val kotlin_version by extra("1.3.72")
//    val kotlin_version = findProperty("kotlin_version")
    val anko_version= findProperty("anko_version")
//    val kotlin_version= "1.3.72"
//    val anko_version= "0.10.8"
    //ext["kotlin_version"] = "1.3.72"
    //ext["anko_version"] = "0.10.8"
//    ext.kotlin_version = '1.3.72'
//    ext.anko_version = '0.10.8'
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.0.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("com.github.dcendents:android-maven-gradle-plugin:2.0")
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url = URI("https://dl.bintray.com/alfalifr/SidevLib") }
        maven { url = URI("https://dl.bintray.com/alfalifr/JvmLib") }
    }
}

tasks{
    val clean by registering(Delete::class) {
        delete(buildDir)
    }
//    delete rootProject.buildDir
}


/*
buildscript {
    ext.kotlin_version = '1.3.72'
    ext.anko_version = '0.10.8'
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.6.1'
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
        classpath 'com.github.dcendents:android-maven-gradle-plugin:2.0'

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()

    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
// */