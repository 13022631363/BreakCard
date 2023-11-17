package cn.gionrose.facered.api.plugin.list

/**
 * @description TODO
 * @author facered
 * @date 2023/11/9 22:05
 */
abstract class BaseDebugList<OBJ>: ArrayList<OBJ> (), DebugListMode<OBJ>{

    fun debugAdd (value: OBJ?, defaultValue: OBJ? = null, callback: ListStatus.Add.(OBJ?)-> Unit = {})
    {
        var valueIsNull = false
        var defaultValueIsNull = false
        var notNeedAdd = false
        var addSuccess = false
        var result: OBJ? = null


        if (value == null)
            valueIsNull = true
        if (defaultValue == null)
            defaultValueIsNull = true

        if (!valueIsNull)
        {
            if (contains(value))
                notNeedAdd = true
            else
            {
                this.add(value!!)
                addSuccess = true
            }
            result = value
        }
        else if (!defaultValueIsNull)
        {
            if (contains(defaultValue))
                notNeedAdd = true
            else
            {
                this.add(defaultValue!!)
                addSuccess = true
            }
            result = defaultValue
        }

        val addStatus = ListStatus.Add (valueIsNull, notNeedAdd, defaultValueIsNull, addSuccess)

        debug(addStatus, result)
        callback (addStatus, result)

    }

    fun debugRemove (value: OBJ?, callback: ListStatus.Remove.(OBJ?) -> Unit = {})
    {
        var valueIsNull = false
        var removeSuccess = false
        var notNeedRemove = false

        if (value == null)
            valueIsNull = true

        if (!valueIsNull)
        {
            removeSuccess = this.remove(value)
            if (!removeSuccess)
                notNeedRemove = true
        }

        val removeStatus = ListStatus.Remove (valueIsNull, notNeedRemove, removeSuccess)

        debug(removeStatus, value)
        callback (removeStatus, value)
    }

    fun debugRemoveAt (index: Int, callback: ListStatus.RemoveAt.(Int, OBJ?) -> Unit = {_, _ -> })
    {
        var indexNotInRange = false
        var removeAtSuccess = false
        var result: OBJ? = null


        if (index !in 0 until size)
            indexNotInRange = true
        else
        {
            result = this.removeAt(index)
            removeAtSuccess = true
        }

        val removeAtStatus = ListStatus.RemoveAt(indexNotInRange, removeAtSuccess)

        debug(removeAtStatus, index, result)
        callback (removeAtStatus, index, result)
    }

    fun debugGet (index: Int, defaultValue: OBJ? = null, callback: ListStatus.Get.(Int, OBJ?) -> Unit = {_, _ -> })
    {
        var indexNotInRange = false
        var getSuccess = false
        var defaultValueIsNull = false
        var result: OBJ? = null

        if (defaultValue == null)
            defaultValueIsNull = true

        if (index !in 0 until size)
        {
            indexNotInRange = true
            if (!defaultValueIsNull)
                result = defaultValue
        }
        else
        {
            result = this[index]
            getSuccess = true
        }

        val getStatus = ListStatus.Get (indexNotInRange, defaultValueIsNull, getSuccess)

        debug(getStatus, index, result)
        callback (getStatus, index, result)
    }

    fun debugSet (index: Int, value: OBJ?, defaultValue: OBJ? = null, callback: ListStatus.Set.(Int, OBJ?) -> Unit = {_, _ ->} )
    {
        var indexNotInRange = false
        var valueIsNull = false
        var defaultValueIsNull = false
        var setSuccess = false
        var result: OBJ? = null

        if (index !in 0 until size)
            indexNotInRange = true
        if (value == null)
            valueIsNull = true
        if (defaultValue == null)
            defaultValueIsNull = true

        if (!indexNotInRange)
        {
            if (valueIsNull)
            {
                if (!defaultValueIsNull)
                {
                    result = defaultValue
                    this[index] = result!!
                    setSuccess = true
                }
            }
            else
            {
                result = value
                this[index] = result!!
                setSuccess = true
            }
        }

        val setStatus = ListStatus.Set (indexNotInRange, valueIsNull, defaultValueIsNull, setSuccess)

        callback (setStatus, index, result)
        debug (setStatus, index, result)
    }


    fun debugReMoveAll ()
    {
        val temp = mutableListOf<OBJ>()
        temp.addAll(this)
        temp.forEach (::debugRemove)
    }
}