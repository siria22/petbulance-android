package com.petbulance.data.repository

import com.petbulance.domain.model.feature.hospital.hospital.HospitalTag
import com.petbulance.domain.model.feature.hospital.hospital.OpenHour
import com.petbulance.domain.model.feature.hospital.review.ReceiptItem
import com.petbulance.domain.model.type.AnimalCategory
import com.petbulance.domain.model.type.AnimalSpecies
import com.petbulance.domain.model.type.NoticeStatusType
import com.petbulance.domain.model.type.PostCategory
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

/**
 * 서버 종료 이후 Mock Repository들이 함께 쓰는 시연용 데이터.
 * 병원 ↔ 후기, 게시글 ↔ 댓글 ↔ 알림의 id를 이 파일에서 맞춘다.
 *
 * - 병원 좌표는 서울 성북구 보문역(37.5853, 127.0195) 인근이다. 에뮬레이터 GPS를 이 좌표로 맞춘다.
 * - 병원·사용자 이름, 주소 번지, 연락처는 모두 가상 데이터다.
 */
internal object MockFixtures {

    const val MY_NICKNAME = "보리네집사"
    const val MY_EMAIL = "bori.jipsa@example.com"
    const val NETWORK_DELAY_MS = 300L
    const val ROOT_PARENT_ID = 0L

    // region Hospital

