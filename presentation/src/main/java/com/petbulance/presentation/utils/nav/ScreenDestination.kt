package com.petbulance.presentation.utils.nav

import com.petbulance.domain.model.type.AnimalCategory

sealed class ScreenDestinations(val route: String) {

    data object Splash : ScreenDestinations("splash")

    data object Login : ScreenDestinations("login")
    data object Welcome : ScreenDestinations("welcome")

    data object Home : ScreenDestinations("home?checkTerms={checkTerms}") {
        const val ARG_CHECK_TERMS = "checkTerms"

        fun createRoute(checkTerms: Boolean = false): String {
            return "home?$ARG_CHECK_TERMS=$checkTerms"
        }
    }

    data object Search : ScreenDestinations("search?animal={animal}&initialHospitalId={initialHospitalId}") {
        const val ARG_ANIMAL = "animal"
        const val ARG_INITIAL_HOSPITAL_ID = "initialHospitalId"

        fun createRoute(animal: AnimalCategory = AnimalCategory.ALL, initialHospitalId: Long? = null): String {
            val base = if (animal == AnimalCategory.ALL) "search" else "search?animal=${animal.name}"
            return if (initialHospitalId != null) {
                if (base.contains("?")) "$base&initialHospitalId=$initialHospitalId"
                else "$base?initialHospitalId=$initialHospitalId"
            } else base
        }

        data object HospitalInfo : ScreenDestinations("search/hospital/{id}") {
            const val ARG_ID: String = "id"
            fun createRoute(id: Long): String = "search/hospital/$id"
        }
    }

    data object Review : ScreenDestinations("review") {
        data object Search : ScreenDestinations("review/search")
        data object Create : ScreenDestinations("review/create?data={data}") {
            const val ARG_DATA = "data"

            fun createRoute(data: String? = null): String {
                return if (data == null) "review/create"
                else "review/create?data=$data"
            }
        }

        data object ReceiptCamera : ScreenDestinations("review/receipt_camera")

        data object Edit : ScreenDestinations("review/edit/{id}") {
            const val ARG_ID = "id"
            fun createRoute(id: Long): String = "review/edit/$id"
        }

        data object Detail : ScreenDestinations("review/detail/{id}") {
            const val ARG_ID = "id"
            fun createRoute(id: Long): String = "review/detail/$id"
        }
    }

    data object Community : ScreenDestinations("community") {
        data object PostDetail : ScreenDestinations("community/post/{id}") {
            const val ARG_ID = "id"
            fun createRoute(id: Long) = "community/post/$id"
        }

        data object WritePost : ScreenDestinations("community/write?postId={postId}") {
            const val ARG_POST_ID = "postId"
            const val NO_POST_ID = -1L
            fun createRoute(postId: Long? = null): String =
                if (postId != null) "community/write?postId=$postId" else "community/write"
        }
    }

    data object MyPage : ScreenDestinations("mypage") {

        sealed class Activity : ScreenDestinations("mypage/activity") {
            data object Reviews : ScreenDestinations("mypage/activity/reviews")
            data object Posts : ScreenDestinations("mypage/activity/posts")
        }

        sealed class User : ScreenDestinations("mypage/user") {
            data object Profile : ScreenDestinations("mypage/user/profile")
            data object Account: ScreenDestinations("mypage/user/account")
        }

        sealed class Help : ScreenDestinations("mypage/help") {
            data object Notice : ScreenDestinations("mypage/help/notice") {
                data object Detail : ScreenDestinations("mypage/help/notice/detail/{id}") {
                    const val ARG_ID = "id"
                    fun createRoute(id: Long): String = "mypage/help/notice/detail/$id"
                }
            }

            data object CS : ScreenDestinations("mypage/help/cs") {
                data object Qna : ScreenDestinations("mypage/help/cs/qna") {
                    data object List : ScreenDestinations("mypage/help/cs/qna/list")
                    data object Create : ScreenDestinations("mypage/help/cs/qna/create?qnaId={qnaId}") {
                        const val ARG_QNA_ID = "qnaId"
                        fun createRoute(qnaId: Long? = null): String {
                            return if (qnaId != null) {
                                "mypage/help/cs/qna/create?qnaId=$qnaId"
                            } else {
                                "mypage/help/cs/qna/create"
                            }
                        }
                    }
                    data object Detail : ScreenDestinations("mypage/help/cs/qna/detail/{id}") {
                        const val ARG_ID = "id"
                        fun createRoute(id: Long): String = "mypage/help/cs/qna/detail/$id"
                    }
                }

                data object Coalition : ScreenDestinations("mypage/help/cs/coalition")
            }

            data object Terms : ScreenDestinations("mypage/help/terms") {
                data object Detail : ScreenDestinations("mypage/help/terms/detail/{termsType}") {
                    const val ARG_TERMS_TYPE = "termsType"
                    fun createRoute(termsType: String): String = "mypage/help/terms/detail/$termsType"
                }
            }
        }
    }
}