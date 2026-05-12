package com.iberdrola.practicas2026.davidcv.ui.base.common

class SafeClickTools {
    companion object {
        fun canExecuteMethod(manager: ClickEventManager, method: () -> Unit) {
            if (manager.canExecute()) method()
        }

        fun canExecuteMethodListString(manager: ClickEventManager, method: () -> List<String>): List<String>? {
            if (manager.canExecute()) return method()
            return null
        }
    }
}