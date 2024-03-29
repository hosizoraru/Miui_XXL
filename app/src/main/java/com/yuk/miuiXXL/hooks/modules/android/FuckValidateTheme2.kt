package com.yuk.miuiXXL.hooks.modules.android

import com.github.kyuubiran.ezxhelper.utils.Log
import com.github.kyuubiran.ezxhelper.utils.findAllMethods
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.github.kyuubiran.ezxhelper.utils.hookMethod
import com.github.kyuubiran.ezxhelper.utils.unhookAll
import com.yuk.miuiXXL.hooks.modules.BaseHook
import com.yuk.miuiXXL.utils.getBoolean
import de.robv.android.xposed.XC_MethodHook
import miui.drm.DrmManager
import miui.drm.ThemeReceiver

object FuckValidateTheme2 : BaseHook() {
    override fun init() {

        if (!getBoolean("thememanager_fuck_validate_theme", false)) return
        var hook: List<XC_MethodHook.Unhook>? = null
        try {
            findMethod(ThemeReceiver::class.java) {
                name == "validateTheme"
            }.hookMethod {
                before {
                    hook = findAllMethods(DrmManager::class.java) {
                        name == "isLegal"
                    }.hookBefore {
                        it.result = DrmManager.DrmResult.DRM_SUCCESS
                    }
                }
                after {
                    hook?.unhookAll()
                }
            }
        } catch (t: Throwable) {
            Log.ex(t)
        }
    }

}