    val hospitals = listOf(
        MockHospital(
            id = 1,
            name = "도토리 소동물 클리닉",
            address = "서울 성북구 보문로 34, 2층",
            lat = 37.5861,
            lng = 127.0181,
            phone = "0507-0000-0101",
            species = listOf(AnimalSpecies.HAMSTER, AnimalSpecies.GUINEAPIG, AnimalSpecies.RABBIT),
            tags = listOf(work("야간진료"), animal("소형포유류"), location("보문역 도보 3분"), location("주차가능")),
            openHours = schedule(weekday = "10:00-22:00", saturday = "10:00-18:00", sunday = CLOSED),
            description = "햄스터·기니피그·토끼 등 소형 포유류를 전문으로 진료합니다. 평일에는 밤 10시까지 야간 진료를 운영합니다.",
            notes = "주차 2대 가능, 당일 예약 권장"
        ),
        MockHospital(
            id = 2,
            name = "하늘깃 조류 동물병원",
            address = "서울 성북구 동소문로 88, 3층",
            lat = 37.5898,
            lng = 127.0133,
            phone = "0507-0000-0102",
            species = listOf(AnimalSpecies.PARROT, AnimalSpecies.FINCH_TYPES),
            tags = listOf(work("주말진료"), animal("조류"), location("성신여대입구역")),
            openHours = schedule(weekday = "09:30-19:00", saturday = "09:30-17:00", sunday = "10:00-15:00"),
            description = "앵무새·핀치류 등 반려 조류의 진료와 부리·발톱 관리, 정기 건강검진을 제공합니다.",
            notes = "이동장에 담아 방문해주세요"
        ),
        MockHospital(
            id = 3,
            name = "초록비늘 파충류 동물병원",
            address = "서울 성북구 고려대로 21, 1층",
            lat = 37.5862,
            lng = 127.0290,
            phone = "0507-0000-0103",
            species = listOf(AnimalSpecies.GECKO, AnimalSpecies.TURTLE, AnimalSpecies.SNAKE, AnimalSpecies.FROG),
            tags = listOf(animal("파충류"), animal("양서류"), location("안암역")),
            openHours = schedule(weekday = "10:00-20:00", saturday = "10:00-17:00", sunday = CLOSED),
            description = "도마뱀·거북·뱀과 양서류를 진료합니다. 탈피부전, 대사성 골질환 진료와 사육 환경 상담을 함께 진행합니다.",
            notes = "이동 중 체온 유지를 위해 보온팩 지참을 권장합니다"
        ),
        MockHospital(
            id = 4,
            name = "달빛 24시 특수동물 메디컬센터",
            address = "서울 동대문구 왕산로 55",
            lat = 37.5781,
            lng = 127.0248,
            phone = "0507-0000-0104",
            species = listOf(AnimalSpecies.FERRET, AnimalSpecies.RABBIT, AnimalSpecies.CHINCHILLA, AnimalSpecies.PARROT, AnimalSpecies.GECKO),
            tags = listOf(work("24시간"), work("응급가능"), location("신설동역"), location("주차가능")),
            openHours = schedule(weekday = "00:00-24:00", saturday = "00:00-24:00", sunday = "00:00-24:00"),
            description = "특수동물 응급 진료를 24시간 운영합니다. 입원실과 산소방을 갖추고 있습니다.",
            notes = "야간 진료비 별도"
        ),
        MockHospital(
            id = 5,
            name = "솜털 토끼 클리닉",
            address = "서울 성북구 보문사길 12",
            lat = 37.5818,
            lng = 127.0169,
            phone = "0507-0000-0105",
            species = listOf(AnimalSpecies.RABBIT, AnimalSpecies.CHINCHILLA),
            tags = listOf(animal("소형포유류"), location("보문역 도보 5분")),
            openHours = schedule(weekday = "10:00-19:00", saturday = "10:00-14:00", sunday = CLOSED),
            description = "토끼·친칠라의 치아 질환과 소화기 질환을 중점적으로 진료합니다.",
            notes = "예약제로 운영합니다"
        ),
        MockHospital(
            id = 6,
            name = "물결 아쿠아 동물병원",
            address = "서울 종로구 창신길 40",
            lat = 37.5763,
            lng = 127.0131,
            phone = "0507-0000-0106",
            species = listOf(AnimalSpecies.ORNAMENTAL_FISH, AnimalSpecies.AXOLOTL),
            tags = listOf(animal("어류"), animal("양서류"), location("동묘앞역")),
            openHours = schedule(weekday = "11:00-20:00", saturday = "11:00-18:00", sunday = CLOSED),
            description = "관상어와 우파루파 등 수생 동물의 수질 검사와 질병 진료를 합니다.",
            notes = "사육수 500ml를 가져오시면 수질 검사가 가능합니다"
        ),
        MockHospital(
            id = 7,
            name = "몽글 엑조틱 동물병원",
            address = "서울 성북구 삼선교로 17, 2층",
            lat = 37.5886,
            lng = 127.0077,
            phone = "0507-0000-0107",
            species = listOf(AnimalSpecies.HEDGEHOG, AnimalSpecies.FERRET, AnimalSpecies.SUGAR_GLIDER, AnimalSpecies.PARROT),
            tags = listOf(work("주말진료"), animal("소형포유류"), animal("조류"), location("한성대입구역")),
            openHours = schedule(weekday = "09:00-18:00", saturday = "09:00-18:00", sunday = "09:00-13:00"),
            description = "고슴도치·페럿·슈가글라이더 등 다양한 특수동물의 기본 진료와 예방 관리를 합니다.",
            notes = "주말 진료 가능"
        ),
        MockHospital(
            id = 8,
            name = "포근 특수동물병원",
            address = "서울 동대문구 무학로 102",
            lat = 37.5795,
            lng = 127.0322,
            phone = "0507-0000-0108",
            species = listOf(AnimalSpecies.GECKO, AnimalSpecies.TURTLE, AnimalSpecies.HAMSTER, AnimalSpecies.HEDGEHOG),
            tags = listOf(animal("파충류"), animal("소형포유류"), location("주차가능")),
            openHours = schedule(weekday = "13:00-21:00", saturday = "13:00-18:00", sunday = CLOSED),
            description = "오후 진료 중심으로 운영하며 파충류 건강검진과 소형 포유류 진료를 합니다.",
            notes = "오후 1시부터 진료합니다"
        ),
    )

    fun findHospital(id: Long): MockHospital? = hospitals.find { it.id == id }

    fun reviewsOf(hospitalId: Long): List<MockReview> = reviews.filter { it.hospitalId == hospitalId }

    fun ratingOf(hospitalId: Long): Double {
        val hospitalReviews = reviewsOf(hospitalId)
        if (hospitalReviews.isEmpty()) return 0.0
        return hospitalReviews.map { it.totalRating }.average().roundRating()
    }

    // endregion

    // region Review

