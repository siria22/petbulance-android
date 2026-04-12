package com.petbulance.presentation.screen.feature.mypage.sections.help.notice.list

import com.petbulance.domain.model.feature.support.notice.NoticeListItem

data class MyPageNoticeData(
    val notices: List<NoticeListItem>,
    val isLoadingNextPage: Boolean
) {
    companion object {
        val empty = MyPageNoticeData(
            notices = emptyList(),
            isLoadingNextPage = false
        )

        fun stub() = MyPageNoticeData(
            notices = NoticeListItem.stubs(),
            isLoadingNextPage = false
        )
    }
}