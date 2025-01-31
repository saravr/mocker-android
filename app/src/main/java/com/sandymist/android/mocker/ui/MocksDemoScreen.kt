package com.scribd.android.mocker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.scribd.android.mocker.model.Account
import com.scribd.android.mocker.model.Users

@Composable
fun MocksDemoScreen(
    modifier: Modifier = Modifier,
    userItems: List<Users.UsersItem>,
    account: Account?,
    refresh: () -> Unit,
    launchMockActivity: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = {
                launchMockActivity()
            },
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text("Mock Settings")
        }

        HorizontalDivider()

        Text(
            account?.result?.email ?: "No email",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )

        Button(
            onClick = {
                refresh()
            },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("Refresh")
        }

        HorizontalDivider()

        LazyColumn(
            verticalArrangement = Arrangement.Top,
            contentPadding = PaddingValues(vertical = 10.dp)
        ) {
            items(userItems) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = it.name ?: "No name",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(vertical = 8.dp),
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = it.website ?: "No website",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.DarkGray,
                        )
                        Text(
                            text = it.address?.city ?: "No city",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.DarkGray,
                        )
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewMocksDemoScreen() {
    val userItems = listOf(
        Users.UsersItem(
            name = "Joe Bloe",
            id = 1,
            email = "joe@bloe.com",
            website = "legends.com",
            address = Users.UsersItem.Address(city = "Chennai"),
        ),
        Users.UsersItem(
            name = "Scarlett J",
            id = 2,
            email = "john@acme.com",
            website = "intel.org",
            address = Users.UsersItem.Address(city = "Paris"),
        ),
    )
    MocksDemoScreen(
        userItems = userItems,
        account = null,
        refresh = {},
    ) { }
}