    /** id가 클수록 최근 후기다. */
    val reviews = listOf(
        MockReview(
            id = 12, hospitalId = 1, writer = "햄찌아빠", species = AnimalSpecies.HAMSTER,
            items = listOf(ReceiptItem("진찰료", 15_000), ReceiptItem("분변검사", 20_000), ReceiptItem("정장제 처방", 8_000)),
            content = "밤 9시가 넘었는데 햄스터가 밥을 안 먹고 계속 웅크려 있어서 찾아갔어요. 야간인데도 바로 봐주셨고, 분변검사 결과를 화면으로 보여주면서 설명해주셔서 안심됐습니다.",
            facilityRating = 4.5, expertiseRating = 5.0, kindnessRating = 5.0, likeCount = 18, daysAgo = 1
        ),
        MockReview(
            id = 11, hospitalId = 5, writer = MY_NICKNAME, species = AnimalSpecies.RABBIT,
            items = listOf(ReceiptItem("진찰료", 15_000), ReceiptItem("치아 방사선 촬영", 45_000), ReceiptItem("부정교합 교정", 40_000)),
            content = "보리가 앞니가 길어져서 사료를 제대로 못 먹었어요. 방사선으로 어금니 상태까지 확인하고 교정해주셨고, 집에서 건초 급여하는 방법도 알려주셨어요.",
            facilityRating = 5.0, expertiseRating = 5.0, kindnessRating = 4.5, likeCount = 24, daysAgo = 3
        ),
        MockReview(
            id = 10, hospitalId = 2, writer = "코카티엘하루", species = AnimalSpecies.PARROT,
            items = listOf(ReceiptItem("진찰료", 15_000), ReceiptItem("깃털·피부 검사", 30_000), ReceiptItem("영양제 처방", 12_000)),
            content = "코카티엘이 깃털을 자꾸 뽑아서 방문했어요. 스트레스 요인을 하나씩 체크해주시고 케이지 배치까지 조언해주셔서 도움이 많이 됐습니다.",
            facilityRating = 4.0, expertiseRating = 4.5, kindnessRating = 5.0, likeCount = 9, daysAgo = 4
        ),
        MockReview(
            id = 9, hospitalId = 3, writer = "게코는사랑", species = AnimalSpecies.GECKO,
            items = listOf(ReceiptItem("진찰료", 15_000), ReceiptItem("탈피부전 처치", 25_000), ReceiptItem("온욕 처치", 10_000)),
            content = "레오파드 게코 발가락에 허물이 남아서 갔는데 처치를 꼼꼼하게 해주셨어요. 사육장 습도 맞추는 팁도 알려주셔서 이후로는 깔끔하게 탈피하고 있어요.",
            facilityRating = 4.5, expertiseRating = 5.0, kindnessRating = 4.5, likeCount = 15, daysAgo = 6
        ),
        MockReview(
            id = 8, hospitalId = 4, writer = "밤톨이네", species = AnimalSpecies.FERRET,
            items = listOf(ReceiptItem("야간 진찰료", 30_000), ReceiptItem("혈액검사", 60_000), ReceiptItem("수액 처치", 35_000)),
            content = "새벽에 페럿이 구토를 해서 급하게 갔어요. 24시간 운영이라 바로 진료받을 수 있었고, 검사 결과가 나올 때까지 상태를 계속 설명해주셨어요.",
            facilityRating = 4.0, expertiseRating = 4.5, kindnessRating = 4.5, likeCount = 21, daysAgo = 8
        ),
        MockReview(
            id = 7, hospitalId = 3, writer = "느림보거북", species = AnimalSpecies.TURTLE,
            items = listOf(ReceiptItem("진찰료", 15_000), ReceiptItem("방사선 촬영", 40_000), ReceiptItem("칼슘제 처방", 10_000)),
            content = "등갑에 하얀 반점이 생겨서 방문했는데 방사선으로 뼈 상태까지 확인해주셨어요. UVB 조명 교체 주기를 알려주셔서 바로 바꿨습니다.",
            facilityRating = 4.5, expertiseRating = 4.5, kindnessRating = 4.0, likeCount = 7, daysAgo = 10
        ),
        MockReview(
            id = 6, hospitalId = 7, writer = "도치맘", species = AnimalSpecies.HEDGEHOG,
            items = listOf(ReceiptItem("진찰료", 15_000), ReceiptItem("피부 진드기 검사", 20_000), ReceiptItem("외부기생충 약", 15_000)),
            content = "고슴도치 가시가 빠지고 몸을 자꾸 긁어서 갔는데 진드기였어요. 약 바르는 방법을 직접 보여주셔서 집에서도 어렵지 않았어요.",
            facilityRating = 4.0, expertiseRating = 4.0, kindnessRating = 5.0, likeCount = 5, daysAgo = 12
        ),
        MockReview(
            id = 5, hospitalId = 6, writer = "우파루파집사", species = AnimalSpecies.AXOLOTL,
            items = listOf(ReceiptItem("진찰료", 15_000), ReceiptItem("수질 검사", 20_000), ReceiptItem("약욕 처치", 18_000)),
            content = "우파루파 아가미가 짧아져서 사육수를 가지고 방문했어요. 수질 검사로 원인을 바로 찾아주셔서 물 관리 방법을 바꿨습니다.",
            facilityRating = 4.5, expertiseRating = 4.5, kindnessRating = 4.5, likeCount = 6, daysAgo = 14
        ),
        MockReview(
            id = 4, hospitalId = 1, writer = MY_NICKNAME, species = AnimalSpecies.GUINEAPIG,
            items = listOf(ReceiptItem("진찰료", 15_000), ReceiptItem("비타민C 주사", 12_000)),
            content = "기니피그 콩이가 기운이 없어서 데려갔어요. 비타민C가 부족할 수 있다고 하셔서 사료와 채소 구성을 같이 점검해주셨어요.",
            facilityRating = 4.5, expertiseRating = 4.5, kindnessRating = 5.0, likeCount = 11, daysAgo = 20,
            isUnderReview = true
        ),
        MockReview(
            id = 3, hospitalId = 8, writer = "볼파이톤러버", species = AnimalSpecies.SNAKE,
            items = listOf(ReceiptItem("진찰료", 15_000), ReceiptItem("구내염 처치", 30_000)),
            content = "볼파이톤이 먹이를 계속 거부해서 방문했는데 입안 염증을 찾아주셨어요. 오후 진료라 퇴근 후에 가기 편했습니다.",
            facilityRating = 4.0, expertiseRating = 4.5, kindnessRating = 4.0, likeCount = 4, daysAgo = 25
        ),
        MockReview(
            id = 2, hospitalId = 2, writer = "핀치네", species = AnimalSpecies.FINCH_TYPES,
            items = listOf(ReceiptItem("진찰료", 15_000), ReceiptItem("부리·발톱 정리", 15_000)),
            content = "금화조 발톱이 길어서 정리하러 갔어요. 새가 놀라지 않게 짧고 빠르게 해주셔서 좋았어요.",
            facilityRating = 4.5, expertiseRating = 4.0, kindnessRating = 4.5, likeCount = 3, daysAgo = 30,
            isReceiptVerified = false
        ),
        MockReview(
            id = 1, hospitalId = 4, writer = "친칠라솜뭉치", species = AnimalSpecies.CHINCHILLA,
            items = listOf(ReceiptItem("진찰료", 15_000), ReceiptItem("복부 초음파 검사", 55_000)),
            content = "친칠라가 변을 적게 봐서 걱정됐는데 초음파로 장 상태를 확인해주셨어요. 병원이 넓고 대기 공간이 깨끗했어요.",
            facilityRating = 4.5, expertiseRating = 4.0, kindnessRating = 4.0, likeCount = 8, daysAgo = 40
        ),
    )

