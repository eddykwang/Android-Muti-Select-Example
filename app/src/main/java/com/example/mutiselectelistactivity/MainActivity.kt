package com.example.mutiselectelistactivity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mTracker: SelectionTracker<Long> by lazy {
        SelectionTracker.Builder<Long>(
            "default",
            message_list_recyclerview,
            StableIdKeyProvider(message_list_recyclerview),
            MessageItemLookUp(message_list_recyclerview),
            StorageStrategy.createLongStorage()
        )
            .withSelectionPredicate(SelectionPredicates.createSelectAnything())
            .build()
    }

    private val mAdapter by lazy { SelectableListAdapter(arrayListOf()) }
    private var start = 1
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = " Messaging"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initView()
    }

    private fun initView() {
        message_list_recyclerview.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }.let {
            it.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visbileItemCount: Int = it.layoutManager!!.childCount
                    val totalItemCount = it.layoutManager!!.itemCount
                    val firstVisibleItemPos = (it.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                    if (!isLoading) {
                        if ((visbileItemCount + firstVisibleItemPos >= totalItemCount) && firstVisibleItemPos >= 0 ) {
                            isLoading = true
                            loadMoreMessage(start)
                        }
                    }
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                }
            })
        }

        mAdapter.tracker = mTracker

        mTracker.addObserver(object : SelectionTracker.SelectionObserver<Long>() {

            override fun onItemStateChanged(key: Long, selected: Boolean) {
                setupToolBar()
            }

            override fun onSelectionChanged() {
                setupToolBar()
            }
        })

        refesh_fab.setOnClickListener {

        }

        pull_to_refresh.setOnRefreshListener {
            pull_to_refresh.postDelayed(
                {
                    start = 0
                    mAdapter.list.clear()
                    mAdapter.list.addAll(MessageManager.getMessage(0))
                    mAdapter.notifyDataSetChanged()
                    pull_to_refresh.isRefreshing = false
                }, 500
            )
        }
    }

    private fun loadMoreMessage(page: Int) {

        pull_to_refresh.isRefreshing = true

        message_list_recyclerview.postDelayed(
            {
                val oldListSize = mAdapter.list.size
                mAdapter.list.addAll(MessageManager.getMessage(page))
                mAdapter.notifyItemRangeChanged(oldListSize, 20)
                start++
                pull_to_refresh.isRefreshing = false
                isLoading = false
                message_list_recyclerview.scrollToPosition(oldListSize)
            }, 1000
        )

    }

    private fun setupToolBar() {
        if (mTracker.hasSelection()) {
            pull_to_refresh.isEnabled = false
            val selectedAmount = mTracker.selection.size()
            if (selectedAmount > 1)
                supportActionBar?.title = " $selectedAmount Messages Selected"
            else
                supportActionBar?.title = " $selectedAmount Message Selected"
            supportActionBar?.setHomeAsUpIndicator(R.drawable.abc_ic_clear_material)
        } else {
            pull_to_refresh.isEnabled = true
            supportActionBar?.title = " Messaging"
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp)
        }

        invalidateOptionsMenu()

    }

    override fun onBackPressed() {
        if (mTracker.selection.size() > 0) {
            mTracker.clearSelection()
            return
        }
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (mTracker.hasSelection())
            menuInflater.inflate(R.menu.selection_mode_menu, menu)
        else
            menuInflater.inflate(R.menu.messaging_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if (mTracker.hasSelection()) {
                    mTracker.clearSelection()
                } else {
                    onBackPressed()
                }
                true
            }
            R.id.delete ->
                deleteSelectedItem()
            else ->
                false
        }
    }

    private fun deleteSelectedItem(): Boolean {
        val newList = mAdapter.list.toList()
        if (mTracker.hasSelection()) {
            mTracker.selection.forEach {
                mAdapter.list.remove(newList[it.toInt()])
            }
            mAdapter.notifyDataSetChanged()
            mTracker.clearSelection()

        }
        return true
    }
}
