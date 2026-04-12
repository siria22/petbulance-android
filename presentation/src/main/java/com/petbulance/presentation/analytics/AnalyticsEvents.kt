package com.petbulance.presentation.analytics

/**
 * GA4 커스텀 이벤트 정의 — PM 문서 기준
 * @see .claude/GA4 핵심 이벤트 정의
 */
object AnalyticsEvents {

    // P0 - 최우선
    const val SELECT_PET_CATEGORY_HOME = "select_pet_category_home"
    const val SEARCH_HOSPITAL_START = "search_hospital_start"
    const val VIEW_SEARCH_RESULTS = "view_search_results"
    const val VIEW_HOSPITAL_DETAIL = "view_hospital_detail"
    const val CLICK_CALL_HOSPITAL = "click_call_hospital"

    // P1 - 중요
    const val VIEW_TAB = "view_tab"
    const val SUBMIT_REVIEW = "submit_review"
    const val SIGN_UP_COMPLETE = "sign_up_complete"
    const val APPLY_SEARCH_FILTER = "apply_search_filter"

    // P2 - 부가
    const val SEARCH_MAP_CURRENT_LOCATION = "search_map_current_location"
    const val SWITCH_TO_LIST_VIEW = "switch_to_list_view"

    object Params {
        // 공통
        const val PET_TYPE = "pet_type"
        const val HOSPITAL_ID = "hospital_id"
        const val HOSPITAL_NAME = "hospital_name"
        const val FROM_SCREEN = "from_screen"

        // 검색
        const val SEARCH_METHOD = "search_method"
        const val REGION = "region"
        const val FILTER_OPERATING = "filter_operating"
        const val RESULT_COUNT = "result_count"
        const val HAS_FILTER = "has_filter"
        const val FILTER_TYPE = "filter_type"
        const val FILTER_VALUE = "filter_value"

        // 병원 상세
        const val HAS_REVIEW = "has_review"
        const val IS_OPERATING_NOW = "is_operating_now"
        const val CALL_TYPE = "call_type"

        // 탭
        const val TAB_NAME = "tab_name"
        const val PREVIOUS_TAB = "previous_tab"

        // 후기
        const val RATING = "rating"
        const val HAS_PHOTO = "has_photo"
        const val HAS_RECEIPT = "has_receipt"
        const val REVIEW_LENGTH = "review_length"

        // 가입
        const val SIGNUP_METHOD = "signup_method"

        // 지도
        const val IS_FIRST_SEARCH = "is_first_search"
        const val FROM_VIEW = "from_view"
    }
}
