package huce.fit.appreadstories.story.list.follow

import huce.fit.appreadstories.model.Story

interface StoryFollowView {
    fun setDataFollow(storyList: List<Story>)
}
