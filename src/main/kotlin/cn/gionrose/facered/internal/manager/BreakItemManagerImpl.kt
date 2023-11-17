package cn.gionrose.facered.internal.manager

import cn.gionrose.facered.BreakCard
import cn.gionrose.facered.api.BreakItem
import cn.gionrose.facered.api.ItemData
import cn.gionrose.facered.api.manager.BreakItemManager
import cn.gionrose.facered.api.plugin.list.ListStatus
import cn.gionrose.facered.api.PrefixType.*
import cn.gionrose.facered.util.addDotIfNotExists
import io.lumine.mythic.bukkit.MythicBukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.manager.ItemManager
import pers.neige.neigeitems.utils.ItemUtils.getNbt
import pers.neige.neigeitems.utils.ItemUtils.invalidNBT
import pers.neige.neigeitems.utils.ItemUtils.isNiItem
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.console
import taboolib.common5.cdouble
import taboolib.common5.cint
import taboolib.platform.compat.replacePlaceholder

/**
 * @description TODO
 * @author facered
 * @date 2023/11/10 14:18
 */
object BreakItemManagerImpl: BreakItemManager() {

    override fun handle(
        builder: (BreakItem.() -> BreakItem)?,
        handle: BreakItemManager.(BreakItem?) -> Unit
    ): BreakItemManager {
        if (builder == null)
            this.handle(null)
        else
            this.handle (builder (BreakItem()))
        return this
    }

    override fun handle (builder: ItemData. () -> ItemData): ItemData
    {
        return builder (ItemData())
    }

    override fun match(itemStack: ItemStack, callback: BreakItem.() -> Unit) {
        this.forEach {
            if (it.match(itemStack))
                for (count in 0 until itemStack.amount)
                    callback (it)
        }
    }

    override fun billing(
        player: Player,
        breakItems: MutableList<BreakItem>,
        callback: (MutableList<ItemStack>, MutableList<BreakItem>, ItemStack?, Double) -> Unit
    ) {
        var totalMoney = 0.0
        val billingItems = mutableListOf<ItemStack>()


        breakItems.forEach {
            totalMoney += it.money
            billingItems.addAll(it.itemData.map { itemData ->
                when (itemData.prefix)
                {
                    NI -> {ItemManager.getItemStack(itemData.itemId)!!.apply { amount = itemData.amount }}
                    MM -> {MythicBukkit.inst().itemManager.getItemStack(itemData.itemId)!!.apply { amount = itemData.amount }}
                    MONEY -> TODO()
                }
             })
        }
        val moneyItem = BreakCard.moneyItemManager.getMoneyItem {
            it.replacePlaceholder(player).replace("{money}", totalMoney.toString())
        }

        billingItems.merge()

        if (billingItems.isEmpty())
            callback (billingItems, breakItems, null, 0.0)
        else
            callback (billingItems, breakItems, moneyItem, totalMoney)

    }

    private fun List<ItemStack>.merge ()
    {
        for (index in indices)
            for (nextIndex in index+1 until this.size)
                if (this[index].itemMeta!!.displayName == (this[nextIndex].itemMeta!!.displayName))
                {
                    if (this[index].amount >= 64)
                        break
                    this[index].amount += this[nextIndex].amount
                    this[nextIndex].amount = 0
                }
    }

    @Awake (LifeCycle.ENABLE)
    override fun onEnable() {
        load()
    }


    private fun BreakItem.match (itemStack: ItemStack): Boolean
    {
        return itemStack.itemMeta!!.lore!!.let {
            if (it.contains(condition))
                if (it.contains(condition2))
                    return@let true
            false
        }
    }

    private fun load ()
    {
        //获取 breakItem.yml 配置文件
        BreakCard.configManager.debugGet ("breakItem"){_, config ->
            //如果获取成功
            getSuccess.takeIf { it }?.let {
                //获取 lore 节点
                val loreSection = config!!.getConfigurationSection("lore")!!
                //遍历 lore 下的一级节点名
                loreSection.getKeys(false).forEach { next ->
                    //装备位置节点
                    val positionSection = loreSection.getConfigurationSection(next)!!
                    //遍历装备位置节点下的一级节点名 星级节点
                    positionSection.getKeys(false).forEach {doubleNext ->
                        //获取星级节点下的 分解物集合
                        val breakItemConfigList = positionSection.getStringList(doubleNext)

                        //分解物
                        val itemData = mutableListOf<ItemData>()

                        var money = 0.0

                        breakItemConfigList.forEach {

                            val prefix = it.substring(0, 7)
                            if (prefix.contains(NI.name))
                            {
                                val neigeItemId = it.split(" ")[1].trim()
                                val amount = it.split(" ")[2].trim().cint

                                BreakCard.breakItemManager.handle {
                                    this.itemId = neigeItemId
                                    this.amount = amount
                                    this.prefix = NI
                                    this
                                }.let (itemData::add)

                            }else if (prefix.contains(MM.name))
                            {
                                val mmItemId = it.split(" ")[1].trim()
                                val amount = it.split(" ")[2].trim().cint
                                BreakCard.breakItemManager.handle {
                                    this.itemId = mmItemId
                                    this.amount = amount
                                    this.prefix = MM
                                    this
                                }.let (itemData::add)

                            }else if (prefix.contains(MONEY.name))
                                money = it.split(" ")[1].addDotIfNotExists().cdouble
                        }
                        BreakCard.breakItemManager.handle({
                            this.condition = next.replace("&", "§")
                            this.condition2 = doubleNext.replace("&", "§")
                            this.itemData.addAll(itemData)
                            this.money = money
                            this
                        }){breakItem ->
                            debugAdd(breakItem)
                        }
                    }
                }
            }
        }
    }

    override fun reload() {
        this.debugReMoveAll()
        onEnable()
    }

    override fun debug(removeStatus: ListStatus.Remove, result: BreakItem?) {
        if (removeStatus.removeSuccess)
            BreakCard.configManager.debug {
                console ().sendMessage("[breakItemManager] 移除 [breakItem] [条件] : [${result!!.condition}] [${result.condition}]")
            }
    }

    override fun debug(removeAtStatus: ListStatus.RemoveAt, index: Int, result: BreakItem?) {

    }

    override fun debug(getStatus: ListStatus.Get, index: Int, result: BreakItem?) {
        if (getStatus.getSuccess && getStatus.defaultValueIsNull)
            BreakCard.configManager.debug {
                console ().sendMessage("[breakItemManager] 获取 [breakItem] : [$result]")
            }
    }

    override fun debug(setStatus: ListStatus.Set, index: Int, result: BreakItem?) {

    }

    override fun debug(addStatus: ListStatus.Add, result: BreakItem?) {
        if (addStatus.addSuccess && addStatus.defaultValueIsNull)
            BreakCard.configManager.debug {
                console ().sendMessage("[breakItemManager] 添加 [BreakItem] : [$result]")

            }
    }
}