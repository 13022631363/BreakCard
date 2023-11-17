package cn.gionrose.facered.api

import cn.gionrose.facered.api.PrefixType.*

/**
 * @description TODO
 * @author facered
 * @date 2023/11/15 11:16
 */
class ItemData (var amount: Int = 0, var itemId: String = "", var prefix: PrefixType = NI)
{
    override fun toString(): String {
        return "ItemData(amount=$amount, itemId='$itemId', prefix=$prefix)"
    }
}
