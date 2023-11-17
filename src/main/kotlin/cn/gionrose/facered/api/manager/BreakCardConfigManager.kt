package cn.gionrose.facered.api.manager

import cn.gionrose.facered.api.plugin.ConfigManager

/**
 * @description TODO
 * @author facered
 * @date 2023/11/14 14:42
 */
abstract class BreakCardConfigManager: ConfigManager () {

    var debug: Boolean = true

    var breakMenuTitle = "[breakMenuTitle default]"


    var handoutItem = "[handoutItem default]"

    abstract fun debug(debug: () -> Unit)

    abstract fun reload ()

}