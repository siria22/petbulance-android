package com.petbulance.data.di

import com.petbulance.data.repository.feature.community.board.MockBoardRepository
import com.petbulance.data.repository.feature.community.comment.MockCommentRepository
import com.petbulance.data.repository.feature.community.post.MockPostRepository
import com.petbulance.data.repository.feature.community.recent.MockRecentRepository
import com.petbulance.data.repository.feature.home.BannerRepositoryImpl
import com.petbulance.data.repository.feature.home.MockBannerRepository
import com.petbulance.data.repository.feature.hospital.history.MockHistoryRepository
import com.petbulance.data.repository.feature.hospital.history.MockSearchRepository
import com.petbulance.data.repository.feature.hospital.hospital.MockHospitalRepository
import com.petbulance.data.repository.feature.hospital.review.MockReviewRepository
import com.petbulance.data.repository.feature.hospital.search.SearchRepositoryImpl
import com.petbulance.data.repository.feature.support.inquiry.MockInquiryRepository
import com.petbulance.data.repository.feature.support.notice.MockNoticeRepository
import com.petbulance.data.repository.feature.support.qna.MockQnaRepository
import com.petbulance.data.repository.feature.support.qna.QnaRepositoryImpl
import com.petbulance.data.repository.feature.support.report.MockReportRepository
import com.petbulance.data.repository.feature.support.report.ReportRepositoryImpl
import com.petbulance.data.repository.feature.user.auth.AuthRepositoryImpl
import com.petbulance.data.repository.feature.user.auth.MockAuthRepository
import com.petbulance.data.repository.feature.user.terms.MockTermsRepository
import com.petbulance.data.repository.feature.user.user.MockUserRepository
import com.petbulance.data.repository.nonfeature.app.MockAppInfoRepository
import com.petbulance.data.repository.nonfeature.device.MockDeviceRepository
import com.petbulance.data.repository.nonfeature.preference.PreferenceRepositoryImpl
import com.petbulance.domain.repository.feature.community.BoardRepository
import com.petbulance.domain.repository.feature.community.CommentRepository
import com.petbulance.domain.repository.feature.community.PostRepository
import com.petbulance.domain.repository.feature.community.RecentRepository
import com.petbulance.domain.repository.feature.home.BannerRepository
import com.petbulance.domain.repository.feature.hospital.HistoryRepository
import com.petbulance.domain.repository.feature.hospital.HospitalRepository
import com.petbulance.domain.repository.feature.hospital.ReviewRepository
import com.petbulance.domain.repository.feature.hospital.SearchRepository
import com.petbulance.domain.repository.feature.support.InquiryRepository
import com.petbulance.domain.repository.feature.support.NoticeRepository
import com.petbulance.domain.repository.feature.support.QnaRepository
import com.petbulance.domain.repository.feature.support.ReportRepository
import com.petbulance.domain.repository.feature.user.AuthRepository
import com.petbulance.domain.repository.feature.user.TermsRepository
import com.petbulance.domain.repository.feature.user.UserRepository
import com.petbulance.domain.repository.nonfeature.app.AppInfoRepository
import com.petbulance.domain.repository.nonfeature.device.DeviceRepository
import com.petbulance.domain.repository.nonfeature.preference.PreferenceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPreferenceRepository(
        impl: PreferenceRepositoryImpl
    ): PreferenceRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        mock: MockAuthRepository,
//        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindTermsRepository(
        mock: MockTermsRepository
//        impl: TermsRepositoryImpl
    ): TermsRepository

    @Binds
    @Singleton
    abstract fun bindAppInfoRepository(
        mock: MockAppInfoRepository
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
        mock: MockQnaRepository,
//        impl: QnaRepositoryImpl
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

    @Binds
    @Singleton
    abstract fun bindSearchRepository(
        mock: MockSearchRepository,
//        impl: SearchRepositoryImpl
    ): SearchRepository

    @Binds
    @Singleton
    abstract fun bindBannerRepository(
        mock: MockBannerRepository,
//        impl: BannerRepositoryImpl
    ): BannerRepository

    @Binds
    @Singleton
    abstract fun bindReportRepository(
        mock: MockReportRepository,
//        impl: ReportRepositoryImpl
    ): ReportRepository
}