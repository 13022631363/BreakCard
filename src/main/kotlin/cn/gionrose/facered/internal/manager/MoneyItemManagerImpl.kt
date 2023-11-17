package cn.gionrose.facered.internal.manager

import cn.gionrose.facered.BreakCard
import cn.gionrose.facered.api.PrefixType
import cn.gionrose.facered.api.manager.MoneyItemManager
import cn.gionrose.facered.api.plugin.map.MapStatus
import cn.gionrose.facered.api.PrefixType.*
import cn.gionrose.facered.api.plugin.map.BaseDebugMap
import io.lumine.mythic.bukkit.MythicBukkit
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.manager.ItemManager
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.console
import taboolib.common5.cint

/**
 * @description TODO
 * @author facered
 * @date 2023/11/13 21:54
 */
internal object MoneyItemManagerImpl: MoneyItemManager () {

    var type = NI

    var id = ""

    var source: ItemStack? = null


    private fun loadItem ()
    {
        BreakCard.configManager.debugGet("breakItem") { key, breakItem ->
            if (getSuccess && defaultValueIsNull)
            {
                type = PrefixType.valueOf(breakItem!!.getString("moneyItem.type")!!)
                id = breakItem.getString("moneyItem.id")!!
                (breakItem.getMapList("moneyItem.lore")[0] as MutableMap<Int, String>).forEach { (slot, aLore) ->
                    this@MoneyItemManagerImpl.debugPut(slot, aLore)
                }
            }
        }
        source = when (type) {
            NI -> ItemManager.getItemStack(id)!!
            MM -> MythicBukkit.inst().itemManager.getItemStack(id)!!
            MONEY -> null
        }
    }
    override fun getMoneyItem (beforeLoad: (String) -> String): ItemStack
    {
        val clone = source!!.clone()

        val itemMeta = source!!.itemMeta!!
        val lore = itemMeta.lore!!

        this.forEach { (index, aLore) ->
            val outOfSize = index - (lore.size -1)
            if (outOfSize > 0)
                for (count in 0 until outOfSize)
                    lore.add(" ")
            lore[index] = beforeLoad (aLore)
        }
        itemMeta.lore = lore
        clone.itemMeta = itemMeta

        return clone
    }

    override fun reload() {
        this.debugRemoveAll()
        this.onEnable()
    }

    @Awake (LifeCycle.ENABLE)
    override fun onEnable() {
        loadItem()
    }

    override fun debug(removeStatus: MapStatus.Remove, key: Int?, result: String?) {
        if (removeStatus.removeSuccess)
            BreakCard.configManager.debug { console ().sendMessage ("[moneyItem] 移除 [key] : [$key] [value] : [$result]")}
    }

    override fun debug(getStatus: MapStatus.Get, key: Int?, result: String?) {
        if (getStatus.getSuccess)
            if (getStatus.defaultValueIsNull)
                BreakCard.configManager.debug { console ().sendMessage ("[moneyItem] 获取 [key] : [$key] [value] : [$result]")}
            else BreakCard.configManager.debug { console ().sendMessage ("[moneyItem] 获取默认值 [key] : [$key]")}

    }

    override fun debug(putStatus: MapStatus.Put, key: Int?, result: String?) {
        if (putStatus.putSuccess)
            if (putStatus.defaultValueIsNull)
                BreakCard.configManager.debug { console ().sendMessage ("[moneyItem] 放入 [key] : [$key] [value] : [$result]")}
            else BreakCard.configManager.debug { console ().sendMessage ("[moneyItem] 放入默认值 [key] : [$key]")}
    }
}