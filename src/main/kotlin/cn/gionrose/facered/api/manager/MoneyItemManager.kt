package cn.gionrose.facered.api.manager

import cn.gionrose.facered.api.plugin.map.BaseDebugMap
import org.bukkit.inventory.ItemStack

/**
 * @description TODO
 * @author facered
 * @date 2023/11/13 21:52
 */
abstract class MoneyItemManager: BaseDebugMap<Int, String> (){


    abstract fun onEnable ()

    abstract fun getMoneyItem(beforeLoad: (String) -> String): ItemStack

    abstract fun reload ()
}