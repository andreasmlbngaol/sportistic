package com.jawapbo.sportistic.features.main.maps.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.google.android.gms.maps.model.LatLng
import com.jawapbo.sportistic.core.component.IconTextButton
import com.jawapbo.sportistic.shared.data.merchants.Merchant

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PlaceBottomSheet(
    merchant: Merchant,
    bookmarkStatus: Boolean,
    onDismissRequest: () -> Unit,
    onNavigate: (location: LatLng, name: String) -> Unit,
    onAddToBookmark: (placeId: Long) -> Unit,
    onRemoveFromBookmark: (placeId: Long) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onDismissRequest() },
        sheetState = sheetState
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .heightIn(min = 400.dp)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = merchant.name,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = merchant.description ?: "No Description",
                style = MaterialTheme.typography.bodyMedium
            )

            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
            ) {
                IconTextButton(
                    onClick = {
                        onNavigate(
                            LatLng(merchant.latitude, merchant.longitude),
                            merchant.name
                        )
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Navigation,
                            contentDescription = "Navigate",
                            modifier = Modifier
                                .rotate(30f)
                        )
                    },
                    label = "Navigate",
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
                Spacer(Modifier.size(4.dp))

                IconTextButton(
                    onClick = {},
                    imageVector = Icons.Filled.Payment,
                    label = "Book",
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    )
                )
                Spacer(Modifier.size(4.dp))

                FilledIconButton(
                    onClick = {},
                    colors = IconButtonDefaults.filledIconButtonColors().copy(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Detail"
                    )
                }

                AnimatedContent(bookmarkStatus) { bookmarked ->
                    FilledIconButton(
                        onClick = {
                            if(!bookmarked) onAddToBookmark(merchant.id) else onRemoveFromBookmark(merchant.id)
                        },
                        colors = IconButtonDefaults.filledIconButtonColors().copy(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onTertiary
                        )
                    ) {
                        Icon(
                            imageVector = if(bookmarked) Icons.Filled.Check else Icons.Filled.Bookmark,
                            contentDescription = "Save"
                        )
                    }
                }
            }

            AsyncImage(
                model = merchant.imageUrl,
                contentDescription = "${merchant.name} Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .widthIn(max = 450.dp)
                    .heightIn(max = 200.dp)
            )
        }
    }
}