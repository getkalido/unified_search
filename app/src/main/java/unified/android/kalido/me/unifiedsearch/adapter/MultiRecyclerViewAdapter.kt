package unified.android.kalido.me.unifiedsearch.adapter

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import android.util.ArrayMap
import android.view.ViewGroup
import unified.android.kalido.me.unifiedsearch.adapter.BlankRecyclerView.FilteredAdapter
import java.util.TreeSet

class MultiRecyclerViewAdapter<FILTER> : RecyclerView.Adapter<RecyclerView.ViewHolder>, FilteredAdapter<Any> {
    private val mAdapters: MutableList<RecyclerView.Adapter<RecyclerView.ViewHolder>> = ArrayList()
    private var mIsAttachedToRecyclerView: Boolean = false
    private var mFilterConstraint: FILTER? = null
    private val mViewTypeMap: ArrayList<ViewTypeParams> = ArrayList      ()
    private val mAdapterViewTypeMap: HashMap<Int, HashMap<Int, ViewTypeParams>> = HashMap<Int, HashMap<Int, ViewTypeParams>>()

    constructor(filterConstraint: FILTER) {
        mFilterConstraint = filterConstraint
    }

    constructor(filterConstraint: FILTER, adapters: MutableList<RecyclerView.Adapter<*>>) {
        mFilterConstraint = filterConstraint
        adapters.map {
            addAdapter(it)
        }
    }

    fun getFilterConstraint() = mFilterConstraint

    fun setFilterConstraint(filterConstraint: FILTER?) {
        mFilterConstraint = filterConstraint
        mAdapters.map { adapter ->
            when (adapter) {
                is AdpaterFilterConstrainer<*> -> {
                    adapter.filterConstraint = filterConstraint
                }
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        mIsAttachedToRecyclerView = true
        mAdapters.forEach {
            it.onAttachedToRecyclerView(recyclerView)
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        mIsAttachedToRecyclerView = false
        mAdapters.forEach {
            it.onDetachedFromRecyclerView(recyclerView)
        }
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(hasStableIds)
        mAdapters.forEach {
            it.setHasStableIds(hasStableIds)
        }
    }

    override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        super.registerAdapterDataObserver(observer)
        mAdapters.forEach {
            it.registerAdapterDataObserver(observer)
        }
    }

    override fun unregisterAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        super.unregisterAdapterDataObserver(observer)
        mAdapters.forEach {
            it.unregisterAdapterDataObserver(observer)
        }
    }

    override fun getItemViewType(position: Int): Int {
        var index = position
        mAdapters.forEachIndexed { adapterIndex, adapter ->
            if (index < adapter.itemCount) {
                val type = adapter.getItemViewType(index)
                val param = mAdapterViewTypeMap.get(adapterIndex)?.get(type)?.let {
                    return@let it
                } ?: run {
                    val tParam = ViewTypeParams(adapterIndex, type)
                    mViewTypeMap.add(tParam)
                    mAdapterViewTypeMap.get(adapterIndex)?.put(type, tParam)
                            ?: run {
                                val map = HashMap<Int, ViewTypeParams>()
                                map.put(type, tParam)
                                mAdapterViewTypeMap.put(adapterIndex, map)
                            }
                    return@run tParam
                }
                return mViewTypeMap.indexOf(param)
            } else {
                index -= adapter.itemCount
            }
        }
        throw IllegalArgumentException("not found view type in adapters")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val params = mViewTypeMap.toTypedArray()[viewType]
        val adapter = mAdapters.get(params.adapterIndex)
        return adapter.onCreateViewHolder(parent, params.viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var offset = 0
        mAdapters.forEach {
            if (position - offset < it.itemCount) {
                it.onBindViewHolder(holder, position - offset)
                return
            } else {
                offset += it.itemCount
            }
        }
    }

    override fun getItemCount(): Int {
        return mAdapters.sumBy { it.itemCount }
    }

    override fun getItemId(position: Int): Long {
        var offset = 0
        mAdapters.forEach {
            if (position - offset < it.itemCount) {
                return it.getItemId(position - offset)
            } else {
                offset += it.itemCount
            }
        }
        return RecyclerView.NO_ID
    }

    /**
     * Add chatAdapter into MixAdapter
     */
    fun addAdapter(recyclerAdapter: RecyclerView.Adapter<*>?) {
        recyclerAdapter?.let { adapter ->
            (adapter as Adapter<ViewHolder>?)?.let {
                mAdapters.add(it)
                if (mIsAttachedToRecyclerView)
                    notifyDataSetChanged()
            }
        }
    }

    override fun getFilteredData(): java.util.ArrayList<Any> {
        val list = ArrayList<Any>()
        mAdapters.forEach {
            when (it) {
                is FilteredAdapter<*> ->
                    list.addAll(it.filteredData)
                else -> {
                }
            }
        }
        return list
    }

    override fun isFiltered(): Boolean {
        mAdapters.forEach {
            when (it) {
                is FilteredAdapter<*> ->
                    return it.isFiltered
                else -> {
                }
            }
        }
        return false
    }

    /**
     * Get start position of given chatAdapter in MixAdapter
     */
    fun getAdapterOffset(target: RecyclerView.Adapter<*>): Int {
        var offset = 0
        for (adapter in mAdapters) {
            if (adapter == target) {
                return offset
            }
            offset += adapter.itemCount
        }
        return offset
    }

    class ViewTypeParams constructor(val adapterIndex: Int, val viewType: Int) : Comparable<ViewTypeParams> {
        override fun compareTo(other: ViewTypeParams): Int {
            if (adapterIndex != other.adapterIndex) {
                return adapterIndex.compareTo(other.adapterIndex)
            } else {
                return viewType.compareTo(other.viewType)
            }
        }

        override fun equals(other: Any?): Boolean {
            return when (other) {
                is ViewTypeParams? -> {
                    return other?.adapterIndex == adapterIndex
                            && other?.viewType == viewType
                }
                else -> false
            }
        }

        override fun hashCode(): Int {
            return (adapterIndex.toLong() * 37 + viewType.toLong() * 37).toInt()
        }
    }
}