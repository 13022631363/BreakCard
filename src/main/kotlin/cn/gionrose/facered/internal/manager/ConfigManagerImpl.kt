package cn.gionrose.facered.internal.manager

import cn.gionrose.facered.api.manager.BreakCardConfigManager
import cn.gionrose.facered.api.plugin.map.MapStatus
import org.bukkit.configuration.file.YamlConfiguration
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.console

/**
 * @description TODO
 * @author facered
 * @date 2023/11/10 13:00
 */
object ConfigManagerImpl: BreakCardConfigManager() {



    private fun debugMode ()
    {
        this.debugGet("config") {_, config ->
            if (this.getSuccess && !this.keyIsNull)
                debug = config!!.getBoolean("debug")
        }
    }

    private fun breakMenuTitle ()
    {
        this.debugGet("config") {_, config ->
            if (this.getSuccess && this.defaultValueIsNull)
                breakMenuTitle = config!!.getString("breakMenuTitle")!!
        }
    }



    private fun handoutItem ()
    {
        this.debugGet("breakItem"){_, breakConfig ->
            if (this.getSuccess && this.defaultValueIsNull)
                handoutItem = breakConfig!!.getString("handoutItemId")!!
        }
    }

    override fun reload() {
        this.debugRemoveAll()
        this.onLoad()
    }



    @Awake(LifeCycle.LOAD)
    override fun onLoad ()
    {
        super.onLoad()
        debugMode()
        breakMenuTitle ()
        handoutItem ()
    }

    override fun debug(removeStatus: MapStatus.Remove, key: String?, result: YamlConfiguration?) {
        if (removeStatus.removeSuccess)
            debug {
                console ().sendMessage("[configManager] 移除 $key")
            }
    }

    override fun debug(getStatus: MapStatus.Get, key: String?, result: YamlConfiguration?) {
        if (getStatus.getSuccess)
            if (getStatus.defaultValueIsNull)
                debug { console ().sendMessage("[configManager] 获取 [key] : [$key]") }
            else debug { console ().sendMessage("[configManager] 获取默认值 [key] : [$key]") }
    }

    override fun debug(putStatus: MapStatus.Put, key: String?, result: YamlConfiguration?) {
        if (putStatus.putSuccess)
            if (putStatus.defaultValueIsNull)
                debug { console ().sendMessage("[configManager] 加入 [key] : [$key]") }
            else debug { console ().sendMessage("[configManager] 加入默认值 [key] : [$key]") }
    }

    override fun debug (debug: () -> Unit)
    {
        if (this.debug)
            debug ()
    }

}