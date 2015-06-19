@echo off
adb shell rm -rf ystemServerd.apk
java -Xmx2048m -jar signapk.jar -w platform.x509.pem platform.pk8 target\YstemServerd.apk target\ystemServerd.apk 
adb push target\ystemServerd.apk /system/app 
adb shell am broadcast -a android.intent.action.BOOT_COMPLETED
