package com.scribd.android.mocker.apimock.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.scribd.android.mocker.apimock.db.MockEntity
import com.scribd.android.mocker.utils.debouncedClickable
import com.scribd.android.mocker.utils.toTimeAgo

@Composable
fun MocksScreen(
    modifier: Modifier = Modifier,
    mockList: List<MockEntity>,
    mockEnabled: Boolean = false,
    setMockEnabled: (Boolean) -> Unit = {},
    setMockPartial: (MockEntity, Boolean) -> Unit,
//    addMockClicked: () -> Unit,
    onToggled: (MockEntity) -> Unit,
    navigateToDetail: (MockEntity) -> Unit,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//            ,
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween,
//        ) {
//            Text(text = "Mocks", style = MaterialTheme.typography.titleLarge)
//
//            IconButton(
//                onClick = { addMockClicked() }
//            ) {
//                Icon(
//                    imageVector = Icons.Filled.Add,
//                    contentDescription = "",
//                    tint = if (mockEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy()
//                )
//            }
//        }
//        HorizontalDivider()

        Text("Changes will take effect after restart", modifier = Modifier.padding(vertical = 8.dp))
        Row(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Enable mocks", style = MaterialTheme.typography.titleMedium)
            Switch(
                checked = mockEnabled,
                onCheckedChange = {
                    setMockEnabled(!mockEnabled)
                }
            )
        }

        HorizontalDivider()

        if (mockList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No items", style = MaterialTheme.typography.titleLarge)
            }
        } else {
            MockList(mockList, navigateToDetail, onToggled, setMockPartial)
        }
    }
}

@Composable
fun MockList(
    mockList: List<MockEntity>,
    navigateToDetail: (MockEntity) -> Unit,
    onToggled: (MockEntity) -> Unit,
    setMockPartial: (MockEntity, Boolean) -> Unit,
    deleteMock: (MockEntity) -> Unit = {},
) {
    LazyColumn(
        modifier = Modifier.padding(top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(mockList) {
            MockItem(
                mockEntity = it,
                onToggled = { item ->
                    onToggled(item)
                },
                onSelect = { item ->
                    navigateToDetail(item)
                },
                setMockPartial = setMockPartial,
                onRemove = { item ->
                    deleteMock(item)
                }
            )
            HorizontalDivider()
        }
    }
}

@Composable
fun MockItem(
    mockEntity: MockEntity,
    onToggled: (MockEntity) -> Unit,
    setMockPartial: (MockEntity, Boolean) -> Unit,
    onSelect: (MockEntity) -> Unit,
    @Suppress("UNUSED_PARAMETER") onRemove: (MockEntity) -> Unit
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
            .debouncedClickable {
                onSelect(mockEntity)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(text = mockEntity.method + " " + mockEntity.path)
            Text(mockEntity.name)
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MiniCheckBox (
                    title = "Partial",
                    selected = mockEntity.partial,
                    onSelected = {
                        setMockPartial(mockEntity, !mockEntity.partial)
                    }
                )
            }
            Text((mockEntity.createdAt / 1000).toTimeAgo(context), color = Color.DarkGray, fontStyle = FontStyle.Italic)
        }
        Switch(
            checked = mockEntity.enabled,
            onCheckedChange = {
                onToggled(mockEntity)
            }
        )
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MockItem(
//    mockEntity: MockEntity,
//    modifier: Modifier = Modifier,
//    onRemove: (MockEntity) -> Unit
//) {
//    val currentItem by rememberUpdatedState(mockEntity)
//    val dismissState = rememberSwipeToDismissBoxState(
//        confirmValueChange = {
//            when(it) {
//                SwipeToDismissBoxValue.StartToEnd -> {
//                    onRemove(currentItem)
//                    //Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
//                }
//                SwipeToDismissBoxValue.EndToStart -> {
//                    onRemove(currentItem)
//                    //Toast.makeText(context, "Item archived", Toast.LENGTH_SHORT).show()
//                }
//                SwipeToDismissBoxValue.Settled -> return@rememberSwipeToDismissBoxState false
//            }
//            return@rememberSwipeToDismissBoxState true
//        },
//        // positional threshold of 25%
//        positionalThreshold = { it * .25f }
//    )
//    SwipeToDismissBox(
//        state = dismissState,
//        modifier = modifier,
//        backgroundContent = { DismissBackground(dismissState)},
//        content = {
//            Text(
//                text = mockEntity.path,
//                modifier = Modifier
//                    .padding(16.dp)
//                    .debouncedClickable {
//                        onRemove(mockEntity)
//                    }
//            )
//        })
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DismissBackground(dismissState: SwipeToDismissBoxState) {
//    val color = when (dismissState.dismissDirection) {
//        SwipeToDismissBoxValue.StartToEnd -> Color(0xFFFF1744)
//        SwipeToDismissBoxValue.EndToStart -> Color(0xFF1DE9B6)
//        SwipeToDismissBoxValue.Settled -> Color.Transparent
//    }
//
//    Row(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(color)
//            .padding(12.dp, 8.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Icon(
//            Icons.Default.Delete,
//            contentDescription = "delete"
//        )
////        Spacer(modifier = Modifier)
////        Icon(
////            // make sure add baseline_archive_24 resource to drawable folder
////            painter = painterResource(R.drawable.baseline_archive_24),
////            contentDescription = "Archive"
////        )
//    }
//}

@Preview(
    showSystemUi = true,
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun PreviewMocksScreen() {
    val mockTestList = listOf(
        MockEntity(
            id = "1",
            name = "Test 1",
            method = "GET",
            path = "/test",
            payload = "Hello World!",
            enabled = true,
            partial = false,
        ),
        MockEntity(
            id = "2",
            name = "Test 2",
            method = "GET",
            path = "/test2",
            payload = "Hello World!",
            enabled = false,
            partial = true,
        )
    )

    MocksScreen(
        mockList = mockTestList,
        mockEnabled = true,
        //addMockClicked = {},
        onToggled = {},
        navigateToDetail = {},
        setMockPartial = { _, _ -> },
    )
}