    fun findReview(id: Long): MockReview? = reviews.find { it.id == id }

    // endregion

    // region Community

    /** id가 클수록 최근 게시글이다. */
    val posts = listOf(
        MockPost(
            id = 12, type = AnimalCategory.SMALLMAMMALS, topic = PostCategory.HEALTH,
            title = "햄스터 한쪽 볼이 갑자기 부었어요",
            content = "어제까지 괜찮았는데 오늘 보니 왼쪽 볼주머니가 부어 있어요. 먹이를 넣어둔 건지 염증인지 구분이 안 되네요. 비슷한 경험 있으신 분 계신가요?",
            writer = "햄찌아빠", minutesAgo = 25, viewCount = 84, likeCount = 6
        ),
        MockPost(
            id = 11, type = AnimalCategory.REPTILE, topic = PostCategory.DAILY,
            title = "볼파이톤 탈피 한 번에 성공했어요",
            content = "습식 은신처를 넣어주고 나서 처음으로 허물을 통째로 벗었어요. 눈 캡까지 깔끔하게 벗겨져서 너무 뿌듯하네요.",
            writer = "볼파이톤러버", minutesAgo = 95, viewCount = 132, likeCount = 27
        ),
        MockPost(
            id = 10, type = AnimalCategory.AVIAN, topic = PostCategory.HEALTH,
            title = "코카티엘이 깃털을 뽑는 이유가 뭘까요",
            content = "요즘 가슴 쪽 깃털을 자꾸 뽑아요. 병원에서는 피부에는 문제가 없다고 했는데, 집에서 확인해볼 만한 스트레스 요인이 있을까요?",
            writer = "코카티엘하루", minutesAgo = 180, viewCount = 210, likeCount = 14
        ),
        MockPost(
            id = 9, type = AnimalCategory.SMALLMAMMALS, topic = PostCategory.DAILY,
            title = "토끼 보리 치아 교정 받고 왔어요",
            content = "앞니가 길어져서 사료를 잘 못 먹길래 근처 토끼 전문 병원에 다녀왔어요. 교정하고 나니 건초를 다시 잘 먹네요. 치아 문제는 빨리 병원에 가는 게 답인 것 같아요.",
            writer = MY_NICKNAME, minutesAgo = 300, viewCount = 356, likeCount = 41, isLiked = true
        ),
        MockPost(
            id = 8, type = AnimalCategory.AMPHIBIAN, topic = PostCategory.SUPPLIES,
            title = "우파루파 수조 냉각팬 추천 부탁드려요",
            content = "여름마다 수온이 24도를 넘어서 걱정이에요. 45cm 수조에 쓰기 좋은 냉각팬 추천해주실 분 있을까요?",
            writer = "우파루파집사", minutesAgo = 600, viewCount = 98, likeCount = 5
        ),
        MockPost(
            id = 7, type = AnimalCategory.SMALLMAMMALS, topic = PostCategory.TRADE,
            title = "기니피그 케이지 나눔합니다 (성북구)",
            content = "콩이 케이지를 넓은 걸로 바꾸면서 쓰던 케이지를 나눔해요. 100cm 크기이고 급수기와 은신처도 같이 드립니다. 직접 가져가실 분만 연락주세요.",
            writer = MY_NICKNAME, minutesAgo = 1_300, viewCount = 145, likeCount = 9
        ),
        MockPost(
            id = 6, type = AnimalCategory.REPTILE, topic = PostCategory.HEALTH,
            title = "거북이 등갑에 하얀 반점이 생겼어요",
            content = "붉은귀거북 등갑에 하얀 반점이 몇 개 생겼어요. 일광욕은 매일 시키고 있는데 곰팡이일까요, 칼슘 부족일까요?",
            writer = "느림보거북", minutesAgo = 2_900, viewCount = 188, likeCount = 12
        ),
        MockPost(
            id = 5, type = AnimalCategory.FISH, topic = PostCategory.HEALTH,
            title = "구피 꼬리가 녹는 증상 겪어보신 분",
            content = "수컷 구피 꼬리 끝이 하얗게 변하면서 조금씩 짧아져요. 물갈이는 일주일에 한 번 하고 있습니다.",
            writer = "물멍러", minutesAgo = 4_400, viewCount = 76, likeCount = 3
        ),
        MockPost(
            id = 4, type = AnimalCategory.SMALLMAMMALS, topic = PostCategory.SUPPLIES,
            title = "고슴도치 사료 바꿀 때 섞는 비율 궁금해요",
            content = "기존 사료에서 새 사료로 바꾸려고 하는데, 며칠에 걸쳐 어떤 비율로 섞어주시나요?",
            writer = "도치맘", minutesAgo = 5_800, viewCount = 120, likeCount = 8
        ),
        MockPost(
            id = 3, type = AnimalCategory.AVIAN, topic = PostCategory.DAILY,
            title = "금화조 부부가 둥지를 만들기 시작했어요",
            content = "코코넛 둥지를 넣어줬더니 둘이 번갈아 가며 지푸라기를 물어 나르고 있어요. 하루 종일 봐도 안 질리네요.",
            writer = "핀치네", minutesAgo = 7_200, viewCount = 64, likeCount = 11
        ),
        MockPost(
            id = 2, type = AnimalCategory.SMALLMAMMALS, topic = PostCategory.HEALTH,
            title = "페럿 새벽 응급실 다녀온 후기",
            content = "새벽 3시에 페럿이 계속 구토를 해서 24시간 특수동물 병원에 다녀왔어요. 이물질 섭취가 의심돼서 검사를 받았고 다행히 수액 처치 후 회복했습니다. 근처 24시간 병원은 미리 알아두세요.",
            writer = "밤톨이네", minutesAgo = 8_700, viewCount = 402, likeCount = 53
        ),
        MockPost(
            id = 1, type = AnimalCategory.REPTILE, topic = PostCategory.SUPPLIES,
            title = "레오파드 게코 은신처 뭐 쓰세요?",
            content = "지금은 반쪽 코르크를 쓰고 있는데 습식 은신처를 하나 더 두려고 해요. 써보신 제품 중에 괜찮았던 거 있으면 알려주세요.",
            writer = "게코는사랑", minutesAgo = 11_500, viewCount = 230, likeCount = 19
        ),
    )

