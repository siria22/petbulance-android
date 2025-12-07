package com.example.data.di

import com.example.data.repository.ExampleRepositoryImpl
import com.example.data.repository.feature.community.board.MockBoardRepository
import com.example.data.repository.feature.community.comment.MockCommentRepository
import com.example.data.repository.feature.community.post.MockPostRepository
import com.example.data.repository.feature.community.recent.MockRecentRepository
import com.example.data.repository.feature.hospital.history.MockHistoryRepository
import com.example.data.repository.feature.hospital.hospital.MockHospitalRepository
import com.example.data.repository.feature.hospital.review.MockReviewRepository
import com.example.data.repository.feature.support.inquiry.MockInquiryRepository
import com.example.data.repository.feature.support.notice.MockNoticeRepository
import com.example.data.repository.feature.support.qna.QnaRepositoryImpl
import com.example.data.repository.feature.user.MockUserRepository
import com.example.data.repository.nonfeature.app.MockAppInfoRepository
import com.example.data.repository.nonfeature.device.MockDeviceRepository
import com.example.data.repository.nonfeature.preference.PreferenceRepositoryImpl
import com.example.domain.repository.feature.ExampleRepository
import com.example.domain.repository.feature.community.BoardRepository
import com.example.domain.repository.feature.community.CommentRepository
import com.example.domain.repository.feature.community.PostRepository
import com.example.domain.repository.feature.community.RecentRepository
import com.example.domain.repository.feature.hospital.HistoryRepository
import com.example.domain.repository.feature.hospital.HospitalRepository
import com.example.domain.repository.feature.hospital.ReviewRepository
import com.example.domain.repository.feature.support.InquiryRepository
import com.example.domain.repository.feature.support.NoticeRepository
import com.example.domain.repository.feature.support.QnaRepository
import com.example.domain.repository.feature.user.UserRepository
import com.example.domain.repository.nonfeature.app.AppInfoRepository
import com.example.domain.repository.nonfeature.device.DeviceRepository
import com.example.domain.repository.nonfeature.preference.PreferenceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindExampleRepository(
        impl: ExampleRepositoryImpl
    ): ExampleRepository

    @Binds
    @Singleton
    abstract fun bindPreferenceRepository(
        impl: PreferenceRepositoryImpl
    ): PreferenceRepository

    @Binds
    @Singleton
    abstract fun bindAppInfoRepository(
        mock: MockAppInfoRepository
//        impl: AppInfoRepositoryImpl
    ): AppInfoRepository

    @Binds
    @Singleton
    abstract fun bindBoardRepository(
        mock: MockBoardRepository
//        impl: BoardRepositoryImpl
    ): BoardRepository

    @Binds
    @Singleton
    abstract fun bindCommentRepository(
        mock: MockCommentRepository
//        impl: CommentRepositoryImpl
    ): CommentRepository

    @Binds
    @Singleton
    abstract fun bindDeviceRepository(
        mock: MockDeviceRepository
//        impl: DeviceRepositoryImpl
    ): DeviceRepository

    @Binds
    @Singleton
    abstract fun bindHospitalRepository(
        mock: MockHospitalRepository
//        impl: HospitalRepositoryImpl
    ): HospitalRepository

    @Binds
    @Singleton
    abstract fun bindInquiryRepository(
        mock: MockInquiryRepository
//        impl: InquiryRepositoryImpl
    ): InquiryRepository

    @Binds
    @Singleton
    abstract fun bindNoticeRepository(
        mock: MockNoticeRepository
//        impl: NoticeRepositoryImpl
    ): NoticeRepository

    @Binds
    @Singleton
    abstract fun bindPostRepository(
        mock: MockPostRepository
//        impl: PostRepositoryImpl
    ): PostRepository

    @Binds
    @Singleton
    abstract fun bindQnaRepository(
        impl: QnaRepositoryImpl
    ): QnaRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        mock: MockUserRepository
//        impl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindReviewRepository(
        mock: MockReviewRepository
//        impl: ReviewRepositoryImpl
    ): ReviewRepository

    @Binds
    @Singleton
    abstract fun bindHistoryRepository(
        mock: MockHistoryRepository
//        impl: HistoryRepositoryImpl
    ): HistoryRepository

    @Binds
    @Singleton
    abstract fun bindRecentRepository(
        mock: MockRecentRepository
//        impl: RecentRepositoryImpl
    ): RecentRepository
}