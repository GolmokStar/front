package com.example.golmokstar.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.compose.ui.zIndex
import com.example.golmokstar.R
import com.example.golmokstar.ui.theme.*

data class Event(
    var title: String, var startDate: String, var endDate: String
)


data class Place(
    val name: String, val address: String, val imageUrl: String
)


data class History(
    val name: String,
    val address: String,
    val imageUrl: String,
    val rating: Double,
    val title: String,
    val content: String,
    val date: String,
)

data class Friend(
    val name: String
)

val sampleFriends = listOf(
    Friend(name = "하늘빛"),
    Friend(name = "별의길"),
    Friend(name = "검은늑대"),
    Friend(name = "빠른발걸음"),
    Friend(name = "달의여행"),
    Friend(name = "화염의검"),
    Friend(name = "눈의여왕")
)

val samplePlaces = listOf(
    Place(
        name = "남산타워",
        address = "서울특별시 용산구",
        imageUrl = "https://source.unsplash.com/400x300/?seoul,tower"
    ), Place(
        name = "해운대 해수욕장",
        address = "부산광역시 해운대구",
        imageUrl = "https://source.unsplash.com/400x300/?beach,ocean"
    ), Place(
        name = "성산일출봉",
        address = "제주도 서귀포시",
        imageUrl = "https://source.unsplash.com/400x300/?jeju,mountain"
    ), Place(
        name = "안목해변",
        address = "강원도 강릉시",
        imageUrl = "https://source.unsplash.com/400x300/?cafe,beach"
    ), Place(
        name = "한옥마을",
        address = "전라북도 전주시 완산구",
        imageUrl = "https://source.unsplash.com/400x300/?hanok,village"
    )
)

val sampleHistories = listOf(
    History(
        name = "올림픽공원",
        address = "서울특별시 송파구",
        imageUrl = "https://source.unsplash.com/400x300/?seoul,park",
        rating = 4.7,
        title = "올림픽공원 산책",
        content = "서울 올림픽공원에서 자전거를 타며 여유롭게 산책을 즐겼다. 공원 내의 평화로운 분위기가 인상적이었다.",
        date = "2025.03.10"
    ), History(
        name = "불국사",
        address = "경상북도 경주시",
        imageUrl = "https://source.unsplash.com/400x300/?temple,kyongju",
        rating = 4.9,
        title = "경주 불국사 탐방",
        content = "불국사의 아름다운 건축과 고요한 분위기에 감동했다. 역사적인 의미를 되새기며 조용히 산책했다.",
        date = "2025.04.05"
    ), History(
        name = "평창",
        address = "강원도 평창군",
        imageUrl = "https://source.unsplash.com/400x300/?mountain,pyeongchang",
        rating = 4.8,
        title = "평창 스키 여행",
        content = "평창에서 스키를 타며 겨울을 만끽했다. 눈 덮인 산의 아름다움과 스키장의 즐거움이 인상 깊었다.",
        date = "2025.02.25"
    ), History(
        name = "북촌 한옥마을",
        address = "서울특별시 종로구",
        imageUrl = "https://source.unsplash.com/400x300/?hanok,seoul",
        rating = 4.6,
        title = "한옥마을 탐방",
        content = "북촌 한옥마을에서 전통적인 분위기를 느꼈다. 한옥의 아름다움과 조용한 골목이 마음에 들었다.",
        date = "2025.05.12"
    ), History(
        name = "덕진공원",
        address = "전라북도 전주시 덕진구",
        imageUrl = "https://source.unsplash.com/400x300/?park,jeonju",
        rating = 4.4,
        title = "전주 덕진공원 산책",
        content = "덕진공원에서 피크닉을 즐기며 봄날의 따뜻한 햇살을 만끽했다. 공원의 아름다움에 감탄했다.",
        date = "2025.04.20"
    )
)


