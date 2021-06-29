package app.futured.arkitekt.kmmexample

import platform.Foundation.NSThread
import platform.UIKit.UIDevice

actual class Platform actual constructor() {
    actual val platform: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun isMainThread(): Boolean {
    return NSThread.isMainThread
}
