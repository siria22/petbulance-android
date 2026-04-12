package com.petbulance.presentation.analytics

object ScreenNames {

    fun fromRoute(route: String?): String? = when {
        route == null -> null
        // Auth
        route.startsWith("splash") -> "Splash"
        route.startsWith("login") -> "Login"
        route.startsWith("welcome") -> "Welcome"
        // Home
        route.startsWith("home") -> "Home"
        // Search (specific before generic)
        route.startsWith("search/hospital/") -> "HospitalInfo"
        route.startsWith("search") -> "Search"
        // Review (specific before generic)
        route.startsWith("review/search") -> "ReviewSearch"
        route.startsWith("review/create") -> "ReviewCreate"
        route.startsWith("review/receipt_camera") -> "ReceiptCamera"
        route.startsWith("review/edit/") -> "ReviewEdit"
        route.startsWith("review/detail/") -> "ReviewDetail"
        route.startsWith("review") -> "Review"
        // Community (specific before generic)
        route.startsWith("community/post/") -> "PostDetail"
        route.startsWith("community/write") -> "WritePost"
        route.startsWith("community") -> "Community"
        // MyPage - Activity
        route.startsWith("mypage/activity/reviews") -> "MyPageReviews"
        route.startsWith("mypage/activity/posts") -> "MyPagePosts"
        route.startsWith("mypage/activity/comments") -> "MyPageComments"
        // MyPage - User
        route.startsWith("mypage/user/profile") -> "MyPageProfile"
        route.startsWith("mypage/user/account") -> "MyPageAccount"
        // MyPage - Help (specific before generic)
        route.startsWith("mypage/help/notice/detail/") -> "NoticeDetail"
        route.startsWith("mypage/help/notice") -> "NoticeList"
        route.startsWith("mypage/help/cs/qna/create") -> "QnaCreate"
        route.startsWith("mypage/help/cs/qna/detail/") -> "QnaDetail"
        route.startsWith("mypage/help/cs/qna/list") -> "QnaList"
        route.startsWith("mypage/help/cs/coalition") -> "Coalition"
        route.startsWith("mypage/help/cs") -> "CS"
        route.startsWith("mypage/help/terms/detail/") -> "TermsDetail"
        route.startsWith("mypage/help/terms") -> "TermsList"
        route.startsWith("mypage") -> "MyPage"
        else -> route
    }

    // 하단 탭에 해당하는 화면 이름
    private val TAB_SCREENS = setOf("Home", "Search", "Review", "Community", "MyPage")

    fun isTabScreen(screenName: String): Boolean = screenName in TAB_SCREENS

    fun toTabName(screenName: String): String? = when (screenName) {
        "Home" -> "홈"
        "Search" -> "병원검색"
        "Review" -> "병원후기"
        "Community" -> "커뮤니티"
        "MyPage" -> "My"
        else -> null
    }
}