@Preview
@Composable
fun HomeScreen() {

    // FocusManager 객체 생성
    val focusManager = LocalFocusManager.current

    var openTripSetBox by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    val count = 1

    Column(
        verticalArrangement = Arrangement.spacedBy(35.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(color = White)
            .verticalScroll(scrollState)
            .padding(top = 20.dp, bottom = 50.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    // 화면의 빈 곳을 클릭하면 포커스를 해제
                    focusManager.clearFocus()
                    openTripSetBox = false
                })
            },
    ) {
        Section(firstComponent = {
            Text(
                text = "여행 등록하기", style = AppTypography.titleMedium
            )
        }, secondComponent = {
            CheckIconTextField(focusManager,
                openTripSetBox,
                onFocusChange = { openTripSetBox = it },
                onOpenTripSetBoxChange = { openTripSetBox = true })
        }, padding = 20
        )

        Section(
            firstComponent = {
                Text(
                    text = "최근 기록한 장소", style = AppTypography.titleMedium, modifier = Modifier.then(
                        if (count != 0) Modifier.padding(horizontal = 20.dp) else Modifier
                    )
                )
            },
            secondComponent = { RecentHistoryPlace(count = count) },
            padding = (if (count == 0) 20 else 0)
        )

        Section(firstComponent = {
            Text(
                text = "AI 여행지 추천",
                style = AppTypography.titleMedium,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }, secondComponent = { RecommendPlace() }, padding = 0
        )


    }
}

@Composable
fun Section(
    firstComponent: @Composable () -> Unit, secondComponent: @Composable () -> Unit, padding: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (padding >= 0) Modifier.padding(horizontal = padding.dp) else Modifier
            ), verticalArrangement = Arrangement.spacedBy(10.dp) // 두 컴포넌트 사이 간격 설정
    ) {
        firstComponent()
        secondComponent()
    }
}

