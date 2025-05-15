package com.zinchuk.scandoc.presentation.screens.main.components.pages

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.zinchuk.scandoc.presentation.models.DocumentSetItemData
import com.zinchuk.scandoc.presentation.navigation.Route
import com.zinchuk.scandoc.presentation.screens.main.components.item.DocumentSetItem
import java.util.UUID

@Composable
fun DocumentSetsPage(
    documentSets: LazyPagingItems<DocumentSetItemData>,
    navController: NavHostController,
    onDeleteDocumentSet: (UUID) -> Unit,
) {
    LazyColumn(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(
            count = documentSets.itemCount,
            key = { documentSets[it]?.uuid ?: 0 },
        ) { idx ->
            documentSets[idx]?.let {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .animateItem(),
                ) {
                    DocumentSetItem(
                        data = it,
                        onClick = {
                            navController.navigate(
                                Route.DETAILS_SCREEN.route + "/${Uri.encode(it.uuid.toString())}",
                            )
                        },
                        onDelete = onDeleteDocumentSet,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
        if (documentSets.loadState.append is LoadState.Loading) {
            item {
                CircularProgressIndicator(
                    modifier =
                        Modifier
                            .size(32.dp),
                )
            }
        }
    }
}
