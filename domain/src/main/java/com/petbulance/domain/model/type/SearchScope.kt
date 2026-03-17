package com.petbulance.domain.model.type

/**
 * 검색 대상 범위
 * 서버 API 분석 결과: author가 아닌 writer를 사용
 */
enum class SearchScope(val korean: String, val value: String) {
    // 게시글 검색
    TITLE_CONTENT("글제목+내용", "title_content"),
    TITLE("글제목", "title"),
    WRITER("글작성자", "writer"),
    
    // 댓글 검색
    COMMENT_CONTENT("댓글내용", "content");

    companion object {
        fun fromValue(value: String?): SearchScope {
            return entries.find { it.value.equals(value, ignoreCase = true) } 
                ?: TITLE_CONTENT
        }
        
        fun getPostSearchScopes(): List<SearchScope> {
            return listOf(TITLE_CONTENT, TITLE, WRITER)
        }
        
        fun getCommentSearchScopes(): List<SearchScope> {
            return listOf(COMMENT_CONTENT, WRITER)
        }
    }
}