@Composable
fun CheckIconTextField(
    focusManager: FocusManager,
    openTripSetBox: Boolean,
    onFocusChange: (Boolean) -> Unit,
    onOpenTripSetBoxChange: (Boolean) -> Unit
) {

    // FocusRequester 객체 생성
    val focusRequester = remember { FocusRequester() }

    var showCaption by remember { mutableStateOf(true) }

    var event by remember {
        mutableStateOf(
            Event(
                title = "", startDate = "", endDate = ""
            )
        )
    }

    Column {
        OutlinedTextField(value = event.title,
            onValueChange = { newTitle ->
                event = event.copy(title = newTitle)
                if(event.title.isNotEmpty()) {
                    showCaption = false
                }
                else {
                    showCaption = true
                }
            },
            placeholder = {
                Text(
                    "이번 여행의 제목은 무엇인가요?", color = TextDarkGray, style = AppTypography.bodyMedium
                )
            },
            trailingIcon = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(end = 10.dp)
                ) {
                    VerticalDivider(
                        color = IconGray, modifier = Modifier
                            .height(32.dp) // 선 높이
                            .width(1.dp) // 선 너비
                    )
                    Icon(imageVector = Icons.Default.Check,
                        contentDescription = "Check Icon",
                        tint = TextDarkGray,
                        modifier = Modifier.clickable {
                            if(event.title.isNotEmpty()) {
                                onFocusChange(false) // 아이콘 클릭 시 포커스 해제
                                focusManager.clearFocus()
                            }
                        }

                    )
                }
            },
            textStyle = TextStyle(color = TextBlack),
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = White,
                unfocusedContainerColor = White,
                focusedIndicatorColor = MainNavy,
                unfocusedIndicatorColor = MainNavy,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .zIndex(1f)
                .focusRequester(focusRequester) // FocusRequester 연결
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        onFocusChange(true)
                    }
                })
        if (openTripSetBox) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .offset(y = (-10).dp)
                .background(BackgroundSky)
                .zIndex(0f)
                .padding(top = 10.dp)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        onOpenTripSetBoxChange(true) // Box 클릭 시 openTripSetBox를 true로 설정
                    })
                }) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Section(firstComponent = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "시작",
                                color = TextDarkGray,
                                style = AppTypography.labelMedium
                            )
                            Text(
                                text = "종료",
                                color = TextDarkGray,
                                style = AppTypography.labelMedium
                            )
                        }
                    }, secondComponent = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            DateInputField(event.startDate, onValueChange = { newDate ->
                                event = event.copy(startDate = newDate)
                            })
                            DateInputField(event.endDate, onValueChange = { newDate ->
                                event = event.copy(endDate = newDate)
                            })
                        }
                    }, padding = 10
                    )

                    Section(firstComponent = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "함께하는 친구",
                                color = TextDarkGray,
                                style = AppTypography.labelMedium
                            )
                            ClickableLink()
                        }
                    }, secondComponent = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            SelectFriends()
                        }
                    }, padding = 0
                    )
                }
            }
            if(showCaption) {
                Text(text = "* 제목을 입력해주세요", color = ErrorRed, style = AppTypography.labelSmall)
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateInputField(value: String, onValueChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .width(149.dp)
            .height(44.dp)
            .border(1.dp, MainNavy, RoundedCornerShape(10.dp))
            .background(White, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center
    ) {
        Text(text = "YYYY/MM/DD", color = TextDarkGray, style = AppTypography.bodyMedium)
    }

    /*textField로 구현했으나 옳은 날짜인지 확인할 수 없는 + 포커싱 문제 발생으로 고민이 필요함... 일단 박스만 둘게요...*/

//    OutlinedTextField(
//        value = value,
//        onValueChange = { newValue ->
//            // 숫자만 입력 가능
//            if (newValue.all { it.isDigit() || it == '/' }) {
//                val cleanedValue = newValue.replace("/", "")
//
//                // 날짜 YYYY/MM/DD 포맷팅
//                val formattedValue = when {
//                    cleanedValue.length >= 6 -> {
//                        "${cleanedValue.take(4)}/${cleanedValue.drop(4).take(2)}/${cleanedValue.drop(6).take(2)}"
//                    }
//                    cleanedValue.length >= 4 -> {
//                        "${cleanedValue.take(4)}/${cleanedValue.drop(4).take(2)}"
//                    }
//                    else -> {
//                        cleanedValue
//                    }
//                }
//
//                onValueChange(formattedValue)
//            }
//        },
//        placeholder = {
//            Text("YYYY/MM/DD", color = TextDarkGray, style = AppTypography.bodyMedium)
//        },
//        textStyle = TextStyle(
//            color = TextBlack,
//            textAlign = TextAlign.Start,  // 만약 중앙 정렬을 원한다면 TextAlign.Center로 변경
//        ),
//        keyboardOptions = KeyboardOptions.Default.copy(
//            keyboardType = KeyboardType.Number // 숫자 입력을 위한 키보드 설정
//        ),
//        shape = RoundedCornerShape(10.dp),
//        modifier = Modifier
//            .width(149.dp)
//            .padding(0.dp),  // 기본 패딩을 없앰
//        colors = TextFieldDefaults.outlinedTextFieldColors(
//            unfocusedBorderColor = MainNavy, // 예시로 unfocused 상태일 때의 테두리 색을 설정
//            focusedBorderColor = MainNavy,   // focused 상태일 때의 테두리 색을 설정
//            containerColor = Color.White      // 배경을 하얀색으로 설정
//        ),
//    )
}

@Composable
fun ClickableLink() {
    val context = LocalContext.current  // 컴포저블 함수 안에서만 사용해야 합니다.

    //임시로 구글 연결
    val annotatedString = buildAnnotatedString {
        pushStringAnnotation(tag = "URL", annotation = "https://www.google.com/")
        append("친구초대 링크")
        pop()
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ClickableText(text = annotatedString,
            style = AppTypography.labelMedium.copy(color = TextDarkGray),
            onClick = { offset ->
                annotatedString.getStringAnnotations("URL", offset, offset).firstOrNull()
                    ?.let { annotation ->
                        // 링크를 열기 위해 Intent 사용
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                        context.startActivity(intent)  // 컴포저블 내에서 context를 안전하게 사용
                    }
            })
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.link_icon),
            contentDescription = "Link",
            tint = TextDarkGray
        )
    }
}

@Composable
fun SelectFriends() {
    var selectedFriends by remember { mutableStateOf<List<Friend>>(emptyList()) }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 10.dp)
    ) {
        items(sampleFriends.toList()) { friend ->
            FriendCirCle(friend = friend,
                isSelected = selectedFriends.contains(friend), // 선택된 친구와 비교
                onClick = {
                    // 친구가 선택된 상태라면 리스트에서 제거, 아니면 추가
                    selectedFriends = if (selectedFriends.contains(friend)) {
                        selectedFriends - friend // 이미 선택된 아이템을 클릭하면 제거
                    } else {
                        selectedFriends + friend // 새로 선택된 아이템을 추가
                    }
                })
        }

        item {
            AddFriend()
        }
    }
}

@Composable
fun FriendCirCle(friend: Friend, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier
            .size(50.dp) // 원하는 크기로 설정
            .clip(CircleShape) // 원형 모양으로 자르기
            .background(White) // 배경색 (선택사항)
            .border(1.dp, if (isSelected) MainNavy else TextLightGray, CircleShape)
            .clickable { onClick() }) {
//        Image(
//
//        )
        }
        Text(text = friend.name, style = AppTypography.labelMedium, color = TextBlack)
    }

}

