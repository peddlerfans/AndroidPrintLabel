pluginManagement {
    repositories {
//        maven("https://maven.aliyun.com/repositoryrepository/public/")
        maven("https://jitpack.io/")

        maven("https://maven.aliyun.com/repository/public/")
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/central")
        maven("https://maven.aliyun.com/repository/jcenter/")
        maven("https://maven.aliyun.com/repository/gradle-plugin/")
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
//        maven("https://maven.aliyun.com/repositoryrepository/public/")
        maven("https://jitpack.io/")
        maven("https://maven.aliyun.com/repository/public/")
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/central")
        maven("https://maven.aliyun.com/repository/jcenter/")
        maven("https://maven.aliyun.com/repository/gradle-plugin/")

        google()
        mavenCentral()
    }
}

rootProject.name = "LabelPrinting"
include(":app")

include(":core:network")
include(":core:navigation")

include(":feature:deliver")

include(":hardware:JCPrint")
include(":hardware:ble")
