package com.zinchuk.scandoc.presentation.screens.main.components.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.zinchuk.scandoc.R
import com.zinchuk.scandoc.presentation.models.DocumentSetItemData
import java.util.UUID

@Composable
fun DocumentSetItem(
    data: DocumentSetItemData,
    onClick: () -> Unit = {},
    onDelete: (UUID) -> Unit = {},
) {
    ElevatedCard(
        modifier =
            Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        onClick = onClick,
    ) {
        Box {
            Column {
                AsyncImage(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                    model =
                        ImageRequest
                            .Builder(LocalContext.current)
                            .data(data.previewImage)
                            .crossfade(true)
                            .build(),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    modifier =
                        Modifier
                            .padding(horizontal = 8.dp, vertical = 2.dp),
                    text = data.name,
                    fontSize = 22.sp,
                )
                Text(
                    modifier =
                        Modifier
                            .padding(horizontal = 8.dp, vertical = 2.dp),
                    text =
                        pluralStringResource(
                            R.plurals.document_set_item_pages,
                            data.numberOfPages,
                            data.numberOfPages,
                        ),
                    fontSize = 16.sp,
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            Text(
                modifier =
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                text = data.createdAt,
                fontSize = 16.sp,
            )
            Box(
                modifier =
                    Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-8).dp, y = 8.dp),
            ) {
                var menuVisible by remember { mutableStateOf(false) }
                IconButton(
                    modifier =
                        Modifier
                            .background(Color.White, RoundedCornerShape(percent = 100))
                            .size(32.dp),
                    onClick = { menuVisible = true },
                ) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = null,
                    )
                }
                DropdownMenu(
                    modifier =
                        Modifier
                            .align(Alignment.TopStart),
                    expanded = menuVisible,
                    onDismissRequest = { menuVisible = false },
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.document_set_item_delete)) },
                        onClick = {
                            onDelete.invoke(data.uuid)
                            menuVisible = false
                        },
                        leadingIcon = { Icon(Icons.Outlined.Delete, null) },
                    )
                }
            }
        }
    }
}
