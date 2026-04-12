package com.petbulance.presentation.screen.feature.mypage.sections.help.notice.detail

import com.petbulance.domain.model.feature.support.notice.NoticeDetail

data class MyPageNoticeDetailData(
    val noticeDetail: NoticeDetail?
) {
    companion object {
        val empty = MyPageNoticeDetailData(noticeDetail = null)
    }
}
