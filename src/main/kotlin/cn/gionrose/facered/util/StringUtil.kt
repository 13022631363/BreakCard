package cn.gionrose.facered.util

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import taboolib.platform.BukkitPlugin

/**
 * @description TODO
 * @author facered
 * @date 2023/11/10 23:12
 */

fun String.addDotIfNotExists(): String
{
    return if (!this.contains("."))
        "${this}.0"
    else this.toString ()
}

fun String.getPlayer (): Player?
{
    return Bukkit.getPlayerExact(this)
}