    val comments = listOf(
        MockComment(101, postId = 12, writer = "몽실햄찌", minutesAgo = 15, content = "볼주머니에 먹이를 넣은 거면 몇 시간 안에 줄어들어요. 하루 넘게 부어 있으면 농양일 수 있어서 병원에 가보시는 게 좋아요."),
        MockComment(102, postId = 12, writer = "햄찌아빠", minutesAgo = 10, parentId = 101, mentionNickname = "몽실햄찌", content = "감사해요! 저녁까지 지켜보고 안 줄어들면 병원 가볼게요."),
        MockComment(81, postId = 10, writer = MY_NICKNAME, minutesAgo = 150, content = "케이지 위치를 바꾸고 나서 좋아졌다는 얘기를 들었어요. 창가나 현관 근처라면 옮겨보세요."),
        MockComment(83, postId = 10, writer = "코카티엘하루", minutesAgo = 90, parentId = 81, mentionNickname = MY_NICKNAME, content = "조언 감사해요! 오늘 바로 창가에서 옮겼어요."),
        MockComment(82, postId = 10, writer = "하늘이아빠", minutesAgo = 100, content = "놀이 시간이 부족해도 그런다고 하더라고요. 장난감을 주기적으로 바꿔주는 것도 추천해요."),
        MockComment(91, postId = 9, writer = "토순이네", minutesAgo = 240, content = "저희 토끼도 부정교합이라 두 달마다 병원에 가요. 건초를 많이 먹이는 게 제일 중요하더라고요."),
        MockComment(92, postId = 9, writer = MY_NICKNAME, minutesAgo = 200, parentId = 91, mentionNickname = "토순이네", content = "맞아요, 건초 종류도 바꿔봤어요. 조언 감사합니다!"),
        MockComment(93, postId = 9, writer = "도치맘", minutesAgo = 120, content = "보리 너무 귀엽네요. 얼른 회복하길 바라요!"),
        MockComment(71, postId = 11, writer = "게코는사랑", minutesAgo = 80, content = "허물을 통째로 벗은 거 보면 뿌듯하죠. 습도 관리 비결 공유 부탁드려요!"),
        MockComment(61, postId = 7, writer = "콩콩이맘", minutesAgo = 1_200, content = "혹시 아직 나눔 가능할까요? 저도 성북구 살아요!"),
        MockComment(62, postId = 7, writer = MY_NICKNAME, minutesAgo = 1_150, parentId = 61, mentionNickname = "콩콩이맘", content = "네 가능해요. 쪽지 드릴게요!"),
        MockComment(41, postId = 6, writer = "거북이할매", minutesAgo = 2_800, content = "UVB 조명 교체 주기부터 확인해보세요. 6개월이 넘으면 효과가 떨어진대요."),
        MockComment(51, postId = 2, writer = "페럿사랑", minutesAgo = 8_000, content = "새벽에 진료 가능한 곳을 알아두는 게 정말 중요하네요. 정보 감사합니다."),
        MockComment(52, postId = 2, writer = MY_NICKNAME, minutesAgo = 7_000, content = "저장해둘게요. 밤톨이 회복 잘하길 바라요!"),
        MockComment(31, postId = 1, writer = "레게사육3년차", minutesAgo = 11_000, content = "코르크 은신처에 습식 은신처를 하나 더 두면 탈피할 때 확실히 편해요."),
    )

