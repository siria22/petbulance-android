package com.petbulance.data.mapper.feature.support

import com.petbulance.data.datasource.remote.network.feature.support.notice.dto.AdjacentNoticeDto
import com.petbulance.data.datasource.remote.network.feature.support.notice.dto.AttachmentDto
import com.petbulance.data.datasource.remote.network.feature.support.notice.dto.DetailNoticeResDto
import com.petbulance.data.datasource.remote.network.feature.support.notice.dto.NoticeButtonDto
import com.petbulance.data.datasource.remote.network.feature.support.notice.dto.NoticeListResDto
import com.petbulance.data.datasource.remote.network.feature.support.notice.dto.PagingNoticeListResDto
import com.petbulance.domain.model.feature.support.notice.AdjacentNotice
import com.petbulance.domain.model.feature.support.notice.Attachment
import com.petbulance.domain.model.feature.support.notice.NoticeButton
import com.petbulance.domain.model.feature.support.notice.NoticeButtonTarget
import com.petbulance.domain.model.feature.support.notice.NoticeDetail
import com.petbulance.domain.model.feature.support.notice.NoticeListItem
import com.petbulance.domain.model.feature.support.notice.PagingNoticeList
import com.petbulance.domain.model.type.NoticeStatusType

fun DetailNoticeResDto.toDomain() : NoticeDetail =   NoticeDetail(
    noticeId = noticeId,
    noticeStatus = NoticeStatusType.fromString(noticeStatus),
    title = title,
    createdAt = createdAt,
    content = content,
    attachments = attachments.map{ it.toDomain() },
    previousNotice = previousNotice?.toDomain(),
    nextNotice = nextNotice?.toDomain(),
    buttons = buttons?.map { it.toDomain() }
)

fun AttachmentDto.toDomain() : Attachment = Attachment(
    fileId = fileId,
    fileName = fileName,
    fileUrl = fileUrl,
    fileType = fileType
)

fun AdjacentNoticeDto.toDomain() : AdjacentNotice = AdjacentNotice(
    noticeId = noticeId,
    title = title
)

fun NoticeButtonDto.toDomain() : NoticeButton = NoticeButton(
    buttonId = buttonId,
    text = text,
    link = link,
    target = NoticeButtonTarget.fromString(target)
)

fun PagingNoticeListResDto.toDomain() : PagingNoticeList = PagingNoticeList(
    content = content.map{ it.toDomain() },
    hasNext = hasNext
)

fun NoticeListResDto.toDomain() : NoticeListItem = NoticeListItem(
    noticeId = noticeId,
    noticeStatus = NoticeStatusType.fromString(noticeStatus),
    title = title,
    content = content,
    createdAt = createdAt
)
