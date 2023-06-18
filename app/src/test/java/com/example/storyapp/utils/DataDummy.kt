import com.example.storyapp.api.ListStoryPagingResponse

object DataDummy {
    fun generateDummyStoryEntity(): List<ListStoryPagingResponse> {
        val storyList = ArrayList<ListStoryPagingResponse>()
        for (i in 0..5) {
            val story = ListStoryPagingResponse(
                "Title $i",
                "this is name",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                "This is description",
                "2022-02-22T22:22:22Z",
                null,
                null,
            )
            storyList.add(story)
        }
        return storyList
    }
}