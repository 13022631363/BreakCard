package cn.gionrose.facered.api.plugin.map

import pers.neige.neigeitems.utils.ConfigUtils.clone

/**
 * @description 1. 可以根据 PUT REMOVE GET 行为 来做包括但不限于 输出日志功能的 Map
 *              2. 假如使用 debugPut 方法时 根据传入的参数来判断各个参数是否为 null
 *                  1. 如果是 null 那么 putStatus 中的 各个指定的值会设置为 true
 *                  2. 如果 key 和 value 不是 null / key 和 defaultValue 不是null
 *                  3. 那么 putStatus 中的 putSuccess 会设置为 true
 *
 *                     举例 debugPut ("name", "facered")
 *                     解释 此时 callback 中的
 *                                  1. mapStatus 中的
 *                                          keyIsNull = false
 *                                          valueIsNull = false
 *                                          defaultValueIsNull = true
 *                                          getSuccess = true
 *                                  2. key 传入的 name
 *                                  3. value 传入的 value 也就是 facered
 *
 *                    举例 debugPut ("name", null, "cao")
 *                    解释 此时 callback 中的
 *                                  1. mapStatus 中的
 *                                          keyIsNull = false
 *                                          valueIsNull = true
 *                                          defaultValueIsNull = false
 *                                          getSuccess = true
 *                                  2. key 传入的 name
 *                                  3. value 传入的 defaultValue 也就是 cao
 *
 * @author facered
 * @date 2023/11/8 21:27
 */
abstract class BaseDebugMap<K, V>: HashMap<K, V> (), DebugMapMode<K, V> {


    /**
     * @param key
     * @param value
     * @param defaultValue
     * @param callBack
     *
     * 1. key value defaultValue 都可以为 null
     * 2. 当 key value 都不为 null 时才会真正放入 (意味着此 map 是非 null 的)
     * 3. 当 key defaultValue 不为 null 但 value 为 null 选择存入 defaultValue
     * 4. 当 key value defaultValue 都不为 null 时选择存入 value
     *
     * 5. callback 中持有 3 个参数
     *      1. MapStatus.Put 持有此次 put 行为的一些关键信息
     *      2. key
     *      3. value
     * 6. callback 如果不传入那么将什么都不做
     */
    open fun debugPut(key: K?, value: V?, defaultValue: V? = null,callBack: MapStatus.Put.(K?, V?) -> Unit = { _, _ -> }){
        var keyIsNull = false
        var valueIsNull = false
        var putSuccess = false
        var defaultValueIsNull = false



        if (key == null)
            keyIsNull = true
        if (value == null)
            valueIsNull = true
        if (defaultValue == null)
            defaultValueIsNull = true

        if (!keyIsNull && !valueIsNull)
        {
            put(key!!,value!!)
            putSuccess = true
        }else if (!keyIsNull && !defaultValueIsNull)
        {
            put(key!!,defaultValue!!)
            putSuccess = true
        }

        val putStatus = MapStatus.Put(
            keyIsNull = keyIsNull,
            valueIsNull = valueIsNull,
            defaultValueIsNull = defaultValueIsNull,
            putSuccess = putSuccess
        )
        debug(putStatus, key, value)
        callBack (putStatus, key, value)
    }

    /**
     * @param key
     * @param defaultValue
     * @param callBack
     *
     * 1. key defaultValue 可以为 null
     * 2. 当 key 不为 null 时或者 defaultValue 不为 null 时才算获取成功 (当然后者代表返回了你给的默认值)
     *
     * 3. callback 中持有 3 个参数
     *      1. MapStatus.Get 持有此次 get 行为的一些关键信息
     *      2. key
     *      3. get 到的 value (当 key 是 null / key 不在 map 中 则返回 defaultValue)
     *
     * 4. callback 如果不传入那么将什么都不做
     */
    fun debugGet (key: K?, defaultValue: V? = null, callBack: MapStatus.Get.(K?, V?) -> Unit = { _, _ -> } )
    {
        var keyIsNull = false
        var keyNotFind = false
        var defaultValueIsNull = false
        var getSuccess = false
        var result: V? = null

        if (key == null)
            keyIsNull = true

        if (key != null)
        {
            result = super.get(key)

            if (result != null)
                getSuccess = true
            else
                keyNotFind = true

            if (result == null && defaultValue != null)
            {
                getSuccess = true
                result = defaultValue
            }

            if (defaultValue == null)
                defaultValueIsNull = true

        }

        val getStatus = MapStatus.Get(keyIsNull, keyNotFind, defaultValueIsNull, getSuccess)
        debug(getStatus, key, result)
        callBack (getStatus, key, result)
    }

    /**
     * @param key
     * @param callBack
     *
     * 1. key 可以是 null
     * 2. 当 key 不为 null 时 remove 才会成功
     *
     * 3. callback 中持有 3 个参数
     *      1. MapStatus.Remove 持有此次 remove 行为的一些关键信息
     *      2. key
     *      3. 移除的 value
     */
    fun debugRemove (key: K?, callBack: MapStatus.Remove.(K?, V?) -> Unit = { _, _ -> })
    {
        var keyIsNull = false
        var keyNotFind = false
        var notNeedRemove = false
        var removeSuccess = false

        var result: V? = null

        if (key == null)
        {
            keyIsNull = true
            notNeedRemove = true
        }else
        {
            result = super.remove(key)

            if (result == null)
                keyNotFind = true
            else
                removeSuccess = true

        }

        val removeStatus = MapStatus.Remove(keyIsNull, notNeedRemove, keyNotFind, removeSuccess)

        debug(removeStatus, key, result)
        callBack (removeStatus, key, result)
    }

    fun debugPutAll (map: Map<K, V>): BaseDebugMap<K, V>
    {
        map.forEach { (key, value) ->
            debugPut(key, value)
        }
        return this
    }

    fun debugRemoveAll (): BaseDebugMap<K, V>
    {
        val keys = this.keys.toMutableList().clone() as MutableList<K>
        keys.forEach { key ->
            debugRemove(key)
        }
        return this
    }

}