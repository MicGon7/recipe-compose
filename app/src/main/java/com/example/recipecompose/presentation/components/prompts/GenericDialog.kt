package com.example.recipecompose.presentation.components.prompts

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.lang.NullPointerException
import java.util.*

@Composable
fun GenericDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    title: String,
    description: String? = null,
    positiveAction: PositiveAction? = null,
    negativeAction: NegativeAction? = null
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            if (description != null) {
                Text(description)
            }
        },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                if (negativeAction != null) {
                    Button(
                        modifier = Modifier.padding(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.error
                        ),
                        onClick = negativeAction.onNegativeAction
                    ) {
                        Text(negativeAction.negativeBtnText)
                    }
                }
                if (positiveAction != null) {
                    Button(
                        modifier = Modifier.padding(8.dp),
                        onClick = positiveAction.onPositiveAction
                    ) {
                        Text(positiveAction.positiveBtnTxt)
                    }
                }
            }
        }
    )
}

data class PositiveAction(
    val positiveBtnTxt: String,
    val onPositiveAction: () -> Unit
)

data class NegativeAction(
    val negativeBtnText: String,
    val onNegativeAction: () -> Unit
)

// TODO: Possibly remove -- may be doing way to much
class GenericDialogInfo
private constructor(builder: Builder) {
    val title: String
    val onDismiss: () -> Unit
    val description: String?
    val positiveAction: PositiveAction?
    val negativeAction: NegativeAction?

    init {
        if (builder.title == null) {
            throw NullPointerException("GenericDialogInfo title cannot be null. ")
        }
        if (builder.onDismiss == null) {
            throw NullPointerException("GenericDialogInfo onDismiss function cannot be null. ")
        }
        // TODO: Find a way to remove bangs
        this.title = builder.title!!
        this.onDismiss = builder.onDismiss!!
        this.description = builder.description
        this.positiveAction = builder.positiveAction
        this.negativeAction = builder.negativeAction
    }

    class Builder {
        var title: String? = null
            private set

        var onDismiss: (() -> Unit)? = null

        var description: String? = null

        var positiveAction: PositiveAction? = null

        var negativeAction: NegativeAction? = null

        fun title(title: String): Builder {
            this.title = title
            return this
        }

        fun onDismiss(onDismiss: () -> Unit): Builder {
            this.onDismiss = onDismiss
            return this
        }

        fun description(description: String): Builder {
            this.description = description
            return this
        }

        fun positive(positiveAction: PositiveAction): Builder {
            this.positiveAction = positiveAction
            return this
        }

        fun negative(negativeAction: NegativeAction): Builder {
            this.negativeAction = negativeAction
            return this
        }

        fun build() = GenericDialogInfo(this)

    }

}

@Composable
fun DisplayErrorDialog(
    dialogQueue: Queue<GenericDialogInfo>?
) {
    dialogQueue?.peek()?.let { dialogInfo ->
        GenericDialog(
            title = dialogInfo.title,
            onDismiss = dialogInfo.onDismiss,
            description = dialogInfo.description,
            positiveAction = dialogInfo.positiveAction,
            negativeAction = dialogInfo.negativeAction
        )
    }
}