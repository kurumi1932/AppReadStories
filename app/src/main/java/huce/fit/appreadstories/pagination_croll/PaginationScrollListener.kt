package huce.fit.appreadstories.pagination_croll

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationScrollListener(private val linearLayoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = linearLayoutManager.childCount
        val totalItemCount = linearLayoutManager.getItemCount()
        val firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()
        if (isLoading() || isLastPage()) {
            return
        }
        if (firstVisibleItemPosition >= 0 && visibleItemCount + firstVisibleItemPosition >= totalItemCount) {
            loadMoreItems()
        }
    }

    abstract fun loadMoreItems()

    abstract fun isLoading(): Boolean

    abstract fun isLastPage(): Boolean
}
