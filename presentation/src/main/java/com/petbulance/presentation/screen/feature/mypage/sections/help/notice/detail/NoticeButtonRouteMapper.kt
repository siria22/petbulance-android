package com.petbulance.presentation.screen.feature.mypage.sections.help.notice.detail

import com.petbulance.presentation.utils.nav.ScreenDestinations

/**
 * CTA 공지사항 버튼의 INNER 링크를 앱 내 네비게이션 route로 매핑
 * key : 서버에서 내려오는 link
 * value : 앱 내부 navigation의 route
 */
object NoticeButtonRouteMapper {

    private val routeMap: Map<String, String> = mapOf(
        "home" to ScreenDestinations.Home.route,
        "search" to ScreenDestinations.Search.route,
        "review" to ScreenDestinations.Review.route,
        "mypage" to ScreenDestinations.MyPage.route,
        "mypage/notice" to ScreenDestinations.MyPage.Help.Notice.route,
        "mypage/reviews" to ScreenDestinations.MyPage.Activity.Reviews.route,
    )

    /**
     * INNER 링크 문자열을 앱 내 route로 변환합니다.
     * 매핑되지 않은 링크는 null을 반환합니다.
     */
    fun resolve(link: String): String? = routeMap[link]
}
