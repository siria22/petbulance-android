package com.petbulance.data.repository.feature.notification

import com.petbulance.data.repository.MockFixtures
import com.petbulance.domain.model.feature.notification.DeleteAllResult
import com.petbulance.domain.model.feature.notification.NotificationItem
import com.petbulance.domain.model.feature.notification.PagingNotificationList
import com.petbulance.domain.model.feature.notification.ReadAllResult
import com.petbulance.domain.repository.feature.notification.NotificationRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class MockNotificationRepository @Inject constructor() : NotificationRepository {

    private var notifications: List<NotificationItem> = initialNotifications()

    override suspend fun getNotifications(
        lastNotificationId: Long?,
        pageSize: Int
    ): Result<PagingNotificationList> {
        delay(MockFixtures.NETWORK_DELAY_MS)
        val startIndex = lastNotificationId?.let { id -> notifications.indexOfFirst { it.notificationId == id } + 1 } ?: 0
        val page = notifications.drop(startIndex).take(pageSize)

        return Result.success(
            PagingNotificationList(
                content = page,
                hasNext = startIndex + page.size < notifications.size,
                lastNotificationId = page.lastOrNull()?.notificationId
            )
        )
    }

    override suspend fun readAllNotifications(): Result<ReadAllResult> {
        val unreadCount = notifications.count { !it.isRead }
        notifications = notifications.map { it.copy(isRead = true) }
        return Result.success(ReadAllResult(message = "모든 알림을 읽음 처리했습니다.", updatedCount = unreadCount))
    }

    override suspend fun deleteAllNotifications(): Result<DeleteAllResult> {
        val deletedCount = notifications.size
        notifications = emptyList()
        return Result.success(DeleteAllResult(message = "모든 알림을 삭제했습니다.", deletedCount = deletedCount))
    }

    /** 최근 알림이 앞에 온다. 대상 id는 MockFixtures의 게시글·후기와 맞춘다. */
    private fun initialNotifications(): List<NotificationItem> = listOfNotNull(
        commentNotification(
            id = 5, type = TYPE_COMMENT_REPLY, postId = 10, commentId = 83, isRead = false,
            message = { post, writer -> "\"${post}\" 글의 내 댓글에 ${writer}님이 댓글을 달았어요." }
        ),
        commentNotification(
            id = 4, type = TYPE_POST_COMMENT, postId = 9, commentId = 93, isRead = false,
            message = { post, writer -> "\"${post}\" 글에 ${writer}님이 댓글을 달았어요." }
        ),
        commentNotification(
            id = 3, type = TYPE_POST_COMMENT, postId = 7, commentId = 61, isRead = true,
            message = { post, writer -> "\"${post}\" 글에 ${writer}님이 댓글을 달았어요." }
        ),
        MockFixtures.findPost(9)?.let { post ->
            NotificationItem(
                notificationId = 2,
                type = TYPE_POST_LIKE,
                topic = post.boardLabel,
                createdAt = MockFixtures.notificationTimeLabel(POST_LIKE_MINUTES_AGO),
                message = "\"${post.title}\" 글에 좋아요가 ${post.likeCount}개 달렸어요.",
                isRead = true,
                targetType = TARGET_POST,
                targetId = post.id
            )
        },
        MockFixtures.findReview(11)?.let { review ->
            val hospitalName = MockFixtures.findHospital(review.hospitalId)?.name.orEmpty()
            NotificationItem(
                notificationId = 1,
                type = TYPE_REVIEW_LIKE,
                topic = REVIEW_TOPIC,
                createdAt = MockFixtures.notificationTimeLabel(REVIEW_LIKE_MINUTES_AGO),
                message = "${hospitalName}에 남긴 후기에 좋아요가 ${review.likeCount}개 달렸어요.",
                isRead = true,
                targetType = TARGET_REVIEW,
                targetId = review.id
            )
        }
    )

    private fun commentNotification(
        id: Long,
        type: String,
        postId: Long,
        commentId: Long,
        isRead: Boolean,
        message: (postTitle: String, writer: String) -> String
    ): NotificationItem? {
        val post = MockFixtures.findPost(postId) ?: return null
        val comment = MockFixtures.comments.find { it.id == commentId } ?: return null
        return NotificationItem(
            notificationId = id,
            type = type,
            topic = post.boardLabel,
            createdAt = MockFixtures.notificationTimeLabel(comment.minutesAgo),
            message = message(post.title, comment.writer),
            isRead = isRead,
            targetType = TARGET_COMMENT,
            targetId = post.id
        )
    }

    companion object {
        private const val TYPE_POST_COMMENT = "POST_COMMENT"
        private const val TYPE_COMMENT_REPLY = "COMMENT_REPLY"
        private const val TYPE_POST_LIKE = "POST_LIKE"
        private const val TYPE_REVIEW_LIKE = "REVIEW_LIKE"
        private const val TARGET_POST = "POST"
        private const val TARGET_COMMENT = "COMMENT"
        private const val TARGET_REVIEW = "REVIEW"
        private const val REVIEW_TOPIC = "병원 후기"
        private const val POST_LIKE_MINUTES_AGO = 1_500L
        private const val REVIEW_LIKE_MINUTES_AGO = 4_320L
    }
}
