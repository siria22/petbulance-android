package com.petbulance.presentation.screen.feature.community.search

import com.petbulance.domain.model.feature.community.comment.SearchPostCommentRes
import com.petbulance.domain.model.feature.community.post.PostSearchSummary

data class CommunitySearchData(
    val searchKeyword: String,
    val searchScope: String,
    val currentSort: String,
    val selectedAnimalCategory: String?,
    val selectedPostCategory: String?,
    val postResults: List<PostSearchSummary>,
    val commentResults: List<SearchPostCommentRes>,
    val hasNextPost: Boolean,
    val hasNextComment: Boolean,
    val totalPostCount: Long,
    val totalCommentCount: Long
) {
    companion object {
        val empty = CommunitySearchData(
            searchKeyword = "",
            searchScope = "title_content", // 게시글 검색 기본값
            currentSort = "latest",
            selectedAnimalCategory = null,
            selectedPostCategory = null,
            postResults = emptyList(),
            commentResults = emptyList(),
            hasNextPost = false,
            hasNextComment = false,
            totalPostCount = 0L,
            totalCommentCount = 0L
        )

        fun stub() = CommunitySearchData(
            searchKeyword = "테스트 검색어",
            searchScope = "title_content",
            currentSort = "latest",
            selectedAnimalCategory = null,
            selectedPostCategory = null,
            postResults = emptyList(),
            commentResults = emptyList(),
            hasNextPost = false,
            hasNextComment = false,
            totalPostCount = 0L,
            totalCommentCount = 0L
        )
    }
    
    fun getCommentSearchScope(): String {
        // 댓글 검색 시 기본값은 content
        return if (searchScope == "title_content" || searchScope == "title") {
            "content"
        } else {
            searchScope
        }
    }
    
    val isFilterApplied: Boolean
        get() = selectedAnimalCategory != null || selectedPostCategory != null
    
    val filterDisplayText: String
        get() {
            val animalKorean = selectedAnimalCategory?.let { name ->
                com.petbulance.domain.model.type.AnimalCategory.entries.find { it.name == name }?.korean ?: name
            }
            val postKorean = selectedPostCategory?.let { name ->
                com.petbulance.domain.model.type.PostCategory.entries.find { it.name == name }?.korean ?: name
            }
            
            return when {
                animalKorean != null && postKorean != null -> 
                    "$animalKorean, $postKorean"
                animalKorean != null -> animalKorean
                postKorean != null -> postKorean
                else -> "필터"
            }
        }
}
