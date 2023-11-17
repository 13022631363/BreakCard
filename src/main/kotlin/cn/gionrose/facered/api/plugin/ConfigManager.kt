package cn.gionrose.facered.api.plugin

import cn.gionrose.facered.api.plugin.map.BaseDebugMap
import org.bukkit.configuration.file.YamlConfiguration
import taboolib.common.platform.function.getDataFolder
import java.io.File

/**
 * @description TODO
 * @author facered
 * @date 2023/11/9 13:48
 */
abstract class ConfigManager: BaseDebugMap<String, YamlConfiguration>() {

    /**
     * 插件文件夹
     */
    val pluginDir: File by lazy { getDataFolder()}


    open fun onLoad ()
    {
        load (pluginDir)
    }

    private fun load (file: File)
    {
        if (file.isDirectory)
        {
            file.listFiles()?.forEach {
                load (it)
            }
        }
        if (file.name.endsWith(".yml") && file.name != "lang.yml")
        {
            this.debugPut (file.nameWithoutExtension, YamlConfiguration.loadConfiguration(file))
        }
    }

    fun ifNotExistsCreate (fileName: String)
    {
        val file = File(pluginDir, fileName)
        if (!file.exists())
        {
            file.createNewFile()
        }
    }



}