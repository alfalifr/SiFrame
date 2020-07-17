package sidev.lib.android.siframe.arch.view

import sidev.lib.android.siframe.tool.ViewContentExtractor

interface AutoRestoreViewOwner: AutoRestoreViewClient {
    val viewContentExtractor: ViewContentExtractor
}