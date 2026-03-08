package com.petbulance.presentation.screen.feature.mypage.sections.help.cs.qna.create

data class QnaCreateData(
    val title: String,
    val content: String,
    val isSubmitEnabled: Boolean,
    val mode: QnaCreateMode
) {
    companion object {
        val empty = QnaCreateData(
            title = "",
            content = "",
            isSubmitEnabled = false,
            mode = QnaCreateMode.CREATE
        )

        fun stub() = QnaCreateData(
            title = "닉네임 변경 어떻게 하나요?",
            content = "안녕하세요!\n닉네임 좀 바꾸고 싶은데 어디서 하는지 잘 찾았어요😅 처음 가입할 때 자동으로 만들어진 닉네임인데 너무 재 스타일이 아니라서요.\n마이페이지도 들어가보고 이것저것 찾아봤는데 수정하는 메뉴가 안 보이네요...\n닉네임 변경 가능한가요? 가능하면 어디서 바꾸는지 알려주세요",
            isSubmitEnabled = true,
            mode = QnaCreateMode.CREATE
        )

        fun empty() = QnaCreateData(
            title = "",
            content = "",
            isSubmitEnabled = true,
            mode = QnaCreateMode.CREATE
        )
    }
}