package com.example.documentscanner.ui.screens.home.components

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.documentscanner.R
import com.example.documentscanner.models.PdfEntity
import com.example.documentscanner.utils.getFileUri
import com.example.documentscanner.viewModels.PdfViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PdfLayout(pdfEntity: PdfEntity, pdfViewModel: PdfViewModel) {

    val context = LocalContext.current
    val activity = LocalContext.current as Activity

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp),
        onClick = {
            val getFileUri = getFileUri(context, pdfEntity.name)
            val browserIntent = Intent(Intent.ACTION_VIEW, getFileUri)

            browserIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            activity.startActivity(browserIntent)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(80.dp),
                painter = painterResource(id = R.drawable.ic_pic_pdf),
                contentDescription = "Pdf Icon"
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = pdfEntity.name, style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "Size: ${pdfEntity.size}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Date: ${
                        SimpleDateFormat(
                            "dd-MM-yyyy HH:mm a",
                            Locale.getDefault()
                        ).format(pdfEntity.lastModifiedTime)
                    }",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(onClick = {
                pdfViewModel.currentPdfEntity = pdfEntity
                pdfViewModel.showRenameDialog = true
            }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More"
                )
            }
        }
    }

}