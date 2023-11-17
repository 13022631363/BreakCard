package cn.gionrose.facered.api.manager

import org.bukkit.inventory.ItemStack

/**
 * @description TODO
 * @author facered
 * @date 2023/11/15 13:45
 */
abstract class HandoutItemManager {

    abstract fun getHandoutItem (): ItemStack

    abstract fun reload ()
}