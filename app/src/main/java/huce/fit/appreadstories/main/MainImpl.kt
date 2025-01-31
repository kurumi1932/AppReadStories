package huce.fit.appreadstories.main

class MainImpl : MainPresenter {

    companion object {
        private const val TAG: String = "MainImpl"
        private const val NUMBER = 1
    }

    private val fragmentOld = HashMap<Int, Int>()
    private var count = NUMBER

    override fun addFragment(id: Int) {
        fragmentOld[count] = id
    }

    override fun changeFragment(id: Int) {
        if (count == 0) {
            count = NUMBER
        }
        fragmentOld[++count] = id
    }

    override fun backPressed(): Int {
        return --count
    }

    override fun getId(): Int {
        return fragmentOld[count]!!
    }

    override fun removeCount() {
        if (count != NUMBER) {
            fragmentOld.remove(count)
        }
    }
}