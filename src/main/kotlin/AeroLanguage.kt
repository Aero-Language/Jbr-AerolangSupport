package com.aerolang.aerolangsupport

import com.intellij.lang.Language

class AeroLanguage private constructor() : Language("Aero") {
    companion object {
        @JvmStatic
        val Instance = AeroLanguage()
    }
}