    fun findPost(id: Long): MockPost? = posts.find { it.id == id }

    /** 부모 댓글 바로 뒤에 답글이 오도록 정렬한다. */
    fun commentsOf(postId: Long): List<MockComment> {
        val postComments = comments.filter { it.postId == postId }
        return postComments.filter { it.isRoot }
            .sortedBy { it.id }
            .flatMap { root -> listOf(root) + postComments.filter { it.parentId == root.id }.sortedBy { it.id } }
    }

    // endregion

    // region Notice

    /** id가 클수록 최근 공지다. */
    val notices = listOf(
        MockNotice(
            id = 3, status = NoticeStatusType.NOTICE, daysAgo = 2,
            title = "커뮤니티 운영 정책 안내",
            content = "안녕하세요, 펫뷸런스입니다.\n\n보호자님들이 안심하고 정보를 나눌 수 있도록 커뮤니티 운영 정책을 안내드립니다. 특정 병원을 근거 없이 비방하거나 광고성 글을 반복해서 올리는 경우 게시글이 숨김 처리될 수 있습니다.\n\n감사합니다."
        ),
        MockNotice(
            id = 2, status = NoticeStatusType.EVENT, daysAgo = 10,
            title = "영수증 인증 후기 작성 이벤트",
            content = "병원 방문 영수증을 인증하고 후기를 남겨주세요. 이벤트 기간 동안 영수증 인증 후기를 작성한 보호자님께 추첨을 통해 반려동물 용품을 드립니다."
        ),
        MockNotice(
            id = 1, status = NoticeStatusType.NOTICE, daysAgo = 20,
            title = "특수동물 병원 정보 업데이트 안내",
            content = "서울 지역 특수동물 진료 병원의 진료 시간과 진료 가능 동물 정보가 업데이트되었습니다. 잘못된 정보는 고객센터로 알려주세요."
        ),
    )

