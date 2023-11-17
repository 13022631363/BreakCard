package cn.gionrose.facered.api.plugin.list

/**
 * @description TODO
 * @author facered
 * @date 2023/11/9 22:26
 */
interface DebugListMode<OBJ> {

    fun debug (addStatus: ListStatus.Add, result: OBJ?)

    fun debug (removeStatus: ListStatus.Remove, result: OBJ?)

    fun debug (removeAtStatus: ListStatus.RemoveAt, index: Int, result: OBJ?)

    fun debug (getStatus: ListStatus.Get, index: Int, result: OBJ?)

    fun debug (setStatus: ListStatus.Set, index: Int, result: OBJ?)
}