@Composable
fun AddFriend() {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(50.dp) // 원하는 크기로 설정
                .clip(CircleShape) // 원형 모양으로 자르기
                .border(1.dp, MainNavy, CircleShape), contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.plus_icon),
                contentDescription = "Plus",
                tint = MainNavy
            )
        }
        Text(text = "친구 추가하기", style = AppTypography.labelMedium, color = TextBlack)
    }
}


@Composable
fun RecentHistoryPlace(count: Int = 0) {
    if (count == 0) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .border(1.dp, MainNavy, shape = RoundedCornerShape(10.dp)) // 테두리 추가
                .background(White, shape = RoundedCornerShape(10.dp)) // 배경색 추가
                .fillMaxWidth()
                .height(92.dp)
        ) {
            Text(
                text = "아직 기록한 곳이 없습니다.",
                style = AppTypography.bodyMedium,
                color = TextDarkGray,
            )
            Text(
                text = "골목별과 여행을 시작해보세요!",
                style = AppTypography.bodyMedium,
                color = TextDarkGray,
            )
        }
    } else {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(horizontal = 20.dp)
        ) {
            items(sampleHistories.toList()) { history ->
                HistoryCard(history)
            }
        }

    }
}

@Composable
fun RecommendPlace() {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(horizontal = 20.dp)

    ) {
        items(samplePlaces.toList()) { place ->
            PlaceCard(place)
        }
    }
}

@Composable
fun PlaceCard(place: Place) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(270.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Gray
        )
    ) {
        Column(
            modifier = Modifier.padding(5.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            NameCard(place)
        }

    }
}

@Composable
fun HistoryCard(history: History) {
    var isClicked by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .width(200.dp)
            .height(270.dp)
            .clickable { isClicked = !isClicked },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Gray
        )
    ) {
        if (!isClicked) {
            Column(
                modifier = Modifier.padding(5.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                NameCard(history)
            }
        } else {
            OverCard(history)
        }


    }
}


@Composable
fun NameCard(data: Any) {
    when (data) {
        is Place -> {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(67.dp),
                shape = RoundedCornerShape(15.dp),
                colors = CardDefaults.cardColors(
                    containerColor = BlurBackgroundGray
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = data.name, color = White, style = AppTypography.bodyMedium
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(1.dp)
                    ) {
                        //ImageVector.vectorResource(id = R.drawable.home_icon)
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.location_icon),
                            contentDescription = "Location",
                            tint = TextLightGray
                        )
                        Text(
                            text = data.address,
                            color = TextLightGray,
                            style = AppTypography.labelMedium
                        )
                    }
                }
            }
        }

        is History -> {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(67.dp),
                shape = RoundedCornerShape(15.dp),
                colors = CardDefaults.cardColors(
                    containerColor = BlurBackgroundGray
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = data.name, color = White, style = AppTypography.bodyMedium
                        )
                        Text(
                            text = data.title,
                            color = TextLightGray,
                            style = AppTypography.labelLarge
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(1.dp)
                        ) {
                            //ImageVector.vectorResource(id = R.drawable.home_icon)
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.location_icon),
                                contentDescription = "Location",
                                tint = TextLightGray
                            )
                            Text(
                                text = data.address,
                                color = TextLightGray,
                                style = AppTypography.labelMedium
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp),

                            ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.star_icon),
                                contentDescription = "Star",
                                tint = TextLightGray

                            )
                            Text(
                                text = data.rating.toString(),
                                color = TextLightGray,
                                style = AppTypography.labelMedium
                            )
                        }
                    }


                }
            }
        }
    }
}


@Composable
fun OverCard(history: History) {
    Card(
        modifier = Modifier.fillMaxSize(), colors = CardDefaults.cardColors(BlurBackgroundGray)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = history.name, color = White, style = AppTypography.bodyMedium)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.star_icon),
                        contentDescription = "Star",
                        tint = White
                    )
                    Text(
                        text = history.rating.toString(),
                        color = White,
                        style = AppTypography.labelMedium
                    )
                }
            }

            Text(text = history.address, color = TextLightGray, style = AppTypography.labelMedium)
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text(
                    text = history.content,
                    color = White,
                    style = AppTypography.labelMedium,
                    )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = history.title, color = TextLightGray, style = AppTypography.labelMedium)
                Text(text = history.date, color = TextLightGray, style = AppTypography.labelMedium)
            }

        }

    }


}