    fun findNotice(id: Long): MockNotice? = notices.find { it.id == id }

    // endregion

    // region Time

    /** 서버 `TimeUtil.formatCreatedAt`과 같은 규칙: 방금 전 / N분 전 / N시간 전 / yyyy.MM.dd */
    fun createdAtLabel(minutesAgo: Long): String = when {
        minutesAgo < 1 -> "방금 전"
        minutesAgo < MINUTES_PER_HOUR -> "${minutesAgo}분 전"
        minutesAgo < MINUTES_PER_DAY -> "${minutesAgo / MINUTES_PER_HOUR}시간 전"
        else -> LocalDateTime.now().minusMinutes(minutesAgo).format(DATE_FORMATTER)
    }

    fun dateLabel(daysAgo: Long): String = LocalDateTime.now().minusDays(daysAgo).format(DATE_FORMATTER)

    /** 알림 목록 표기: N분 전 / N시간 전 / N일 전 */
    fun notificationTimeLabel(minutesAgo: Long): String = when {
        minutesAgo < MINUTES_PER_HOUR -> "${minutesAgo}분 전"
        minutesAgo < MINUTES_PER_DAY -> "${minutesAgo / MINUTES_PER_HOUR}시간 전"
        else -> "${minutesAgo / MINUTES_PER_DAY}일 전"
    }

    // endregion
}

internal data class MockHospital(
    val id: Long,
    val name: String,
    val address: String,
    val lat: Double,
    val lng: Double,
    val phone: String,
    val species: List<AnimalSpecies>,
    val tags: List<HospitalTag>,
    val openHours: List<OpenHour>,
    val description: String,
    val notes: String,
) {
    val animals: List<AnimalCategory> get() = species.map { it.category }.distinct()

    private fun todayRange(now: LocalDateTime): Pair<LocalTime, LocalTime>? {
        val dayKey = DAY_KEYS[now.dayOfWeek.value - 1]
        return openHours.find { it.day == dayKey }?.hours?.toTimeRange()
    }

    fun isOpenAt(now: LocalDateTime): Boolean {
        val (start, end) = todayRange(now) ?: return false
        val time = now.toLocalTime()
        return !time.isBefore(start) && time.isBefore(end)
    }

    fun openHoursLabel(now: LocalDateTime): String {
        val (start, end) = todayRange(now) ?: return "오늘 휴무"
        return when {
            start == LocalTime.MIDNIGHT && end == LocalTime.MAX -> "24시간 진료"
            isOpenAt(now) -> "${end.toLabel()}에 영업 종료"
            now.toLocalTime().isBefore(start) -> "${start.toLabel()}에 영업 시작"
            else -> "오늘 영업 종료"
        }
    }
}

