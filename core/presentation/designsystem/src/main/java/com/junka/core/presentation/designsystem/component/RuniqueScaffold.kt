package com.junka.core.presentation.designsystem.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RuniqueScaffold(
    withGradient : Boolean = true,
    modifier : Modifier = Modifier,
    topAppBar : @Composable () -> Unit = {},
    floatingActionButton : @Composable () -> Unit = {},
    content : @Composable (PaddingValues) -> Unit ,
) {
    Scaffold(
        topBar = topAppBar,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = FabPosition.Center,
        modifier = modifier
    ) { padding ->
        if (withGradient) {
            RuniqueBackground {
                content(padding)
            }
        }else{
            content(padding)
        }
    }
}