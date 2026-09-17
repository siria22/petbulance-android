package com.petbulance.data.repository.feature.user.terms

import com.petbulance.domain.model.feature.user.terms.Term
import com.petbulance.domain.model.feature.user.terms.TermsStatus
import com.petbulance.domain.model.type.TermsType
import com.petbulance.domain.repository.feature.user.TermsRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class MockTermsRepository @Inject constructor() : TermsRepository {

    override suspend fun getTermsStatus(): Result<TermsStatus> {
        delay(300) // 네트워크 지연 시뮬레이션
        return Result.success(
            TermsStatus(
                service = true,
                privacy = true,
                location = true,
                marketing = false
            )
        )
    }

    override suspend fun getTermsList(): Result<List<Term>> {
        delay(500)
        return Result.success(
            listOf(
                Term(
                    id = 1,
                    title = "서비스 이용약관",
                    required = true,
                    summary = "서비스 이용을 위한 필수 약관입니다.",
                    content = """
                        <h3>서비스 이용약관</h3>
                        <p>제1조 (목적)<br>본 약관은 펫뷸런스 서비스의 이용 조건 및 절차에 관한 사항을 규정함을 목적으로 합니다.</p>
                        <br>
                        <p>제2조 (용어의 정의)<br>1. "서비스"라 함은 회사가 제공하는 모든 모바일 서비스를 의미합니다.</p>
                        <p>2. "이용자"라 함은 본 약관에 따라 회사가 제공하는 서비스를 받는 회원 및 비회원을 말합니다.</p>
                    """.trimIndent(),
                    version = "1.0",
                    termsType = TermsType.SERVICE
                ),
                Term(
                    id = 2,
                    title = "개인정보 처리방침",
                    required = true,
                    summary = "개인정보 처리를 위한 필수 약관입니다.",
                    content = """
                        <h3>개인정보 처리방침</h3>
                        <p>회사는 이용자의 개인정보를 중요시하며, "정보통신망 이용촉진 및 정보보호"에 관한 법률을 준수하고 있습니다.</p>
                        <br>
                        <p><strong>1. 수집하는 개인정보의 항목</strong></p>
                        <p>- 필수항목: 이름, 전화번호, 이메일, 반려동물 정보</p>
                        <p>- 선택항목: 주소, 생년월일</p>
                    """.trimIndent(),
                    version = "1.0",
                    termsType = TermsType.PRIVACY
                ),
                Term(
                    id = 3,
                    title = "위치기반 서비스 이용약관",
                    required = true,
                    summary = "위치 서비스 이용을 위한 필수 약관입니다.",
                    content = """
                        <h3>위치기반 서비스 이용약관</h3>
                        <p>본 약관은 회사가 제공하는 위치기반 서비스와 관련하여 회사와 개인위치정보주체와의 권리, 의무 및 책임사항을 규정합니다.</p>
                        <p>회사는 사용자의 현재 위치를 기반으로 가장 가까운 병원 정보를 제공하기 위해 위치 정보를 사용합니다.</p>
                    """.trimIndent(),
                    version = "1.0",
                    termsType = TermsType.LOCATION
                ),
                Term(
                    id = 4,
                    title = "마케팅 정보 수신 동의",
                    required = false,
                    summary = "이벤트 및 혜택 정보를 받으시겠습니까?",
                    content = """
                        <h3>마케팅 정보 수신 동의</h3>
                        <p>다양한 이벤트 및 혜택 정보를 앱 푸시 알림, 문자 메시지 등으로 받아보실 수 있습니다.</p>
                        <p>동의하지 않으셔도 기본 서비스 이용에는 제한이 없습니다.</p>
                        <ul>
                            <li>신규 서비스 안내</li>
                            <li>이벤트 및 프로모션 정보 제공</li>
                        </ul>
                    """.trimIndent(),
                    version = "1.0",
                    termsType = TermsType.MARKETING
                )
            )
        )
    }

    override suspend fun getTermDetail(type: String): Result<Term> {
        val term = getTermsList().getOrNull()?.find { it.termsType?.name.equals(type, ignoreCase = true) }
            ?: return Result.failure(NoSuchElementException("Terms not found: $type"))
        return Result.success(term)
    }

    override suspend fun saveTermsConsent(termsTypeList: List<Long>): Result<Unit> {
        delay(800)
        return Result.success(Unit)
    }

    override suspend fun saveTermsConsentLocal(terms: List<Term>): Result<Unit> {
        delay(100)
        return Result.success(Unit)
    }

    override suspend fun withdrawTermsConsent(type: String): Result<Unit> {
        delay(300)
        return Result.success(Unit)
    }
}