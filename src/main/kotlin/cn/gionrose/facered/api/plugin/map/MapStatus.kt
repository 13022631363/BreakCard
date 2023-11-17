package cn.gionrose.facered.api.plugin.map

/**
 * @description TODO
 * @author facered
 * @date 2023/11/8 22:29
 */
class MapStatus {

    class Put (val keyIsNull: Boolean, val valueIsNull: Boolean, val defaultValueIsNull: Boolean, val putSuccess: Boolean)

    class Get (val keyIsNull: Boolean, val keyNotFind: Boolean, val defaultValueIsNull: Boolean, val getSuccess: Boolean)

    class Remove (val keyIsNull: Boolean, val notNeedRemove: Boolean, val keyNotFind: Boolean, val removeSuccess: Boolean)
}