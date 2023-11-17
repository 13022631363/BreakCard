package cn.gionrose.facered.api.manager

import cn.gionrose.facered.api.BreakItem
import cn.gionrose.facered.api.ItemData
import cn.gionrose.facered.api.plugin.list.BaseDebugList
import cn.gionrose.facered.api.plugin.map.BaseDebugMap
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.util.each
import javax.security.sasl.AuthorizeCallback

/**
 * @description TODO
 * @author facered
 * @date 2023/11/10 13:56
 */
abstract class BreakItemManager: BaseDebugList<BreakItem> () {


    abstract fun handle (
        builder: (BreakItem.() -> BreakItem)?,
        handle: BreakItemManager. (BreakItem?) -> Unit): BreakItemManager

    abstract fun handle (builder: ItemData. () -> ItemData): ItemData
    abstract fun match (itemStack: ItemStack, callback: BreakItem.() -> Unit)

    abstract fun billing (player: Player, breakItems: MutableList<BreakItem>, callback: (MutableList<ItemStack>, MutableList<BreakItem>, ItemStack?, Double) -> Unit)

    abstract fun onEnable ()

    abstract fun reload ()



}