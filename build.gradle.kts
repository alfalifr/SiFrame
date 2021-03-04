// Top-level build file where you can add configuration options common to all sub-projects/modules.
import org.gradle.api.plugins.*
import java.net.URI

//import org.gradle.script.lang.kotlin.*
/*
val ScriptHandlerScope.kotlin_version
    get()= findProperty("kotlin_version")
 */
fun File.withInputStream(): java.io.FileInputStream = java.io.FileInputStream(this)


val PKG_REPOSITORY= "SidevLib_Private" //"JvmLib"

val prop= java.util.Properties().apply { load(file("/setting.properties").withInputStream()) }

val githubUser= prop["githubUser"] as String //prop.loadPropertyFromResources("setting.properties", "bintrayUser")
val githubApiKey= prop["githubApiKey"] as String //prop.loadPropertyFromResources("setting.properties", "bintrayApiKey")

//val githubPkgUrl= "https://maven.pkg.github.com/$githubUser/$PKG_REPOSITORY"

fun DependencyHandler.implementation(dependencyNotation: Any): Dependency? =
    add("implementation", dependencyNotation)


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
        classpath("com.android.tools.build:gradle:4.1.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("com.github.dcendents:android-maven-gradle-plugin:2.0")
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4")

        //Gson
        //implementation("com.google.code.gson:gson:2.8.6")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        //maven { url = URI("https://dl.bintray.com/alfalifr/SidevLib") }
//        maven { url = URI("https://dl.bintray.com/alfalifr/JvmLib") }

        maven {
            name = "GitHubPackages"
            /** Configure path of your package repository on Github
             *  Replace GITHUB_USERID with your/organisation Github userID and REPOSITORY with the repository name on GitHub
             */
            //this@maven.
            url = uri("https://maven.pkg.github.com/$githubUser/$PKG_REPOSITORY") // Github Package
            credentials {
                //Fetch these details from the properties file or from Environment variables

                println(githubUser)
                println(githubApiKey)
                
                username = githubUser //githubProperties.get("gpr.usr") as String? ?: System.getenv("GPR_USER")
                password = githubApiKey //githubProperties.get("gpr.key") as String? ?: System.getenv("GPR_API_KEY")
            }
            //publish = false
        }
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