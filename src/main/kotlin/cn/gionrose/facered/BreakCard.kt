package cn.gionrose.facered


import cn.gionrose.facered.api.manager.BreakCardConfigManager
import cn.gionrose.facered.api.manager.BreakItemManager
import cn.gionrose.facered.api.manager.HandoutItemManager
import cn.gionrose.facered.api.manager.MoneyItemManager
import cn.gionrose.facered.internal.manager.BreakItemManagerImpl
import cn.gionrose.facered.internal.manager.ConfigManagerImpl
import cn.gionrose.facered.internal.manager.HandoutItemManagerImpl
import cn.gionrose.facered.internal.manager.MoneyItemManagerImpl
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.Plugin
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile


/**
 * @description 主类
 * @author facered
 * @date 2023/11/8 12:04
 */
object BreakCard : Plugin() {

    val configManager: BreakCardConfigManager = ConfigManagerImpl

    val breakItemManager: BreakItemManager =  BreakItemManagerImpl

    val moneyItemManager: MoneyItemManager = MoneyItemManagerImpl

    val handoutItemManager: HandoutItemManager = HandoutItemManagerImpl

    @Config (migrate = true)
    lateinit var config: ConfigFile


    @Config (value = "break/breakItem.yml", migrate = true)
    lateinit var breakItem: ConfigFile
    @Awake (LifeCycle.ENABLE)
    fun enable ()
    {

    }
}