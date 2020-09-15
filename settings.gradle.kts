rootProject.name= "SiFrame_Root"
include(":app")

include(":jvm:universal")

include(":Android-External")
project(":Android-External").projectDir= File("/jvm/android/external")

include(":SiFrame")
project(":SiFrame").projectDir= File("/jvm/android/siframe")

include(":SiFrame-Customizable")
project(":SiFrame-Customizable").projectDir= File("/jvm/android/siframe/customizable")

include(":Viewrap")
project(":Viewrap").projectDir= File("/jvm/android/viewrap")

//include(":jvm:android:siframe:customizable")
//include(":jvm:android:viewrap")


//project(":SidevLibKt-StdLib").projectDir= File("/StdLib")