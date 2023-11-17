package cn.gionrose.facered.api.plugin.map

/**
 * @description 赋予 debug 功能
 * @author facered
 * @date 2023/11/9 13:39
 */
interface DebugMapMode<K,V>{

    /**
     * @param removeStatus MapStatus.Remove 持有此次 remove 行为的一些关键信息
     * @param key 需要移除的 key
     * @param result 移除的 value
     *
     * 1. 当调用 debugRemove 方法时会在 callback 前调用这个方法 跟callback一模一样
     * 2. 初始目的是让 remove 行为会根据 mapStatus 的信息来输出日志
     */
    fun debug (removeStatus: MapStatus.Remove, key: K?, result: V? )

    /**
     * @param getStatus MapStatus.Get 持有此次 get 行为的一些关键信息
     * @param key 需要获取的 key
     * @param result 获取的 value
     *
     * 1. 当调用 debugPut 方法时会在 callback 前调用这个方法 跟callback一模一样
     * 2. 初始目的是让 put 行为会根据 mapStatus 的信息来输出日志
     */
    fun debug (getStatus: MapStatus.Get, key: K?, result: V? )

    /**
     * @param putStatus MapStatus.Put 持有此次 put 行为的一些关键信息
     * @param key 需要放入的 key
     * @param result 放入的 value
     *
     * 1. 当调用 debugPut 方法时会在 callback 前调用这个方法 跟callback一模一样
     * 2. 初始目的是让 put 行为会根据 mapStatus 的信息来输出日志
     */
    fun debug (putStatus: MapStatus.Put, key: K?, result: V? )
}