package cn.gionrose.facered.api.plugin.list

/**
 * @description TODO
 * @author facered
 * @date 2023/11/9 22:11
 */
class ListStatus {

    class Add (val valueIsNull: Boolean, val notNeedAdd: Boolean, val defaultValueIsNull: Boolean, val addSuccess: Boolean)

    class Set (val indexNotInRange: Boolean, val valueIsNull: Boolean, val defaultValueIsNull: Boolean, val setSuccess: Boolean)

    class Get (val indexNotInRange: Boolean, val defaultValueIsNull: Boolean, val getSuccess: Boolean)

    class Remove (val valueIsNull: Boolean, val notNeedRemove: Boolean, val removeSuccess: Boolean)

    class RemoveAt (val indexNotInRange: Boolean, val removeAtSuccess: Boolean)
}