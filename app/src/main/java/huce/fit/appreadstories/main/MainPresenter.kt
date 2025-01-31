package huce.fit.appreadstories.main

interface MainPresenter {
    fun addFragment(id: Int)
    fun changeFragment(id: Int)
    fun backPressed(): Int
    fun getId(): Int
    fun removeCount()
}
