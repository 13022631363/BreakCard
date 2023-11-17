package cn.gionrose.facered.internal.manager

import cn.gionrose.facered.BreakCard
import cn.gionrose.facered.api.BreakItem
import cn.gionrose.facered.api.PrefixType
import cn.gionrose.facered.api.manager.HandoutItemManager
import io.lumine.mythic.bukkit.MythicBukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.manager.ItemManager

object HandoutItemManagerImpl : HandoutItemManager() {

    override fun getHandoutItem(): ItemStack {
        BreakCard.configManager.handoutItem.trim().split(" ").let {
            if (it.size != 2)
                throw RuntimeException("handoutItem 物品配置错误 !")
            val type = PrefixType.valueOf(it[0].substring(1, 3))
            val id = it[1].trim()

            return when (type) {
                PrefixType.NI -> ItemManager.getItemStack(id)!!
                PrefixType.MM -> MythicBukkit.inst().itemManager.getItemStack(id)!!
                PrefixType.MONEY -> ItemStack(Material.AIR)
            }
        }
    }

    override fun reload() {

    }
}