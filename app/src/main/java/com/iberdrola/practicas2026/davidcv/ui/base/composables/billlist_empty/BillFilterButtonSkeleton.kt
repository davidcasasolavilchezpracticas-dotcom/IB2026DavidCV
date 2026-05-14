package com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_empty

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.iberdrola.practicas2026.davidcv.ui.base.composables.modifier_extensions.shimmerEffect
import com.iberdrola.practicas2026.davidcv.ui.theme.SkeletonGreen

@Composable
@Preview
fun BillFilterButtonSkeleton(
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
    ) {
        val startGuide = createGuidelineFromStart(0f)
        val endGuide = createGuidelineFromEnd(0f)
        val bottomGuide = createGuidelineFromBottom(0f)

        val (historical, filterButton) = createRefs()

        Box(
            modifier = modifier
                .height(24.dp)
                .width(150.dp)
                .constrainAs(historical){
                    start.linkTo(startGuide)
                    bottom.linkTo(bottomGuide)
                }
                .clip(RoundedCornerShape(4.dp))
                .shimmerEffect()
        )

        Box(
            modifier = modifier
                .height(32.dp)
                .width(125.dp)
                .constrainAs(filterButton) {
                    end.linkTo(endGuide)
                    bottom.linkTo(bottomGuide)
                }
                .fillMaxWidth(0.5f)
                .clip(RoundedCornerShape(24.dp))
                .shimmerEffect()
        )
    }
}