package cn.gionrose.facered.internal.feature.command

import cn.gionrose.facered.BreakCard
import cn.gionrose.facered.api.BreakItem
import cn.gionrose.facered.api.manager.BreakItemManager
import cn.gionrose.facered.util.getPlayer
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.command
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit
import taboolib.common5.Baffle
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.Basic
import taboolib.platform.compat.VaultService
import taboolib.platform.compat.depositBalance
import taboolib.platform.compat.isEconomySupported
import taboolib.platform.util.onlinePlayers
import taboolib.platform.util.sendLang
import java.util.concurrent.TimeUnit

/**
 * @description TODO
 * @author facered
 * @date 2023/11/13 10:31
 */
@CommandHeader (name = "breakc")
internal object BreakCommand
{
    val baffle = Baffle.of(250, TimeUnit.MILLISECONDS)


    @CommandBody(permission = "breakCard.command.menu")
    val menu = subCommand {
        dynamic("玩家名") {
            suggestion<CommandSender> {_, _ ->
                onlinePlayers.map {it.name}
            }
            execute<CommandSender> {_, context, _ ->
                val player = context["玩家名"].getPlayer()!!
                val title = BreakCard.configManager.breakMenuTitle
                player.openMenu<Basic>(title) {
                    rows(4)
                    map("ppppppppp","ccccccchc","bbbbbbbbb", "bbbbbbbbb")

                    val playerSlot = mutableListOf<Int>()
                    for (slot in 36..70)
                        slot.let (playerSlot::add)

                    val putItemSlots = getSlots('p')
                    val handoutSlots = getSlots('h')
                    val billingItemSlots = getSlots('b')
                    val cantMoveSlots = getSlots('c')

                    cantMoveSlots.forEach {
                        set(it, ItemStack(Material.FLINT).apply { itemMeta = itemMeta!!.apply {setCustomModelData (10000) }})
                    }
                    handoutSlots.forEach {
                        set(it, BreakCard.handoutItemManager.getHandoutItem())
                    }

                    val matchedBreakItem = mutableListOf<BreakItem>()
                    onClick(true) {clickEvent ->

                        if (putItemSlots.any (clickEvent.rawSlot::equals) || playerSlot.any (clickEvent.rawSlot::equals))
                            clickEvent.isCancelled = false

                        if (putItemSlots.any(clickEvent.rawSlot::equals))
                        {
                            matchedBreakItem.clear()

                            val billingItems = mutableListOf<ItemStack>()
                            val putItems = mutableListOf<ItemStack>()

                            if (baffle.hasNext(player.uniqueId.toString()))
                            {
                                submit (async = false, delay = 5) {
                                    putItemSlots.forEach {
                                        clickEvent.inventory.getItem(it)?.let(putItems::add)
                                    }

                                    if (putItems.isEmpty())
                                    {
                                        billingItemSlots.forEach {
                                            clickEvent.inventory.setItem(it, ItemStack(Material.AIR))
                                        }
                                        return@submit
                                    }

                                    putItems.forEach {
                                        BreakCard.breakItemManager.match(it){
                                            clone ().let (matchedBreakItem::add)
                                        }
                                    }

                                    BreakCard.breakItemManager.billing(player, matchedBreakItem){billingItemsIt, _, moneyItems, _ ->
                                        moneyItems?.let (billingItems::add)
                                        billingItems.addAll(billingItemsIt)
                                    }
                                    while (true)
                                    {
                                        if (billingItems.size >= billingItemSlots.size)
                                            break
                                        billingItems.add(ItemStack(Material.AIR))
                                    }

                                    billingItemSlots.forEachIndexed { index, slot ->
                                        if (index < billingItems.size)
                                            clickEvent.inventory.setItem(slot, billingItems[index])
                                    }
                                }
                            }

                            return@onClick
                        }
                        if (handoutSlots.any(clickEvent.rawSlot::equals))
                        {
                            if (matchedBreakItem.isEmpty())
                                return@onClick

                            BreakCard.breakItemManager.billing (player, matchedBreakItem){ billingItemsIt, _, _, money ->
                                matchedBreakItem.clear()
                                if (isEconomySupported)
                                {
                                    player.depositBalance(money)
                                    if (money != 0.0)
                                        player.sendLang("分解后金币信息", money)
                                }
                                player.inventory.addItem(*billingItemsIt.toTypedArray().clone())

                                if (billingItemsIt.isNotEmpty())
                                {
                                    billingItemsIt.forEach {
                                        if (it.type != Material.AIR)
                                            player.sendLang("分解后物品信息", it.amount, it.itemMeta!!.displayName)
                                    }
                                    player.playSound( player.location, "block.anvil.use", 1f, 1f)
                                }
                            }

                            billingItemSlots.forEach {
                                clickEvent.inventory.setItem(it, ItemStack(Material.AIR))
                            }
                            putItemSlots.forEach {
                                clickEvent.inventory.setItem(it, ItemStack(Material.AIR))
                            }
                        }

                    }

                    onClose {
                        matchedBreakItem.clear()
                        baffle.reset(player.uniqueId.toString())
                        putItemSlots.forEach {slot ->
                            it.inventory.getItem(slot)?.let {itemStack ->
                                if (itemStack.type != Material.AIR)
                                    player.inventory.addItem(itemStack)
                            }
                        }
                    }
                }
            }
        }
    }

    @CommandBody(permission = "breakCard.command.reload")
    val reload = subCommand {
        execute<CommandSender> {sender, _, _ ->
            BreakCard.configManager.reload()
            BreakCard.breakItemManager.reload()
            BreakCard.handoutItemManager.reload()
            BreakCard.moneyItemManager.reload()
            sender.sendLang("重载成功")
        }
    }
}