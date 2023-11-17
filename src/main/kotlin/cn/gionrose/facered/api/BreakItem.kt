package cn.gionrose.facered.api

import org.bukkit.inventory.ItemStack

/**
 * @description TODO
 * @author facered
 * @date 2023/11/10 12:17
 */
class BreakItem(var condition: String = "", var condition2: String = "", var money: Double = 0.0, val itemData: MutableList<ItemData> = mutableListOf()) {

    override fun toString(): String {
        return "BreakItem(condition='$condition', condition2='$condition2', money=$money, itemData=$itemData)"
    }

    fun clone (): BreakItem
    {
        return BreakItem (condition, condition2, money, itemData)
    }
}