internal data class MockReview(
    val id: Long,
    val hospitalId: Long,
    val writer: String,
    val species: AnimalSpecies,
    val items: List<ReceiptItem>,
    val content: String,
    val facilityRating: Double,
    val expertiseRating: Double,
    val kindnessRating: Double,
    val likeCount: Int,
    val daysAgo: Long,
    val isReceiptVerified: Boolean = true,
    val isUnderReview: Boolean = false,
) {
    val totalRating: Double
        get() = listOf(facilityRating, expertiseRating, kindnessRating).average().roundRating()
    val totalPrice: Int get() = items.sumOf { it.price }
    val treatmentService: String get() = items.joinToString(", ") { "${it.name}(${it.price})" }
    val isMine: Boolean get() = writer == MockFixtures.MY_NICKNAME

    fun createdAt(): String = LocalDateTime.now().minusDays(daysAgo).withNano(0).toString()
    fun createdDate(): String = LocalDateTime.now().minusDays(daysAgo).toLocalDate().toString()
    fun visitDate(): String = LocalDateTime.now().minusDays(daysAgo + 1).toLocalDate().toString()
}

internal data class MockPost(
    val id: Long,
    val type: AnimalCategory,
    val topic: PostCategory,
    val title: String,
    val content: String,
    val writer: String,
    val minutesAgo: Long,
    val viewCount: Int,
    val likeCount: Int,
    val isLiked: Boolean = false,
) {
    val isMine: Boolean get() = writer == MockFixtures.MY_NICKNAME
    val commentCount: Int get() = MockFixtures.commentsOf(id).size
    /** 서버 `AnimalType`·`Topic`의 description과 같은 표기. 목록·상세 화면은 이 문자열을 그대로 보여준다. */
    val typeLabel: String get() = type.korean.replace(" ", "")
    val topicLabel: String get() = topic.korean
    val boardLabel: String get() = "$typeLabel · $topicLabel"
}

internal data class MockComment(
    val id: Long,
    val postId: Long,
    val writer: String,
    val content: String,
    val minutesAgo: Long,
    val parentId: Long = MockFixtures.ROOT_PARENT_ID,
    val mentionNickname: String? = null,
) {
    val isRoot: Boolean get() = parentId == MockFixtures.ROOT_PARENT_ID
    val isMine: Boolean get() = writer == MockFixtures.MY_NICKNAME
}

internal data class MockNotice(
    val id: Long,
    val status: NoticeStatusType,
    val title: String,
    val content: String,
    val daysAgo: Long,
)

private const val CLOSED = "CLOSED"
private const val END_OF_DAY = "24:00"
private const val WEEKDAY_COUNT = 5
private const val MINUTES_PER_HOUR = 60L
private const val MINUTES_PER_DAY = 1_440L
private const val RATING_SCALE = 100.0
private val DAY_KEYS = listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")
private val TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm")
private val DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd")

private fun schedule(weekday: String, saturday: String, sunday: String): List<OpenHour> =
    DAY_KEYS.take(WEEKDAY_COUNT).map { OpenHour(it, weekday) } +
            OpenHour(DAY_KEYS[WEEKDAY_COUNT], saturday) +
            OpenHour(DAY_KEYS[WEEKDAY_COUNT + 1], sunday)

private fun work(value: String) = HospitalTag(type = "WORKTYPE", value = value)
private fun animal(value: String) = HospitalTag(type = "ANIMALTYPE", value = value)
private fun location(value: String) = HospitalTag(type = "LOCATIONTYPE", value = value)

private fun String.toTimeRange(): Pair<LocalTime, LocalTime>? {
    if (this == CLOSED) return null
    val parts = split("-")
    if (parts.size != 2) return null
    val end = if (parts[1] == END_OF_DAY) LocalTime.MAX else LocalTime.parse(parts[1])
    return LocalTime.parse(parts[0]) to end
}

private fun LocalTime.toLabel(): String = if (this == LocalTime.MAX) END_OF_DAY else format(TIME_FORMATTER)

private fun Double.roundRating(): Double = (this * RATING_SCALE).roundToInt() / RATING_SCALE
